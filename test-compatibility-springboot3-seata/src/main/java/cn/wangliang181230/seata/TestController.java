package cn.wangliang181230.seata;

import io.seata.spring.annotation.GlobalTransactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

	@Autowired
	private JdbcTemplate jdbcTemplate;

	@GetMapping("/test")
	@GlobalTransactional
	public Object test() {
		jdbcTemplate.execute("insert into tb_order (id) values (1)");

		return "test";
	}

}
