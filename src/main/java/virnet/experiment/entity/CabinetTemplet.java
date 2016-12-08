package virnet.experiment.entity;

public class CabinetTemplet implements java.io.Serializable {

	/**
	 * 实验机柜模板表
	 */
	private static final long serialVersionUID = 5813221532139099171L;
	private Integer cabinetTempletId;
	private String cabinetTempletName;
	private String cabinetTempletLimit;
	private String cabinetTempletRemark;
	
	public CabinetTemplet(Integer cabinetTempletId, String cabinetTempletName, String cabinetTempletLimit,
			String cabinetTempletRemark) {
		super();
		this.cabinetTempletId = cabinetTempletId;
		this.cabinetTempletName = cabinetTempletName;
		this.cabinetTempletLimit = cabinetTempletLimit;
		this.cabinetTempletRemark = cabinetTempletRemark;
	}

	public CabinetTemplet(Integer cabinetTempletId, String cabinetTempletName) {
		super();
		this.cabinetTempletId = cabinetTempletId;
		this.cabinetTempletName = cabinetTempletName;
	}

	public Integer getCabinetTempletId() {
		return cabinetTempletId;
	}

	public void setCabinetTempletId(Integer cabinetTempletId) {
		this.cabinetTempletId = cabinetTempletId;
	}

	public String getCabinetTempletName() {
		return cabinetTempletName;
	}

	public void setCabinetTempletName(String cabinetTempletName) {
		this.cabinetTempletName = cabinetTempletName;
	}

	public String getCabinetTempletLimit() {
		return cabinetTempletLimit;
	}

	public void setCabinetTempletLimit(String cabinetTempletLimit) {
		this.cabinetTempletLimit = cabinetTempletLimit;
	}

	public String getCabinetTempletRemark() {
		return cabinetTempletRemark;
	}

	public void setCabinetTempletRemark(String cabinetTempletRemark) {
		this.cabinetTempletRemark = cabinetTempletRemark;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}	
}
