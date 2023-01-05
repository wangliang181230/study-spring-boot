package cn.wangliang181230.seata;

import io.seata.core.context.RootContext;
import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TestTccServiceImpl implements ITestTccService {
	private static final Logger LOGGER = LoggerFactory.getLogger(TestTccServiceImpl.class);


	@Override
	public TccResult prepare(TccParam param) {
		LOGGER.info("test tcc: prepare, xid: {}, a: {}", RootContext.getXID(), param.getA());
		return new TccResult(RootContext.getXID(), param.getA());
	}

	@Override
	public void commit(BusinessActionContext context) {
		LOGGER.info("test tcc: commit, xid: {}, a: {}, map: {}",
				context.getXid(), context.getActionContext("a"), context.getActionContext());
	}

	@Override
	public void rollback(BusinessActionContext context) {
		LOGGER.info("test tcc: rollback, xid: {}, a: {}, map: {}",
				context.getXid(), context.getActionContext("a"), context.getActionContext());
	}
}
