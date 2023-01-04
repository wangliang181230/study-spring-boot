package io.seata.spring.aot;

public abstract class AotUtils {

	public static final String SPRING_AOT_PROCESSING = "spring.aot.processing";

	/**
	 * 判断是否在spring-aot的处理过程中
	 *
	 * @return true:是 | false:否
	 */
	public static boolean isSpringAotProcessing() {
		return "true".equalsIgnoreCase(System.getProperty(SPRING_AOT_PROCESSING));
	}

}
