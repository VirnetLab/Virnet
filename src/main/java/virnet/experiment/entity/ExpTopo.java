package virnet.experiment.entity;

public class ExpTopo implements java.io.Serializable {

	/**
	 * 实验模板拓扑表
	 */
	private static final long serialVersionUID = 8055545965784719339L;
	private Integer expTopoId;
	private Integer expId;
	private Integer expTaskOrder;
	private Integer expTopoConnetNum;
	private String  expTopoRemark;
	
	public ExpTopo(Integer expTopoId, Integer expId, Integer expTaskOrder, Integer expTopoConnetNum,
			String expTopoRemark) {
		super();
		this.expTopoId = expTopoId;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
		this.expTopoConnetNum = expTopoConnetNum;
		this.expTopoRemark = expTopoRemark;
	}

	public ExpTopo(Integer expTopoId, Integer expId, Integer expTaskOrder) {
		super();
		this.expTopoId = expTopoId;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
	}

	public Integer getExpTopoId() {
		return expTopoId;
	}

	public void setExpTopoId(Integer expTopoId) {
		this.expTopoId = expTopoId;
	}

	public Integer getExpId() {
		return expId;
	}

	public void setExpId(Integer expId) {
		this.expId = expId;
	}

	public Integer getExpTaskOrder() {
		return expTaskOrder;
	}

	public void setExpTaskOrder(Integer expTaskOrder) {
		this.expTaskOrder = expTaskOrder;
	}

	public Integer getExpTopoConnetNum() {
		return expTopoConnetNum;
	}

	public void setExpTopoConnetNum(Integer expTopoConnetNum) {
		this.expTopoConnetNum = expTopoConnetNum;
	}

	public String getexpTopoRemark() {
		return expTopoRemark;
	}

	public void setexpTopoRemark(String expTopoRemark) {
		this.expTopoRemark = expTopoRemark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}