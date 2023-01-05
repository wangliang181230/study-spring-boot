package cn.wangliang181230.seata;

public class TccParam extends TccParamSuperClass {

	private TccParam2 tccParam2;


	public TccParam() {
		super();
	}

	public TccParam(String a) {
		super(a);
	}

	public TccParam(String a, TccParam2 tccParam2) {
		this(a);
		this.tccParam2 = tccParam2;
	}


	public TccParam2 getTccParam2() {
		return tccParam2;
	}

	public void setTccParam2(TccParam2 tccParam2) {
		this.tccParam2 = tccParam2;
	}
}
