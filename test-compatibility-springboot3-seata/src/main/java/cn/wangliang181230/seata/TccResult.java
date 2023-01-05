package cn.wangliang181230.seata;

public class TccResult {

	private String xid;

	private String a;


	public TccResult() {
	}

	public TccResult(String xid) {
		this.xid = xid;
	}

	public TccResult(String xid, String a) {
		this.xid = xid;
		this.a = a;
	}

	public String getXid() {
		return xid;
	}

	public void setXid(String xid) {
		this.xid = xid;
	}

	public String getA() {
		return a;
	}

	public void setA(String a) {
		this.a = a;
	}
}
