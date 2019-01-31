package com.sameer.tdd;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
/**
 * Processes user input and performs required validations and computations for the galactic currency conversions
 * @author Sameer
 *
 */
public class CurrencyConverter {
	private Map<Character, Integer> romanToNumeral = new HashMap<>();
	private Map<String, String> galacticToRoman = new HashMap<>();
	private Map<String, Float> metalValue = new HashMap<>();

	public Map<String, String> getGalacticToRoman() {
		return galacticToRoman;
	}

	public Map<String, Float> getMetalValue() {
		return metalValue;
	}

	public CurrencyConverter() {
		romanToNumeral.put('I', 1);
		romanToNumeral.put('V', 5);
		romanToNumeral.put('X', 10);
		romanToNumeral.put('L', 50);
		romanToNumeral.put('C', 100);
		romanToNumeral.put('D', 500);
		romanToNumeral.put('M', 1000);
	}

	/**
	 * 
	 * @param tokens tokenized user input containing galactic currency and metal symbols.
	 * @return credit valuation of supplied input.returns 0 in case of processing errors 
	 */
	public Float calculateCredits(String[] tokens) {
		String romanString = galacticToRomanCurrencyConversion(Arrays.copyOfRange(tokens, 0, tokens.length - 1));
		if (null == romanString) {
			return 0f;
		}
		if (null == metalValue.get(tokens[tokens.length - 1])) {
			return 0f;
		}
		return romanToArabicCurrencyConverter(romanString) * metalValue.get(tokens[tokens.length - 1]);
	}

	private String galacticToRomanCurrencyConversion(String[] tokens) {
		StringBuilder romanString = new StringBuilder();
		for (String token : tokens) {
			String roman = galacticToRoman.get(token);
			if (null == roman) {
				return null;
			}
			if (romanString.length() >= 1 && romanString.charAt(romanString.length() - 1) == 'I') {
				if (positionCheck(roman, new char[] { 'I', 'V', 'X' }) == false) {
					return null;
				}
			}
			if (romanString.length() >= 1 && romanString.charAt(romanString.length() - 1) == 'V') {
				if (positionCheck(roman, new char[] { 'I' }) == false) {
					return null;
				}
			}
			romanString.append(roman);
		}
		if (applyRomanCurrencyOccurenceRules(romanString.toString())) {
			return romanString.toString();
		}
		return null;
	}

	private boolean positionCheck(String current, char[] allowed) {
		for (char token : allowed) {
			if (current.charAt(0) == token) {
				return true;
			}
		}
		return false;
	}

	private boolean applyRomanCurrencyOccurenceRules(String roman) {
		return ixcmOccurenceCheck(roman) && maxOneOccurenceCheck(roman, "L") && maxOneOccurenceCheck(roman, "D")
				&& maxOneOccurenceCheck(roman, "V");
	}

	private boolean maxOneOccurenceCheck(String roman, String token) {
		if (roman.length() - roman.replaceAll(token, "").length() > 1) {
			return false;
		}
		return true;
	}

	private boolean ixcmOccurenceCheck(String roman) {
		Pattern pattern = Pattern.compile("([IXCM])\\1{3,}", Pattern.DOTALL);
		Matcher matcher = pattern.matcher(roman);
		if (matcher.find()) {
			return false;
		}
		return true;
	}

	private boolean validateInputForCurrencyMapping(String[] tokens) {
		if (!tokens[1].equalsIgnoreCase("is"))
			return false;
		if (!romanToNumeral.containsKey(tokens[2].charAt(0)))
			return false;
		return true;
	}

	public long romanToArabicCurrencyConverter(String input) {
		char[] array = input.toCharArray();
		long result = 0;
		for (int i = 0; i < array.length - 1; i++) {
			if (romanToNumeral.get(array[i]) >= romanToNumeral.get(array[i + 1])) {
				result += romanToNumeral.get(array[i]);
			} else {
				result += (romanToNumeral.get(array[i + 1]) - romanToNumeral.get(array[i]));
				i++;
			}
		}
		if (romanToNumeral.get(array[array.length - 1]) <= romanToNumeral.get(array[array.length - 2])) {
			result += romanToNumeral.get(array[array.length - 1]);
		}
		return result;
	}
    /**
     * 
     * @param tokens tokenized user input containing value mappings between galactic and roman currency symbols
     * @return true if mapped successfully.
     */
	public boolean createCurrencyMapping(String[] tokens) {
		boolean valid = validateInputForCurrencyMapping(tokens);
		if (valid) {
			galacticToRoman.put(tokens[0], tokens[2]);
		}
		return valid;
	}

	/**
	 * 
	 * @param tokens tokenized user input containing galactic currency symbols
	 * @return Equivalent currency value in Arabic. 0 if error in conversion. 
	 */
	public long galacticToArabicCurrencyConversion(String[] tokens) {
		String romanString = galacticToRomanCurrencyConversion(tokens);
		if (null == romanString) {
			return 0;
		} else {
			return romanToArabicCurrencyConverter(romanString);
		}
	}

	/**
	 * compute the value of metal present in user input based upon the equation provided in terms of galactic currency
	 * @param tokens tokenized user input containing galactic currency symbols and a metal symbol
	 */
	public void createMetalValueMapping(String[] tokens) {
		boolean valid = validateInputForMetalValueMapping(tokens);
		if (valid) {
			String romanString = galacticToRomanCurrencyConversion(Arrays.copyOfRange(tokens, 0, tokens.length - 3));
			if (null != romanString) {
				metalValue.put(tokens[tokens.length - 3],
						Float.valueOf(tokens[tokens.length - 1]) / romanToArabicCurrencyConverter(romanString));
			}
		}
	}

	private boolean validateInputForMetalValueMapping(String[] tokens) {
		if (!Pattern.compile("[0-9]").matcher(tokens[tokens.length - 1]).find()) {
			return false;
		}
		if (!tokens[tokens.length - 2].equals("is")) {
			return false;
		}
		return true;
	}
}
