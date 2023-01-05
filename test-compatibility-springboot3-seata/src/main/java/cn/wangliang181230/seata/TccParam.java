package cn.wangliang181230.seata;

import io.seata.rm.tcc.api.BusinessActionContextParameter;

public class TccParam {

	@BusinessActionContextParameter
	private String a;


	public TccParam() {
	}

	public TccParam(String a) {
		this.a = a;
	}


	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
}
