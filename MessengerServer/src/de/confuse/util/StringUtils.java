package de.confuse.util;

import java.util.ArrayList;
import java.util.List;

import static de.confuse.util.ArrayUtils.addElementToArray;
import static de.confuse.util.ArrayUtils.containsConsecutiveValues;

public class StringUtils
{
	/**
	 * Finds the first occurrence of the given <code>val</code> character, if the
	 * character before <code>val</code> is not the <code>ignore</code>
	 * character.<br>
	 * The <code>offset</code> specifies the returned index of the occurrence, for
	 * example:<br>
	 * The given String str is: "\"dark\" cookies"<br>
	 * <code>(A copy paste version: "\"\\\"dark\\\" cookies\"")</code><br>
	 * By inputting the values
	 * <b><code>findFirstMatchingPosition(str, '\\', '"', 0)</code></b> the returned
	 * index
	 * will be 1.<br>
	 * If you change the offset from <code>0</code> to <code>1</code> the
	 * method will return 18, as it is ignoring both the first <code>"</code> and
	 * the ones with the <code>\</code> in front of them.
	 * 
	 * @param input The string to search in
	 * @param ignore The character to ignore
	 * @param val The value to search for
	 * @param offset Which matching position will be returned
	 * @return Returns the first matching index of the given values.
	 */
	public static int findFirstMatchingPosition(String input, char ignore, char val, int offset)
	{
		char[] in = input.toCharArray();
		char[] lastChars = new char[] {in[0]};
		int total = 0;

		for (char c : in)
			if (lastChars.length > 0 && lastChars[lastChars.length - 1] != ignore && c == val)
			{
				if (total == offset)
					return lastChars.length;

				lastChars = addElementToArray(lastChars, c);
				total++;
			}
			else
				lastChars = addElementToArray(lastChars, c);

		if (total > 0 && offset > 0)
			return lastChars.length;
		else
			return -1;
	}

	/**
	 * Exact the same method as
	 * {@link #findFirstMatchingPosition(String, char, char, int)}, just that the
	 * offset is set to 0.
	 * 
	 * @param input  The string to search in
	 * @param ignore The character to ignore
	 * @param val    The value to search for
	 * @return Returns the first matching index of the given values.
	 * @see StringUtils#findFirstMatchingPosition(String, char, char, int)
	 */
	public static int findFirstMatchingPosition(String input, char ignore, char val)
	{
		return findFirstMatchingPosition(input, ignore, val, 0);
	}

	public static int amountOfMatches(String input, String value)
	{
		List<Character> lastChars = new ArrayList<Character>();
		char[] values = value.toCharArray();
		int total = 0;

		for (char c : input.toCharArray())
			if (containsConsecutiveValues(lastChars, values))
			{
				lastChars.clear();
				total++;
			}
			else
				lastChars.add(c);

		return total;
	}

	public static int amountOfMatches(String input, char ignore, char val)
	{
		char[] in = input.toCharArray();
		char[] lastChars = new char[] {in[0]};
		int total = 0;

		for (char c : in)
			if (lastChars.length > 0 && lastChars[lastChars.length - 1] != ignore && c == val)
			{
				lastChars = addElementToArray(lastChars, c);
				total++;
			}
			else
				lastChars = addElementToArray(lastChars, c);

		return total;
	}

}
