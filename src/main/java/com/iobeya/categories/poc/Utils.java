package com.iobeya.categories.poc;

import java.util.Objects;

public class Utils {

	public static Throwable findCauseUsingPlainJava(Throwable throwable) {
		Objects.requireNonNull(throwable);
		Throwable rootCause = throwable;
		while (rootCause.getCause() != null && rootCause.getCause() != rootCause) {
			rootCause = rootCause.getCause();
		}
		return rootCause;
	}
}
