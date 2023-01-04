package cn.wangliang181230.studynativeimage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
public class TestController {

	static String[] ARGS;


	@Autowired(required = false)
	private ITestBean myTestBean;

	@Value("${test.value:0}")
	private Integer myProperty;

	@Autowired
	private ConfigurableEnvironment environment;


	@GetMapping("/")
	public Object test() {
		Map<String, Object> map = new HashMap<>();

		map.put("args", ARGS);
		map.put("env", System.getenv());
		map.put("properties", System.getProperties());
		map.put("isAotMode", AotUtils.isAotMode());
		map.put("@Value(\"${test.value:0}\")", myProperty == null ? "null" : myProperty);

		map.put("properties: test.bean.enabled", environment.getProperty("test.bean.enabled", "null"));
		map.put("properties: test.bean.type", environment.getProperty("test.bean.type", "null"));
		map.put("myTestBean", (myTestBean != null ? myTestBean.getBeanName() : "null"));

		// native-image时，无论 ARGS[0] 的类存不存在，都会抛异常，导致值为 false
		for (String className : ARGS) {
			try {
				Class.forName(className);
				map.put(className, true);
			} catch (ClassNotFoundException e) {
				map.put(className, e.toString());
			}
		}

		// 写两个类存在的类名，native-image时，会是true
		try {
			Class.forName("cn.wangliang181230.studynativeimage.TestBeanImplA");
			map.put("TestBeanImplA", true);
		} catch (ClassNotFoundException e) {
			map.put("TestBeanImplA", false);
		}
		try {
			Class.forName("cn.wangliang181230.studynativeimage.TestBeanImplB");
			map.put("TestBeanImplB", true);
		} catch (ClassNotFoundException e) {
			map.put("TestBeanImplB", false);
		}

		// 写一个类不存在的类名，native-image时，会是false
		try {
			Class.forName("cn.wangliang181230.studynativeimage.TestBeanImplXxxx");
			map.put("TestBeanImplXxxx", true);
		} catch (ClassNotFoundException e) {
			map.put("TestBeanImplXxxx", false);
		}

		return map;
	}
}
