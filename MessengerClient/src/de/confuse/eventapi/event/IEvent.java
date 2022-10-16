package de.confuse.eventapi.event;

public interface IEvent
{
	/**
	 * Defaulted to null; Can be changed if you want to a normal string for specific
	 * tags or the likes.<br>
	 * When I implemented this I thought something along the lines of a optional
	 * parameter when creating your event to change the tag when needed but that's
	 * up to you to decide on how/if to use this feature :).
	 * 
	 * @return The tag of this {@link IEvent} or <code>null</code>
	 */
	String getTag();

}
