package de.confuse.eventapi;

import de.confuse.eventapi.event.IEvent;
import de.confuse.eventapi.event.events.EventBasic;
import de.confuse.eventapi.types.Priority;

import java.io.Serial;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

public class EventManager
{
	/**
	 * Contains all the currently registered classes with their respective
	 * methods.<br>
	 * Key is the type of class extending the Event interface, for example
	 * {@link EventBasic} or your custom Event.<br>
	 * Value of the map is a {@link List} of {@link MethodData} containing all
	 * methods that have that specific {@link IEvent} as parameter.
	 */
	private static final Map<Class<? extends IEvent>, List<MethodData>> REGISTRY_MAP = new HashMap<>();

	/**
	 * Static methods only, no need to instantiate
	 */
	private EventManager()
	{}

	public static void register(Object obj)
	{
		for (final Method m : obj.getClass().getDeclaredMethods())
		{
			if (isMethodBad(m))
				continue;

			register(m, obj);
		}

	}

	public static void registerAll(Object obj)
	{
		for (final Method m : obj.getClass().getMethods())
		{
			if (isMethodBad(m))
				continue;

			register(m, obj);
		}

	}

	private static void register(Method method, Object obj)
	{
		@SuppressWarnings("unchecked")
		final Class<? extends IEvent> indexClass = (Class<? extends IEvent>) method.getParameterTypes()[0];
		// New MethodData from the Method we are registering.
		final MethodData data = new MethodData(obj, method, method.getAnnotation(EventTarget.class).priority());

		/*
		 * Set's the method to accessible so that we can also invoke it if it's
		 * protected or private.
		 */
		if (!data.getMethod().isAccessible())
			data.getMethod().setAccessible(true);

		if (REGISTRY_MAP.containsKey(indexClass))
		{
			if (!REGISTRY_MAP.get(indexClass).contains(data))
			{
				REGISTRY_MAP.get(indexClass).add(data);
				sortListValue(indexClass);
			}

		}
		else
		{
			REGISTRY_MAP.put(indexClass, new CopyOnWriteArrayList<>()
			{
				@Serial
				private static final long serialVersionUID = 42069L;
				{
					add(data);
				}
			});
		}

	}

	/**
	 * Unregisters all the methods inside the Object that are marked with the
	 * EventTarget annotation.
	 *
	 * @param object
	 *               Object of which you want to unregister all Methods.
	 */
	public static void unregister(Object object)
	{
		for (final List<MethodData> dataList : REGISTRY_MAP.values())
			dataList.removeIf(data -> data.getObject().equals(object));

		cleanMap(true);
	}

	public static IEvent call(final IEvent event)
	{
		List<MethodData> dataList = REGISTRY_MAP.get(event.getClass());

		if (dataList != null)
		{
			for (final MethodData data : dataList)
				invoke(data, event);
		}

		return event;
	}

	private static void invoke(MethodData data, IEvent event)
	{
		try
		{
			data.getMethod().invoke(data.getObject(), event);
		}
		catch (IllegalAccessException e)
		{
			e.printStackTrace();
		}
		catch (IllegalArgumentException e)
		{
			e.printStackTrace();
		}
		catch (InvocationTargetException e)
		{
			e.printStackTrace();
		}

	}

	/**
	 * Sorts the List that matches the corresponding Event class based on priority
	 * value.
	 *
	 * @param indexClass The Event class index in the HashMap of the List to sort.
	 */
	private static void sortListValue(Class<? extends IEvent> indexClass)
	{
		List<MethodData> sortedList = new CopyOnWriteArrayList<>();

		for (final byte priority : Priority.VALUE_ARRAY)
			for (final MethodData data : REGISTRY_MAP.get(indexClass))
				if (data.getPriority() == priority)
					sortedList.add(data);

		// Overwriting the existing entry.
		REGISTRY_MAP.put(indexClass, sortedList);
	}

	/**
	 * Cleans up the map entries.
	 * Uses an iterator to make sure that the entry is completely removed.
	 *
	 * @param onlyEmptyEntries
	 *                         If true only remove the entries with an empty list,
	 *                         otherwise remove all the entries.
	 */
	public static void cleanMap(boolean onlyEmptyEntries)
	{
		Iterator<Map.Entry<Class<? extends IEvent>, List<MethodData>>> mapIterator = REGISTRY_MAP.entrySet().iterator();

		while (mapIterator.hasNext())
			if (!onlyEmptyEntries || mapIterator.next().getValue().isEmpty())
				mapIterator.remove();
	}

	/**
	 * Checks if the method does not meet the requirements to be used to receive
	 * event calls from the Dispatcher.
	 * Performed checks: Checks if the parameter length is not 1 and if the
	 * EventTarget annotation is not present.
	 *
	 * @param method
	 *               Method to check.
	 *
	 * @return True if the method should not be used for receiving event calls from
	 *         the Dispatcher.
	 */
	private static boolean isMethodBad(Method method)
	{
		return method.getParameterTypes().length != 1 || !method.isAnnotationPresent(EventTarget.class);
	}

	/**
	 * Checks if the method does not meet the requirements to be used to receive
	 * event calls from the Dispatcher.
	 * Performed checks: Checks if the parameter class of the method is the same as
	 * the event we want to receive.
	 *
	 * @param method Method to check.
	 * @param eventClass of the Event we want to find a method for receiving it.
	 *
	 * @return True if the method should not be used for receiving event calls from
	 *         the Dispatcher.
	 */
	private static boolean isMethodBad(Method method, Class<? extends IEvent> eventClass)
	{
		return isMethodBad(method) || !method.getParameterTypes()[0].equals(eventClass);
	}

	private final static class MethodData
	{
		private final Object object;
		private final Method method;
		private final byte priority;

		/**
		 * Creates a new MethodData object which stores information about a specific
		 * method of a specific object. All the Methods added here have to be annotated
		 * with the {@link EventTarget} annotation.
		 * 
		 * @param object   The objects instance from which to invoke the method
		 * @param method   The method to invoke on the specified objects instance
		 * @param priority The priority of the methods invocation
		 * 
		 * @see EventTarget
		 */
		public MethodData(Object object, Method method, byte priority)
		{
			this.object = object;
			this.method = method;
			this.priority = priority;
		}

		/**
		 * @return the object
		 */
		public Object getObject()
		{ return object; }

		/**
		 * @return the method
		 */
		public Method getMethod()
		{ return method; }

		/**
		 * @return the priority
		 */
		public byte getPriority()
		{ return priority; }

	}

}
