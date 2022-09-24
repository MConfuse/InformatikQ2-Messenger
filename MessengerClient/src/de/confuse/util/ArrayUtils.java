package de.confuse.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * A Utility class for Array management and other List/Array related things that
 * are just common enough to be annoying, for example the Arrays.print() method
 * is fully integrated with this class so you don't have to import/reference the
 * Arrays class for some simple debugging :).<br>
 * <br>
 * This Class includes a lot of more or less useful methods each with multiple
 * data types, if something is missing, please get in touch with me so I can add
 * it to this class!
 * 
 * @author Confuse/Confuse#5117
 * @version 6
 */
public class ArrayUtils
{
	/**
	 * Adds the given Element to the given Array. The element will be appended to
	 * the end of the input Array.
	 * 
	 * @param arr     The Array which will be extended
	 * @param element The Element which will be added to the end of the input Array.
	 * @return A independent String Array that has the exact order of the input
	 *         Array with the new Element appended to the end of the original Array.
	 */
	public static Object[] addElementToArray(Object[] arr, Object element)
	{
		Object[] newArr = new Object[arr.length + 1];
		for (int i = 0; i < arr.length; i++)
			newArr[i] = arr[i];
		newArr[arr.length] = element;

		return newArr;
	}

	/**
	 * Adds the given Element to the given Array. The element will be appended to
	 * the end of the input Array.
	 * 
	 * @param arr     The Array which will be extended
	 * @param element The Element which will be added to the end of the input Array.
	 * @return A independent String Array that has the exact order of the input
	 *         Array with the new Element appended to the end of the original Array.
	 */
	public static String[] addElementToArray(String[] arr, String element)
	{
		String[] newArr = new String[arr.length + 1];
		for (int i = 0; i < arr.length; i++)
			newArr[i] = arr[i];
		newArr[arr.length] = element;

		return newArr;
	}

	/**
	 * Adds the given Element to the given Array. The element will be appended to
	 * the end of the input Array.
	 * 
	 * @param arr     The Array which will be extended
	 * @param element The Element which will be added to the end of the input Array.
	 * @return A independent String Array that has the exact order of the input
	 *         Array with the new Element appended to the end of the original Array.
	 */
	public static char[] addElementToArray(char[] arr, char element)
	{
		char[] newArr = new char[arr.length + 1];
		for (int i = 0; i < arr.length; i++)
			newArr[i] = arr[i];
		newArr[arr.length] = element;

		return newArr;
	}

	/**
	 * Casts the given Array from one type of another.<br>
	 * <br>
	 * <strong>NOTE:</strong><br>
	 * The casting is unchecked and may fail at any time!
	 * 
	 * @param <T> The type of array to cast to
	 * @param <A> The type of array to cast from
	 * @param target The target array
	 * @param array The array to cast
	 * @return the casted array
	 */
	@SuppressWarnings("unchecked")
	public static <T, A> T[] castArray(T[] target, A[] array)
	{
		for (int i = 0; i < array.length; i++)
			target[i] = (T) array[i];

		return target;
	}

	/**
	 * Will check the given Array for the input Object.<br>
	 * This Method can be used to check for <code>null</code> in the given Array and
	 * (should) be completely null-safe.
	 * 
	 * @param arr The Array to check
	 * @param val The Value to search for
	 * @return true if the Object was found, false if otherwise
	 */
	public static boolean containsValue(Object[] arr, Object val)
	{
		for (Object str : arr)
			if (val == null && str == null)
				return true;
			else if (str == null)
				;
			else if (str.equals(val))
				return true;

		return false;
	}

	/**
	 * Will check the given Array for the input Boolean.
	 * 
	 * @param arr The Array to check
	 * @param val The Value to search for
	 * @return true if the value was found, false if otherwise
	 */
	public static boolean containsValue(boolean[] arr, boolean val)
	{
		for (boolean str : arr)
			if (str == val)
				return true;

		return false;
	}

	/**
	 * Will check the given Array for the input Integer.
	 * 
	 * @param arr The Array to check
	 * @param val The Value to search for
	 * @return true if the value was found, false if otherwise
	 */
	public static boolean containsValue(int[] arr, int val)
	{
		for (int i : arr)
			if (i == val)
				return true;

		return false;
	}

