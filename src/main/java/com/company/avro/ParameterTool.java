package com.company.avro;

import org.apache.commons.lang3.math.NumberUtils;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class ParameterTool {

	protected static final String NO_VALUE_KEY = "__NO_VALUE_KEY";
	protected static final String DEFAULT_UNDEFINED = "<undefined>";

	public static String getKeyFromArgs(String[] args, int index) {
		String key;
		if (args[index].startsWith("--")) {
			key = args[index].substring(2);
		} else if (args[index].startsWith("-")) {
			key = args[index].substring(1);
		} else {
			throw new IllegalArgumentException(
					String.format("Error parsing arguments '%s' on '%s'. Please prefix keys with -- or -.",
							Arrays.toString(args), args[index]));
		}

		if (key.isEmpty()) {
			throw new IllegalArgumentException(
					"The input " + Arrays.toString(args) + " contains an empty argument");
		}

		return key;
	}

	public static Map<String, String> fromArgs(String[] args) {
		final Map<String, String> map = new HashMap<>(args.length / 2);

		int i = 0;
		while (i < args.length) {
			final String key = ParameterTool.getKeyFromArgs(args, i);

			if (key.isEmpty()) {
				throw new IllegalArgumentException(
						"The input " + Arrays.toString(args) + " contains an empty argument");
			}

			i += 1; // try to find the value

			if (i >= args.length) {
				map.put(key, NO_VALUE_KEY);
			} else if (NumberUtils.isNumber(args[i])) {
				map.put(key, args[i]);
				i += 1;
			} else if (args[i].startsWith("--") || args[i].startsWith("-")) {
				// the argument cannot be a negative number because we checked earlier
				// -> the next argument is a parameter name
				map.put(key, NO_VALUE_KEY);
			} else {
				map.put(key, args[i]);
				i += 1;
			}
		}
		return map;
	}
}
