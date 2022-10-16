package de.confuse.eventapi;

import de.confuse.eventapi.types.Priority;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;
import java.util.List;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(METHOD)
public @interface EventTarget
{
	/**
	 * The priority of the Event<br>
	 * <br>
	 * <strong>Note:</strong><br>
	 * Due to using a {@link List} for the priority, the Priorities will still be
	 * called one after another!
	 * 
	 * @return the current {@link Priority}
	 * 
	 * @see Priority
	 */
	byte priority() default Priority.MEDIUM;
}
