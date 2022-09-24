package de.confuse.confFileV2;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConfFileWriterV2
{
	/** The Version of the ConfFileWriter */
	public static final String VERSION = "ConfFileVersion: 2.0 | Created by Confuse/Confuse#5117 | !Removing this might break stuff! | Note: At some point this formatting might not be supported anymore by the File reader";

	/** The target file which the System will use to save the data in */
	private File file;
	/** A list containing all fields */
	private List<ConfFileFieldV2> fields = new ArrayList<ConfFileFieldV2>();
	/** Whether or not .CONFF should be added to the end of the saved File */
	private final boolean fileType;

	/**
	 * Creates a new {@link ConfFileWriterV2} object that is needed to write a file
	 * in
	 * the .CONFF file format.
	 * 
	 * @param file        The file where the ConfFile should be written to
	 * @param addFileType Whether or not .CONFF should be added at the end of the
	 *                    saved file
	 */
	public ConfFileWriterV2(File file, final boolean addFileType)
	{
		this.file = file;
		this.fileType = addFileType;
	}

	/**
	 * Creates a new {@link ConfFileWriterV2} object that is needed to write a file
	 * in
	 * the .CONFF file format. <br>
	 * This Constructor will automatically set the {@link #fileType} boolean to
	 * false.
	 * 
	 * @param file The file where the ConfFile should be written to
	 */
	public ConfFileWriterV2(File file)
	{
		this(file, false);
	}

	/**
	 * Method to write all added {@link ConfFileFieldV2}s to the {@link File} that
	 * was
	 * specified in the constructor ({@link #ConfFileWriterV2(File)}).
	 * 
	 * @throws IOException This will only be thrown when there was an error
	 *                     creating/writing to the file specified in the constructor
	 */
	public void writeFile() throws IOException
	{
		PrintWriter writer = new PrintWriter(new FileWriter(file.getAbsolutePath() + (fileType ? ".conff" : "")));
		writer.println(VERSION + "\r#");
		
		for (ConfFileFieldV2 field : fields)
		{
			writer.println(field.getFormattedFieldV2(0));

			// name + new line + brace
//			writer.println(field.getName() + "\r{");
//
//			for (ConfFileValueV2 value : field.getValues())
//			{
//				writer.println(indent + value.getFormattedValue());
//			}
//
//			writer.println("}");
		}

		writer.close();
	}

	/**
	 * Used to add {@link ConfFileFieldV2}s to this {@link ConfFileWriterV2}s
	 * instance.
	 * Adding {@link ConfFileFieldV2}s is necessary to write the data to the file
	 * later on, using the {@link #writeFile()} method.
	 * 
	 * @param fields All {@link ConfFileFieldV2}s that should be added
	 */
	public void addFields(ConfFileFieldV2... fields)
	{
		this.fields.addAll(Arrays.asList(fields));
	}
	
	/**
	 * Used to add {@link ConfFileFieldV2}s to this {@link ConfFileWriterV2}s
	 * instance.
	 * Adding {@link ConfFileFieldV2}s is necessary to write the data to the file
	 * later on, using the {@link #writeFile()} method.
	 * 
	 * @param fields All {@link ConfFileFieldV2}s that should be added
	 */
	public void addFields(List<ConfFileFieldV2> fields)
	{
		this.fields.addAll(fields);
	}

	/**
	 * Used to add a {@link ConfFileFieldV2} to this {@link ConfFileWriterV2}s
	 * instance.
	 * Adding {@link ConfFileFieldV2}s is necessary to write the data to the file
	 * later on, using the {@link #writeFile()} method.
	 * 
	 * @param field The {@link ConfFileFieldV2} that should be added
	 */
	public void addField(ConfFileFieldV2 field)
	{
		this.fields.add(field);
	}

}
