package cn.wangliang181230.seata;

import io.seata.rm.tcc.api.BusinessActionContext;
import io.seata.rm.tcc.api.BusinessActionContextParameter;
import io.seata.rm.tcc.api.LocalTCC;
import io.seata.rm.tcc.api.TwoPhaseBusinessAction;

@LocalTCC
public interface ITestTccService {

	@TwoPhaseBusinessAction(name = "testTccService", commitMethod = "commit", rollbackMethod = "rollback")
	TccResult prepare(@BusinessActionContextParameter(isParamInProperty = true) TccParam param);

	void commit(BusinessActionContext context);

	void rollback(BusinessActionContext context);

}