	public static boolean containsValue(long[] arr, long val)
	{
		for (long i : arr)
			if (i == val)
				return true;

		return false;
	}

	public static boolean containsValue(float[] arr, float val)
	{
		for (float i : arr)
			if (i == val)
				return true;

		return false;
	}

	public static boolean containsValue(double[] arr, double val)
	{
		for (double i : arr)
			if (i == val)
				return true;

		return false;
	}

	public static boolean containsValue(char[] arr, char val)
	{
		for (char i : arr)
			if (i == val)
				return true;

		return false;
	}

	public static boolean containsValues(Object[] arr, Object... vals)
	{
		int total = 0;
		for (Object obj : arr)
			if (containsValue(vals, obj))
				total++;

		return total == vals.length ? true : false;
	}

	public static boolean containsValues(int[] arr, int... vals)
	{
		int total = 0;
		for (int i : arr)
			if (containsValue(vals, i))
				total++;

		return total == vals.length ? true : false;
	}

	public static boolean containsValues(long[] arr, long... vals)
	{
		int total = 0;
		for (long i : arr)
			if (containsValue(vals, i))
				total++;

		return total == vals.length ? true : false;
	}

	public static boolean containsValues(float[] arr, float... vals)
	{
		int total = 0;
		for (float i : arr)
			if (containsValue(vals, i))
				total++;

		return total == vals.length ? true : false;
	}

	public static boolean containsValues(double[] arr, double... vals)
	{
		int total = 0;
		for (double i : arr)
			if (containsValue(vals, i))
				total++;

		return total == vals.length ? true : false;
	}

	public static boolean containsValues(char[] arr, char... vals)
	{
		int total = 0;
		for (char i : arr)
			if (containsValue(vals, i))
				total++;

		return total == vals.length ? true : false;
	}

	public static boolean containsValues(List<Character> arr, char... vals)
	{
		int total = 0;
		for (char i : arr)
			if (containsValue(vals, i))
				total++;

		return total == vals.length ? true : false;
	}

	public static boolean containsValueHigherThan(int[] arr, int value)
	{
		for (int i : arr)
			if (i > value)
				return true;

		return false;
	}

	public static boolean containsValueHigherThan(long[] arr, long value)
	{
		for (long i : arr)
			if (i > value)
				return true;

		return false;
	}

	public static boolean containsValueHigherThan(float[] arr, float value)
	{
		for (float i : arr)
			if (i > value)
				return true;

		return false;
	}

	public static boolean containsValueHigherThan(double[] arr, double value)
	{
		for (double i : arr)
			if (i > value)
				return true;

		return false;
	}

	/**
	 * Will check the given Array for the input String.
	 * 
	 * @param arr The Array to check
	 * @param val The Value to search for
	 * @return true if the value (Ignoring case) was found, false if otherwise. For
	 *         case sensitive comparison you can use the
	 *         {@link #containsConsecutiveValues(Object[], Object...)} Method.
	 */
	public static boolean containsValueIgnoreCase(String[] arr, String val)
	{
		for (String str : arr)
			if (str.equalsIgnoreCase(val))
				return true;

		return false;
	}

	/**
	 * 
	 * @param arr The Array to check
	 * @param val The Value to search for
	 * @return true if all input values were found at the beginning of any space at
	 *         the Input array
	 */
	public static boolean containsValueStartingWith(String[] arr, String... val)
	{
		int total = 0;
		for (String str : arr)
			for (String st : val)
				if (str.toLowerCase().startsWith(st.toLowerCase()))
					total++;

		return total == val.length ? true : false;
	}

	/**
	 * This Method will just execute
	 * {@link #containsValueStartingWith(String[], String...)}!
	 * 
	 * @param arr The Array to check
	 * @param val The Value to search for
	 * @return true if the value (Ignoring case) was found, false if otherwise
	 * @see #containsValueStartingWith(String[], String...)
	 */
	public static boolean containsValueStartingWith(String[] arr, String val)
	{
		return containsValueStartingWith(arr, val);
	}

