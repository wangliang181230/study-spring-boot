package cn.wangliang181230.seata;

import io.seata.core.context.RootContext;
import io.seata.spring.annotation.GlobalTransactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;
import jakarta.annotation.PostConstruct;

@Service
public class TestService {

	private static final Logger LOGGER = LoggerFactory.getLogger(TestService.class);

	private static final AtomicInteger i = new AtomicInteger(0);


	@Autowired
	private JdbcTemplate jdbcTemplate;


	@PostConstruct
	public void cleanData() {
		this.clean();
	}

	@GlobalTransactional
	public String test(Boolean testError, Boolean testError2, long sleepTime) throws InterruptedException {
		String xid = RootContext.getXID();
		LOGGER.info("xid: {}", xid);

		if (Boolean.TRUE.equals(testError)) {
			LOGGER.error("throw error1");
			throw new RuntimeException("测试异常情况");
		}

		LOGGER.info("after testError");

		jdbcTemplate.execute("insert into tb_order (id) values (" + i.incrementAndGet() + ")");

		LOGGER.info("after insert");

		if (Boolean.TRUE.equals(testError2)) {
			LOGGER.error("throw error1");
			if (sleepTime > 0) {
				Thread.sleep(sleepTime);
			}
			throw new RuntimeException("测试异常情况2");
		}

		LOGGER.info("after testError2");

		return "test";
	}

	public void clean() {
		LOGGER.info("clean tb_order");
		jdbcTemplate.execute("delete from tb_order");
	}

	public int count() {
		return jdbcTemplate.queryForObject("select count(*) from tb_order", Integer.class);
	}

}
