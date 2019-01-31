package com.sameer.tdd;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Arrays;
/**
 * Acts as a entry point and controller of user input processing.  
 * 
 * @author Sameer
 *
 */
public class CurrencyManager {
	private CurrencyConverter cc;
	private Path outputPath;
	private static final String errorMessage = "I have no idea what you are talking about";

	public CurrencyManager(CurrencyConverter cc, String outputFileName) {
		this.cc = cc;
		outputPath = Paths.get(outputFileName);
	}

	/**
	 * 
	 * @param input multiline input from user for processing
	 * For each line , interpret what is the user action and delegate to appropriate actionHandler.
	 * Write processing results to the output file 
	 * @throws UnsupportedEncodingException
	 * @throws IOException
	 */
	public void acceptUserInput(String input) throws UnsupportedEncodingException, IOException {
		String[] lines = input.split(System.lineSeparator());
		for (String line : lines) {
			String[] tokens = line.split("\\s");
			if (tokens.length == 3) {
				cc.createCurrencyMapping(tokens);
			} else if (line.startsWith("how much is") && line.endsWith("?")) {
				long arabicNumber = cc
						.galacticToArabicCurrencyConversion(line.substring(12, line.length() - 1).split("\\s"));
				if (0 != arabicNumber) {
					Files.write(outputPath,
							(line.substring(12, line.length() - 1) + "is " + arabicNumber + System.lineSeparator())
									.getBytes("ISO-8859-1"),
							StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				}
			} else if (line.endsWith("Credits")) {
				cc.createMetalValueMapping(line.substring(0, line.length() - 8).split("\\s"));
			} else if (line.startsWith("how many Credits is") && line.endsWith("?")) {
				Float creditsValue = cc.calculateCredits(Arrays.copyOfRange(tokens, 4, tokens.length - 1));
				if (null != creditsValue) {
					Files.write(outputPath,
							(line.substring(19, line.indexOf('?')) + "is " + creditsValue + " Credits"
									+ System.lineSeparator()).getBytes("ISO-8859-1"),
							StandardOpenOption.CREATE, StandardOpenOption.APPEND);
				}
			} else {
				Files.write(outputPath, (errorMessage + System.lineSeparator()).getBytes("ISO-8859-1"),
						StandardOpenOption.CREATE, StandardOpenOption.APPEND);
			}
		}
	}
}