	/**
	 * Checks whether or not the Input Array contains the Values of the input array
	 * identically to the input Array.<br>
	 * <br>
	 * Example:<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 2, 3 -> true<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 2, 10, 1 -> true<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 10, 1 -> false
	 * 
	 * @param arr The input Array to check
	 * @param val The input Array to use as reference
	 * @return true if the full val array was consecutively found, without another
	 *         value in between.
	 */
	public static boolean containsConsecutiveValues(Object[] arr, Object... val)
	{
		int total = 0;
		for (Object obj : arr)
		{
			if (total == val.length)
				return true;
			else if (val[total] == obj || val[total].equals(obj))
				total++;
			else
				total = 0;
		}

		return total == val.length ? true : false;
	}

	/**
	 * Checks whether or not the Input Array contains the Values of the input array
	 * identically to the input Array.<br>
	 * <br>
	 * Example:<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 2, 3 -> true<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 2, 10, 1 -> true<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 10, 1 -> false
	 * 
	 * @param lis The input List to check
	 * @param val The input Array to use as reference
	 * @return true if the full val array was consecutively found, without another
	 *         value in between.
	 */
	public static boolean containsConsecutiveValues(List<Object> lis, Object... val)
	{
		int total = 0;
		for (Object obj : lis)
			if (total == val.length)
				return true;
			else if (val[total] == obj || val[total].equals(obj))
				total++;
			else
				total = 0;

		return total == val.length ? true : false;
	}

	/**
	 * Checks whether or not the Input List contains the Values of the input array
	 * identically to the input Array.<br>
	 * <br>
	 * Example:<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 2, 3 -> true<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 2, 10, 1 -> true<br>
	 * arr = 1, 2, 10, 1, 2, 3 | val = 1, 10, 1 -> false
	 * 
	 * @param lis The input List to check
	 * @param val The input Array to use as reference
	 * @return true if the full val array was consecutively found, without another
	 *         value in between.
	 */
	public static boolean containsConsecutiveValues(List<Character> lis, char... val)
	{
		int total = 0;
		for (char c : lis)
			if (total == val.length)
				return true;
			else if (val[total] == c)
				total++;
			else
				total = 0;

		return total == val.length ? true : false;
	}

	public static boolean equalsValue(Object[] arr, Object val)
	{
		if (arr.length == 1 && arr[0].equals(val))
			return true;
		else
			return false;
	}

	public static boolean equalsValue(String[] arr, String val)
	{
		if (arr.length == 1 && arr[0].equals(val))
			return true;
		else
			return false;
	}

	public static boolean equalsValue(int[] arr, int val)
	{
		if (arr.length == 1 && arr[0] == val)
			return true;
		else
			return false;
	}

	public static int indexOf(Object[] arr, Object val)
	{
		for (int i = 0; i < arr.length; i++)
			if (arr[i].equals(val))
				return i + 1;

		return -1;
	}

	public static int indexOf(int[] arr, int val)
	{
		for (int i = 0; i < arr.length; i++)
			if (arr[i] == val)
				return i + 1;

		return -1;
	}

	/**
	 * A insertion sort algorithm to sort Integer Arrays.
	 * 
	 * @param arr Array to sort
	 * @return The sorted Array
	 */
	public static int[] insertionSort(int[] arr)
	{
		int temp = 0;
		int j = 0;

		for (int i = 0; i < arr.length; i++)
		{
			temp = arr[i];
			j = i;

			while (j > 0 && arr[j - 1] > temp)
			{
				arr[j] = arr[j - 1];
				j = -1;
			}

			arr[j] = temp;
		}

		return arr;
	}

	public static String[] mergeArrays(String[] arr1, String[] arr2)
	{
		String[] output = new String[arr1.length + arr2.length - 2];
		int count = 0;

		for (int i = 0; i < arr1.length; i++)
		{
			output[count] = arr1[i];
			count++;
		}

		for (int i = 0; i < arr2.length; i++)
		{
			output[count] = arr2[i];
			count++;
		}

		return output;
	}

