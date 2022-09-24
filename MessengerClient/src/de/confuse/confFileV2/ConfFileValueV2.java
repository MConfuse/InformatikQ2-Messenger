package de.confuse.confFileV2;

import java.util.Arrays;

public class ConfFileValueV2
{
	/**
	 * Contains all of the characters where the system inserts/replaces the \ before
	 * the character.
	 */
	public static final String[] SPECIAL_CHARACTERS =
	{ "{", "}", "[", "]", "(", ")", "\"", "'" };
//	public static final char[] SPECIAL_CHARACTERS =
//		{ '{', '}', '[', ']', '"' };

	/** The "name" of the value. Used to retrieve it from a field. */
	private final String key;
	/** All values that this instance stores. */
	private final String[] values;
	/**
	 * If the value had a comment attached to it, this will contain it if not, this
	 * will be an empty string.
	 */
	private final String comment;

	/**
	 * Creates a new instance, using the given key and value/-s.<br>
	 * <br>
	 * <strong>NOTE:</strong><br>
	 * Once created the values cannot be changed anymore, so make sure to put in the
	 * final values into the constructor!
	 * 
	 * @param key     The key of this Value, used to retrieve it back out from a
	 *                file.
	 * @param comment The comment attached to this value.
	 * @param values  The actual values of this {@link ConfFileValueV2}. If a not
	 *                initialized array is parsed as parameter it will be changed to
	 *                an array with a single value, that being '<code>null</code>'.
	 */
	public ConfFileValueV2(final String key, final String comment, final String... values)
	{
		this.key = key;
		if (values == null)
			this.values = new String[]
			{ "null" };
		else
			this.values = values;
		this.comment = comment;
	}

	@Override
	/**
	 * Used for debugging purposes so you can easily list all Values of a field into
	 * one string.
	 */
	public String toString()
	{
		return this.key + ": " + Arrays.toString(this.values) + " | " + this.comment;
	}

	/**
	 * @return the key
	 */
	public String getKey()
	{ return this.key; }

	/**
	 * @return the whole {@link #values} array. If {@link #isArrayValue()} returns
	 *         false, you can just use the {@link #getValue()} method to simplify
	 *         the process.
	 * @see #replaceEscapeMarkers(String)
	 */
	public String[] getValues()
	{ return this.values; }

	/**
	 * @return the first value of the {@link #values} array.
	 * @see #replaceEscapeMarkers(String)
	 */
	public String getValue()
	{ return this.values[0]; }

	/**
	 * TODO: Insert link to writer
	 * 
	 * @return a fully formatted value field that the {} uses to write data to the
	 *         file.<br>
	 *         I honestly don't know why anyone would need this but here it is,
	 *         public and functional for you to use :).
	 */
	public String getFormattedValue()
	{
//		System.out.println(key + ": " + isArrayValue());
		if (!isArrayValue())
			return this.key + ": \"" + (insertEscapeMarkers(getValue())) + "\"" + (hasComment() ? " " + this.comment : "");

		StringBuilder builder = new StringBuilder();
		for (int i = 0; i < this.values.length; i++)
			builder.append("\"" + (insertEscapeMarkers(this.values[i])) + "\", ");

		return this.key + " [" + builder.toString().substring(0, builder.toString().length() - 2) + "]"
				+ (hasComment() ? this.comment : "");
	}

	/**
	 * If true this {@link ConfFileValueV2} has more than 1 value and needs to be
	 * stored using the Array format.<br>
	 * If you are reading data from this field, this means the {@link #getValue()}
	 * method won't return all of the results and you have to use the
	 * {@link #getValues()} method to retrieve the other values of this
	 * {@link ConfFileValueV2}.
	 * 
	 * @return true if this {@link ConfFileValueV2} has more than 1 value assigned
	 *         to it.
	 */
	public boolean isArrayValue()
	{ return this.values.length > 1; }

	public boolean hasComment()
	{
		return this.comment != null && !this.comment.equals("null");
	}

	/**
	 * @return the comment. Note: Retains the # in front of the comment, if that is
	 *         a problem you will have to remove it manually.
	 */
	public String getComment()
	{ return this.comment; }

	public static String replaceEscapeMarkers(String in)
	{
		String out = in == null ? "" : in;
		for (String c : SPECIAL_CHARACTERS)
			out = out.replace("\\" + String.valueOf(c), String.valueOf(c));

		return out;
	}

	public static String insertEscapeMarkers(String in)
	{
		String out = in == null ? "" : in;
		for (String c : SPECIAL_CHARACTERS)
			out = out.replace(String.valueOf(c), "\\" + String.valueOf(c));

		return out;
	}

}
