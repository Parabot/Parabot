package org.parabot.core.reflect;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

/**
 * 
 * @author Everel
 * 
 */
public class RefUtils {

	public static final Set<Class<?>> PRIMITIVE_TYPES = new HashSet<Class<?>>(
			Arrays.asList(Boolean.class, Character.class, Byte.class,
					Short.class, Integer.class, Long.class, Float.class,
					Double.class, Void.class));

	public static boolean isPrimitive(Class<?> clazz) {
		return PRIMITIVE_TYPES.contains(clazz);
	}

}
