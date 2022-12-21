package cn.wangliang181230.seata;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.atomic.AtomicInteger;

@RestController
public class TestController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	private static final AtomicInteger i = new AtomicInteger(0);

	@GetMapping("/")
	@GlobalTransactional
	public Object test(@RequestParam(value = "testError", required = false) Boolean testError) {
		String xid = RootContext.getXID();
		System.out.println(xid);

		if (Boolean.TRUE.equals(testError)) {
			throw new RuntimeException("测试异常情况");
		}

		jdbcTemplate.execute("insert into tb_order (id) values (" + i.incrementAndGet() + ")");

		return "test";
	}

}
