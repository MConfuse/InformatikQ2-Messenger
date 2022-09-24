package de.confuse.confFileV2;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ConfFileFieldV2
{
	/**
	 * The name of this field. Used to retrieve it from the
	 * {@link ConfFileReaderV2}.
	 */
	private final String name;
	/**
	 * If the Field is written in one line instead of the usual "one value per
	 * line". If this value is true when retrieving the formatted field using
	 * {@link #getFormattedFieldV2(int)}, usually called by the
	 * {@link ConfFileWriterV2}, the stored {@link #fields} are not written, as that
	 * is not supported by the ConfFile system.
	 */
	private boolean inline;
	/**
	 * Contains all {@link ConfFileValueV2}s that are currently available.
	 */
	private final List<ConfFileValueV2> values;
	/**
	 * Contains all {@link ConfFileFieldV2}s that are currently added.
	 */
	private final List<ConfFileFieldV2> fields;

	/**
	 * The {@link ConfFileFieldV2} is one of the base elements of the whole
	 * ConfF-System as it contains a {@link List} of {@link ConfFileValueV2}s which
	 * store the actual data from the {@link File} read by the
	 * {@link ConfFileReaderV2}.<br>
	 * This class and, in this case, this Object is a bridge between the raw data
	 * read from the file and the actual data as the {@link ConfFileReaderV2} uses
	 * it to split the raw data into usable chunks of data which can the be accessed
	 * using the access methods like {@link #getValue(String)} for direct access or
	 * just using {@link #getValues()} and iterating over all of the
	 * {@link #values}.<br>
	 * <br>
	 * The {@link #inline} boolean is set by the {@link ConfFileReaderV2} according
	 * to the state the {@link ConfFileFieldV2} was read from the field. You can
	 * still change the boolean afterwards though.
	 *
	 * @param name   The name of this Field
	 * @param inline Indicates if this field should be written in a single or
	 *               multiple lines. Can be changed after the instantiation.
	 * @param values A nullable {@link List} of {@link ConfFileValueV2}s
	 * @param fields A nullable {@link List} of {@link ConfFileFieldV2}s
	 */
	public ConfFileFieldV2(final String name, boolean inline, List<ConfFileValueV2> values,
						   List<ConfFileFieldV2> fields)
	{
		// - Name -
		if (name.equals("{"))
			this.name = "";
		else
			this.name = name;

		this.inline = inline;

		// - Values -
		if (values != null)
			this.values = values;
		else
			this.values = new ArrayList<ConfFileValueV2>();

		// - Fields -
		if (fields != null)
			this.fields = fields;
		else
			this.fields = new ArrayList<ConfFileFieldV2>();
	}

	/**
	 * Instantiates a new {@link ConfFileFieldV2}. This constructor is the short
	 * form of {@link ConfFileFieldV2#ConfFileFieldV2(String, boolean, List, List)} and fills the List parameter
	 * with null.<br>
	 * <br>
	 * The {@link ConfFileFieldV2} is one of the base elements of the whole
	 * ConfF-System as it contains a {@link List} of {@link ConfFileValueV2}s which
	 * store the actual data from the {@link File} read by the
	 * {@link ConfFileReaderV2}.<br>
	 * This class and, in this case, this Object is a bridge between the raw data
	 * read from the file and the actual data as the {@link ConfFileReaderV2} uses
	 * it to split the raw data into usable chunks of data which can the be accessed
	 * using the access methods like {@link #getValue(String)} for direct access or
	 * just using {@link #getValues()} and iterating over all of the
	 * {@link #values}.<br>
	 * <br>
	 * The {@link #inline} boolean is set by the {@link ConfFileReaderV2} according
	 * to the state the {@link ConfFileFieldV2} was read from the field. You can
	 * still change the boolean afterwards though.
	 *
	 * @param name   The name of this Field
	 * @param inline Indicates if this field should be written in a single or
	 *               multiple lines. Can be changed after the instantiation.
	 * @see #ConfFileFieldV2(String, boolean, List, List)
	 */
	public ConfFileFieldV2(final String name, boolean inline)
	{
		this(name, inline, null, null);
	}

	/**
	 * Loops through all {@link ConfFileValueV2}s currently in the {@link #values}
	 * list and retrieves the first {@link ConfFileValueV2}, if there is one, that
	 * matches given key (<strong>case sensitive!</strong>).
	 *
	 * @param key The key (name) to search for.
	 * @return The first matching {@link ConfFileValueV2} or null
	 */
	public ConfFileValueV2 getValueObject(String key)
	{
		for (ConfFileValueV2 val : this.values)
			if (val.getKey().equals(key))
				return val;

		return null;
	}

	/**
	 * Loops through all {@link ConfFileValueV2}s currently in the {@link #values}
	 * list and retrieves the first {@link ConfFileValueV2}, if there is one, that
	 * matches given key (<strong>case sensitive!</strong>).
	 *
	 * @param key The key (name) to search for.
	 * @return The first matching {@link ConfFileValueV2}s value or null
	 * @see ConfFileValueV2#getValue()
	 * @see #getValues(String)
	 */
	public String getValue(String key)
	{
		for (ConfFileValueV2 val : this.values)
			if (val.getKey().equals(key))
				return val.getValue();

		return null;
	}

	/**
	 * Loops through all {@link ConfFileValueV2}s currently in the {@link #values}
	 * list and retrieves the first {@link ConfFileValueV2}, if there is one, that
	 * matches given key (<strong>case sensitive!</strong>).
	 *
	 * @param key The key (name) to search for.
	 * @return The first matching {@link ConfFileValueV2}s values or null
	 * @see ConfFileValueV2#getValues()
	 * @see #getValue(String)
	 */
	public String[] getValues(String key)
	{
		for (ConfFileValueV2 val : this.values)
			if (val.getKey().equals(key))
				return val.getValues();

		return null;
	}

	public ConfFileFieldV2 getField(String name)
	{
		for (ConfFileFieldV2 field : this.fields)
			if (field.getName().equals(name))
				return field;

		return null;
	}

	/**
	 * Adds the given {@link ConfFileValueV2} to the {@link #values} list.
	 *
	 * @param val The {@link ConfFileValueV2} to add.
	 * @return This objects instance for chaining.
	 * @see #put(String, CharSequence, String...)
	 * @see #put(String, String...)
	 */
	public ConfFileFieldV2 put(ConfFileValueV2 val)
	{
		this.values.add(val);
		return this;
	}

	/**
	 * Creates and adds a {@link ConfFileValueV2} with the given values to the
	 * {@link #values} list.
	 *
	 * @param key    The key (name) to search for.
	 * @param values The Values to add to the {@link ConfFileValueV2} field.
	 * @return This objects instance for chaining.
	 * @see #put(ConfFileValueV2)
	 * @see #put(String, CharSequence, String...)
	 */
	public ConfFileFieldV2 put(String key, String... values)
	{
		return put(new ConfFileValueV2(key, null, values));
	}

	/**
	 * Creates and adds a {@link ConfFileValueV2} with the given values to the
	 * {@link #values} list.
	 *
	 * @param key     The key (name) to search for.
	 * @param comment The comment that should be attached to the end of the line.
	 * @param values  The Values to add to the {@link ConfFileValueV2} field.
	 * @return This objects instance for chaining.
	 * @see #put(ConfFileValueV2)
	 * @see #put(String, String...)
	 */
	public ConfFileFieldV2 put(String key, CharSequence comment, String... values)
	{
		return put(new ConfFileValueV2(key, String.valueOf(comment), values));
	}

	/**
	 * FIXME: Test this all out
	 *
	 * @param field
	 * @return This objects instance for chaining.
	 */
	public ConfFileFieldV2 put(final ConfFileFieldV2 field)
	{
		this.fields.add(field);
		return this;
	}

	/**
	 * TODO: Add inline tab insertions
	 * <p>
	 * Returns the fully formatted field. This can either be the normal multi line
	 * or a one line layout.<br>
	 * The layout is specified by the {@link #inline} boolean, see that description
	 * for a more detailed behavior.
	 *
	 * @param layer Specifies the indent size in tab spaces. (The name will always
	 *              have layer - 1 tabs).
	 * @return The formatted {@link ConfFileFieldV2} that the
	 * {@link ConfFileReaderV2} can read.
	 * @see #inline
	 */
	public String getFormattedFieldV2(int layer)
	{
		if (this.inline)
			return inlineFormattedField();
		else
			return normallyFormattedField(layer);
	}

	private String normallyFormattedField(int layer)
	{
		StringBuilder builder = new StringBuilder();
		String tab = "";
		for (int i = 0; i < layer; i++)
			tab += "\t";

		/*
		 * Checks if this is the first layer for special formatting to save a bit of
		 * space.
		 */
		if (layer != 0)
			builder.append("\r" + tab + "Field: " + name + "\r" + tab + "{\r");
		else
			builder.append(tab + "Field: " + name + "\r" + tab + "{\r");

		// - Adds one tab to indent the following content -
		tab += "\t";

		// - Adds all of the fields values -
		for (int i = 0; i < this.values.size(); i++)
			if (i != this.values.size() - 1)
				builder.append(tab + this.values.get(i).getFormattedValue() + "\r");
			else
				builder.append(tab + this.values.get(i).getFormattedValue());

		// -- Calls the other fields and appends their stuff to this builder --
		for (ConfFileFieldV2 field : this.fields)
			builder.append(field.getFormattedFieldV2(layer + 1));

		// - Removes one tab to close the bracket -
		tab = tab.substring(0, tab.length() - 1);
		/*
		 * Checks if this is the first layer for space saving formatting.
		 */
		if (layer != 0)
			builder.append(tab + "\r" + tab + "}\r");
		else
			builder.append("\r" + tab + "}");
		return builder.toString();
	}

	private String inlineFormattedField()
	{
		StringBuilder builder = new StringBuilder("Field: " + this.name + " {");

		// - Adds all of the fields values -
		for (int i = 0; i < this.values.size(); i++)
			if (i != this.values.size() - 1)
				builder.append("(" + this.values.get(i).getFormattedValue() + "), ");
			else
				builder.append("(" + this.values.get(i).getFormattedValue() + ")");

		builder.append("}");
		return builder.toString();
	}

	/**
	 * @return the {@link ConfFileFieldV2}s name
	 */
	public String getName()
	{return this.name;}

	/**
	 * @return true if this field should be written in the inline mode
	 */
	public boolean isInline()
	{return inline;}

	/**
	 * @param inline set {@link #inline} state
	 */
	public void setInline(boolean inline)
	{this.inline = inline;}

	/**
	 * @return true if the {@link #inline} field is false, true if otherwise.
	 * @see #inline
	 */
	public boolean isSupportingFields()
	{return !inline;}

	/**
	 * @return all of the values
	 */
	public List<ConfFileValueV2> getValues()
	{return values;}

	/**
	 * @return all of the fields
	 */
	public List<ConfFileFieldV2> getFields()
	{return fields;}

}
