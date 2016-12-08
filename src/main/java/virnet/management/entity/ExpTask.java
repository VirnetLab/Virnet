package virnet.management.entity;

public class ExpTask implements java.io.Serializable {

	/**
	 * 实验模板任务表
	 */
	private static final long serialVersionUID = 5606861693387912292L;
	private Integer expTaskId;
	private Integer expId;
	private Integer expTaskOrder;
	private Integer expTaskType;
	private String  expTaskContent;
	
	
	/** default constructor */
	public ExpTask(){	
	}
	
	
	/** minimal constructor */
	public ExpTask(Integer expTaskId, Integer expId) {
		super();
		this.expTaskId = expTaskId;
		this.expId = expId;
	}

	/** full constryctor */
	public ExpTask(Integer expTaskId, Integer expId, Integer expTaskOrder, Integer expTaskType, String expTaskContent) {
		super();
		this.expTaskId = expTaskId;
		this.expId = expId;
		this.expTaskOrder = expTaskOrder;
		this.expTaskType = expTaskType;
		this.expTaskContent = expTaskContent;
	}

	public Integer getExpTaskId() {
		return expTaskId;
	}

	public void setExpTaskId(Integer expTaskId) {
		this.expTaskId = expTaskId;
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

	public Integer getExpTaskType() {
		return expTaskType;
	}

	public void setExpTaskType(Integer expTaskType) {
		this.expTaskType = expTaskType;
	}

	public String getExpTaskContent() {
		return expTaskContent;
	}

	public void setExpTaskContent(String expTaskContent) {
		this.expTaskContent = expTaskContent;
	}
		
}
