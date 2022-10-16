package de.confuse.eventapi.event.events;

import de.confuse.eventapi.event.IEvent;

/**
 * A simplistic implementation of only the {@link IEvent} interface.<br>
 * Unlike the other predefined events this one is not abstract and usually used
 * for testing purposes.
 * 
 * @author Confuse / Confuse#5117
 * @see IEvent
 */
public class EventBasic implements IEvent
{
	private final String tag;

	public EventBasic()
	{
		this(null);
	}

	public EventBasic(String tag)
	{
		this.tag = tag;
	}

	@Override
	public String getTag()
	{ return tag; }

}
