package cn.wangliang181230.seata;

import io.seata.rm.tcc.api.BusinessActionContextParameter;

public class TccParamSuperClass {

	@BusinessActionContextParameter
	private String a;


	public TccParamSuperClass() {
	}

	public TccParamSuperClass(String a) {
		this.a = a;
	}


	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
}
