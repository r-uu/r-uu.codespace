package de.ruu.lib.util;

import java.lang.reflect.Array;
import java.util.*;

import static de.ruu.lib.util.BooleanFunctions.not;

public interface Collections
{
	/**
	 * @param <T>
	 * @param elems if {@code null}, an empty list will be returned
	 * @return a mutable set with {@code elems} as items, modifications of the set will <b>not</b> change {@code elems}
	 *         array
	 */
	public static <T> Set<T> asSet(T... elems)
	{
		if (elems == null) return new HashSet<>();
		return new HashSet<>(Arrays.asList(elems));
	}

	/**
	 * @param <T>
	 * @param elems if <code>null</code>, an empty list will be returned
	 * @return a mutable list with {@code elems} as items, modifications of the set will <b>not</b> change {@code elems}
	 *         array
	 */
	public static <T> List<T> asList(T... elems)
	{
		if (elems == null) return new ArrayList<>();
		return new ArrayList<>(Arrays.asList(elems));
	}

	public static <T> List<T> asList(Iterable<T> iterable)
	{
		if (iterable == null) return new ArrayList<>();

		List<T> result = new ArrayList<>();

		Iterator<T> iterator = iterable.iterator();

		while (iterator.hasNext())
		{
			result.add(iterator.next());
		}

		return result;
	}

	@SuppressWarnings("unchecked")
	public static <T> T[] asArray(Class<?> clazz, Collection<T> collection)
	{
		return collection.toArray((T[]) Array.newInstance(clazz, collection.size()));
	}


	public static boolean isNullOrEmpty(Collection<?> collection)
	{
		if (Objects.isNull(collection))
		{
			return true;
		}
		return collection.isEmpty();
	}

	public static boolean isNotNullOrEmpty(Collection<?> collection)
	{
		return not(isNullOrEmpty(collection));
	}
}