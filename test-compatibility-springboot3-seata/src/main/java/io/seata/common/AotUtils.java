package io.seata.common;

public abstract class AotUtils {

	public static final String SPRING_AOT_PROCESSING = "spring.aot.processing";


	public static boolean isAotMode() {
		return "true".equalsIgnoreCase(System.getProperty(SPRING_AOT_PROCESSING));
	}

}