	public static void printArray(Object[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static void printArray(boolean[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static void printArray(byte[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static void printArray(char[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static void printArray(double[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static void printArray(float[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static void printArray(int[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static void printArray(long[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static void printArray(short[] arr)
	{
		System.out.println(Arrays.toString(arr));
	}

	public static Object[] removeElementFromArray(Object[] arr, Object obj)
	{
		if (arr.length - 1 <= 0)
			return new Object[0];

		Object[] newArr = new Object[arr.length - 1];
		for (int i = 0; i < arr.length; i++)
			if (i > newArr.length - 1)
				break;
			else if (arr[i] != obj || !arr[i].equals(obj))
				newArr[i] = arr[i];

		return newArr;
	}

	/**
	 * This method will create a new, serializeable, Array containing all Values of
	 * the old Array except for the Objects at the positions you input in the
	 * pos-Array.
	 * <br>
	 * <strong>Note:</strong><br>
	 * - If the pos-Array contains a value higher than <code>arr.length - 1</code>
	 * this method will just return the Input array.
	 * 
	 * @param arr The array to remove the Objects from
	 * @param pos The positions of the Array to ignore
	 * @return a new, serializable Array with every Element except the indicated
	 *         ones that were removed
	 */
	public static Object[] removeElementFromArray(Object[] arr, int... pos)
	{
		if (arr.length - 1 <= 0)
			return new Object[0];
		else if (containsValueHigherThan(pos, arr.length - 1))
			return arr;

		Object[] newArr = new Object[arr.length - pos.length];
		int count = 0;
		for (int i = 0; i < arr.length; i++)
			if (!containsValue(pos, i))
			{
				newArr[count] = arr[count];
				count++;
			}

		return newArr;
	}

	public static Object[] replaceValue(Object[] arr, int pos, Object val)
	{
		arr[pos] = val;
		return arr;
	}

	public static Object[] replaceAll(Object[] arr, Object... replace)
	{
		Object[] newArr = new Object[arr.length - replace.length];
		int count = 0;
		for (Object object : arr)
		{
			if (!containsValue(replace, object))
				newArr[count] = object;

			count++;
		}

		return newArr;
	}

	/**
	 * Utility method for instances where the normal "Hello Mark!".split(" ") Method
	 * does not work (for example characters like '/' or '.').
	 * 
	 * @param str   The String to split
	 * @param split The character which will split the String
	 * @return A new String Array, which is split accordingly.
	 */
	public static String[] splitStringToArray(String str, char split)
	{
		StringBuilder builder = new StringBuilder();
		char[] chars = str.toCharArray();
		String[] output = new String[0];

		for (char c : chars)
			if (c != split)
				builder.append(c);
			else
			{
				output = addElementToArray(output, builder.toString());
				builder.delete(0, builder.toString().length());
			}

		return output = addElementToArray(output, builder.toString());
	}

	/**
	 * Utility method to split a String in special cases where the normal
	 * String.split("") Method doesn't work.
	 * 
	 * @param str   The String to split
	 * @param split The String with which to split
	 * @return A new String Array split accordingly
	 */
	public static String[] splitStringToArray(String str, String split)
	{
		List<Character> lastChars = new ArrayList<Character>();
		StringBuilder builder = new StringBuilder();
		char[] splitChars = split.toCharArray();
		char[] chars = str.toCharArray();
		String[] output = new String[0];

		for (char c : chars)
			if (!containsConsecutiveValues(lastChars, splitChars))
			{
				builder.append(c);
				lastChars.add(c);
			}
			else
			{
				output = addElementToArray(output,
						builder.toString().substring(0, builder.toString().length() - split.length()));
				builder.delete(0, builder.toString().length()).append(c);
				lastChars.clear();
			}

		return output = addElementToArray(output, builder.toString());
	}

	public static Object[] swapValues(Object[] arr, int posA, int posB)
	{
		Object temp = arr[posA];
		arr[posA] = arr[posB];
		arr[posB] = temp;
		return arr;
	}

	public static String toString(Object[] arr)
	{
		return Arrays.toString(arr);
	}

	public static String toString(boolean[] arr)
	{
		return Arrays.toString(arr);
	}

	public static String toString(byte[] arr)
	{
		return Arrays.toString(arr);
	}

	public static String toString(char[] arr)
	{
		return Arrays.toString(arr);
	}

	public static String toString(double[] arr)
	{
		return Arrays.toString(arr);
	}

	public static String toString(float[] arr)
	{
		return Arrays.toString(arr);
	}

	public static String toString(int[] arr)
	{
		return Arrays.toString(arr);
	}

	public static String toString(long[] arr)
	{
		return Arrays.toString(arr);
	}

	public static String toString(short[] arr)
	{
		return Arrays.toString(arr);
	}

}
