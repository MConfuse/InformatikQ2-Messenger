package de.confuse.eventapi.event;

import de.confuse.eventapi.types.EventType;

public interface ITyped
{
	/**
	 * @return the current type ID of the event.
	 * @see EventType
	 */
	byte getType();

	/**
	 * Sets the current type ID of the event.
	 * 
	 * @param type The byte value to set.
	 */
	void setType(byte type);

}
