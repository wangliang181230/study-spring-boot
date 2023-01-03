package cn.wangliang181230.studynativeimage;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class MyAspect {

	private static final Logger LOGGER = LoggerFactory.getLogger(MyAspect.class);


	@Pointcut(value = "@annotation(cn.wangliang181230.studynativeimage.MyAnnotation)")   // 切点为注解
	public void pointCut() {
	}

	/**
	 * 方法式拦截
	 *
	 * @param joinPoint
	 */
	@Around("pointCut()")
	public Object beforePointCut(ProceedingJoinPoint joinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature)joinPoint.getSignature();
		Method method = signature.getMethod();

		LOGGER.info("方法式拦截: {}", method.getName());
		LOGGER.warn("方法式拦截: {}", method.getName());
		LOGGER.error("方法式拦截: {}", method.getName());

		return joinPoint.proceed();
	}

}
