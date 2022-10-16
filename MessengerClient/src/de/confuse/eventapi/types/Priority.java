package de.confuse.eventapi.types;

/**
 * Due to memory consumption the values are stored in bytes instead of an
 * enumeration, so they only consume 8bits instead of 32 bytes.
 *
 * @author Confuse / Confuse#5117
 */
public class Priority
{
	/**
	 * Highest priority, called first.
	 */
	public static final byte
			HIGHEST = 0,
	/**
	 * High priority, called after the highest priority.
	 */
	HIGH = 1,
	/**
	 * Medium priority, called after the high priority.
	 */
	MEDIUM = 2,
	/**
	 * Low priority, called after the medium priority.
	 */
	LOW = 3,
	/**
	 * Lowest priority, called after all the other priorities.
	 */
	LOWEST = 4;

	/**
	 * Array containing all the priority values.
	 * TODO: Rename
	 */
	public static final byte[] VALUE_ARRAY;

	/*
	 * Sets up the VALUE_ARRAY the first time anything in this class is called.
	 */
	static
	{
		VALUE_ARRAY = new byte[]
				{HIGHEST, HIGH, MEDIUM, LOW, LOWEST};
	}

}
