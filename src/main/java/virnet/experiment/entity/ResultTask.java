package virnet.experiment.entity;

public class ResultTask implements java.io.Serializable {

	private static final long serialVersionUID = -1649695575201344021L;
	/**
	 * 实验结果任务表
	 */
	
	private Integer resultTaskId;
	private Integer resultCaseId;
	private Integer resultExpId;
	private Integer resultTaskOrder;
	private Integer resultTaskType;
	private String  resultTaskContent;
	
	public ResultTask(Integer resultTaskId, Integer resultCaseId, Integer resultExpId, Integer resultTaskOrder,
			Integer resultTaskType, String resultTaskContent) {
		super();
		this.resultTaskId = resultTaskId;
		this.resultCaseId = resultCaseId;
		this.resultExpId = resultExpId;
		this.resultTaskOrder = resultTaskOrder;
		this.resultTaskType = resultTaskType;
		this.resultTaskContent = resultTaskContent;
	}

	public ResultTask(Integer resultTaskId, Integer resultCaseId, Integer resultExpId, Integer resultTaskOrder) {
		super();
		this.resultTaskId = resultTaskId;
		this.resultCaseId = resultCaseId;
		this.resultExpId = resultExpId;
		this.resultTaskOrder = resultTaskOrder;
	}

	public Integer getResultTaskId() {
		return resultTaskId;
	}

	public void setResultTaskId(Integer resultTaskId) {
		this.resultTaskId = resultTaskId;
	}

	public Integer getResultCaseId() {
		return resultCaseId;
	}

	public void setResultCaseId(Integer resultCaseId) {
		this.resultCaseId = resultCaseId;
	}

	public Integer getResultExpId() {
		return resultExpId;
	}

	public void setResultExpId(Integer resultExpId) {
		this.resultExpId = resultExpId;
	}

	public Integer getResultTaskOrder() {
		return resultTaskOrder;
	}

	public void setResultTaskOrder(Integer resultTaskOrder) {
		this.resultTaskOrder = resultTaskOrder;
	}

	public Integer getResultTaskType() {
		return resultTaskType;
	}

	public void setResultTaskType(Integer resultTaskType) {
		this.resultTaskType = resultTaskType;
	}

	public String getResultTaskContent() {
		return resultTaskContent;
	}

	public void setResultTaskContent(String resultTaskContent) {
		this.resultTaskContent = resultTaskContent;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
	
	
}
