package com.sameer.tdd;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

import org.junit.Before;
import org.junit.Test;

public class CurrencyConverterTest {

	CurrencyConverter cc;

	@Before
	public void init() {
		cc = new CurrencyConverter();
	}

	@Test
	public void testIfCurrencyMappingIsValid() {
		String input = "glob is I";
		String[] tokens = input.split("\\s");
		cc.createCurrencyMapping(tokens);
		assertEquals(tokens[2], cc.getGalacticToRoman().get(tokens[0]));
	}

	@Test
	public void testIfRomanCurrencyIsNotValid() {
		String input = "glob is U";
		String[] tokens = input.split("\\s");
		cc.createCurrencyMapping(tokens);
		assertEquals(false, cc.getGalacticToRoman().containsKey(tokens[0]));
	}

	@Test
	public void testIfIsTokenMisplaced() {
		String input = "glob I is";
		String[] tokens = input.split("\\s");
		cc.createCurrencyMapping(tokens);
		assertEquals(false, cc.getGalacticToRoman().containsKey(tokens[0]));
	}

	@Test
	public void testCreateMetalValueMapping() {
		cc.createCurrencyMapping("glob is I".split("\\s"));
		cc.createMetalValueMapping("glob glob Silver is 34".split("\\s"));
		assertEquals(17, cc.getMetalValue().get("Silver").intValue());
	}

	@Test
	public void testGalacticToArabicConversion() {
		cc.createCurrencyMapping("glob is I".split("\\s"));
		cc.createCurrencyMapping("prok is V".split("\\s"));
		cc.createCurrencyMapping("pish is X".split("\\s"));
		cc.createCurrencyMapping("tegj is L".split("\\s"));
		assertEquals(42, cc.galacticToArabicCurrencyConversion("pish tegj glob glob".split("\\s")));
	}

	@Test
	public void testCreditsCalculator() {
		cc.createCurrencyMapping("glob is I".split("\\s"));
		cc.createCurrencyMapping("prok is V".split("\\s"));
		cc.createMetalValueMapping("glob glob Silver is 34".split("\\s"));
		assertEquals(68, cc.calculateCredits("glob prok Silver".split("\\s")).intValue());
	}

	@Test
	public void testXrepeatsMoreThanThrice() {
		cc.createCurrencyMapping("glob is I".split("\\s"));
		cc.createCurrencyMapping("prok is V".split("\\s"));
		cc.createCurrencyMapping("pish is X".split("\\s"));
		cc.createCurrencyMapping("tegj is L".split("\\s"));
		assertEquals(0, cc.galacticToArabicCurrencyConversion("pish pish pish pish".split("\\s")));
	}

	@Test
	public void testLoccursMoreThanOnce() {
		cc.createCurrencyMapping("glob is I".split("\\s"));
		cc.createCurrencyMapping("prok is V".split("\\s"));
		cc.createCurrencyMapping("pish is X".split("\\s"));
		cc.createCurrencyMapping("tegj is L".split("\\s"));
		assertEquals(0, cc.galacticToArabicCurrencyConversion("tegj tegj pish pish".split("\\s")));
	}

	@Test
	public void testIposition() {
		cc.createCurrencyMapping("glob is I".split("\\s"));
		cc.createCurrencyMapping("prok is V".split("\\s"));
		cc.createCurrencyMapping("pish is X".split("\\s"));
		cc.createCurrencyMapping("tegj is L".split("\\s"));
		assertEquals(0, cc.galacticToArabicCurrencyConversion("glob tegj".split("\\s")));
	}

	@Test
	public void testVposition() {
		cc.createCurrencyMapping("glob is I".split("\\s"));
		cc.createCurrencyMapping("prok is V".split("\\s"));
		cc.createCurrencyMapping("pish is X".split("\\s"));
		cc.createCurrencyMapping("tegj is L".split("\\s"));
		assertEquals(0, cc.galacticToArabicCurrencyConversion("prok tegj".split("\\s")));
	}

	@Test
	public void testRomanToArabic() {
		String input = "XLII";
		assertEquals(42, cc.romanToArabicCurrencyConverter(input));
	}

	@Test
	public void testInvalidMetalCreditsPatternMissingNumber() {
		cc.createMetalValueMapping("glob glob Silver is some Credits".split("\\s"));
		assertEquals(false, cc.getMetalValue().containsKey("Silver"));
	}

	@Test
	public void testMetalCreditsPatternMissingIsToken() {
		cc.createMetalValueMapping("glob glob Silver 34 Credits".split("\\s"));
		assertEquals(false, cc.getMetalValue().containsKey("Silver"));
	}

	@Test
	public void testCreditsQueryForUnknownMetal() {
		cc.createCurrencyMapping("glob is I".split("\\s"));
		cc.createCurrencyMapping("prok is V".split("\\s"));
		cc.createMetalValueMapping("glob glob Silver is 34".split("\\s"));
		assertEquals(0, cc.calculateCredits("glob prok Titanium".split("\\s")).intValue());
	}

	@Test
	public void endToendTest() throws UnsupportedEncodingException, IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("glob is I").append(System.lineSeparator());
		sb.append("prok is V").append(System.lineSeparator());
		sb.append("pish is X").append(System.lineSeparator());
		sb.append("tegj is L").append(System.lineSeparator());
		sb.append("glob glob Silver is 34 Credits").append(System.lineSeparator());
		sb.append("glob prok Gold is 57800 Credits").append(System.lineSeparator());
		sb.append("pish pish Iron is 3910 Credits").append(System.lineSeparator());
		sb.append("how much is pish tegj glob glob ?").append(System.lineSeparator());
		sb.append("how many Credits is glob prok Silver ?").append(System.lineSeparator());
		sb.append("how many Credits is glob prok Gold ?").append(System.lineSeparator());
		sb.append("how many Credits is glob prok Iron ?").append(System.lineSeparator());
		sb.append("how much wood could a woodchuck chuck if a woodchuck could chuck wood ?");
		CurrencyManager cm = new CurrencyManager(cc, "D:\\files\\output.txt");
		cm.acceptUserInput(sb.toString());
	}
}
