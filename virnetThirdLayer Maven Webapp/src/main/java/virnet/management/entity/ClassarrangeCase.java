package virnet.management.entity;

/**
 * ClassarrangeCase entity. @author MyEclipse Persistence Tools
 */

public class ClassarrangeCase implements java.io.Serializable {

	// Fields

	/**
	 * 
	 */
	private static final long serialVersionUID = 815620231312262799L;
	private Integer classarrangeCaseId;
	private Integer classarrangeCaseClassArrangeId;
	private Integer classarrangeCaseExpId;
	private Integer classarrangeCaseUnitedOrder;
	private Integer classarrangeCaseMinOrderNum;
	private Integer classarrangeCaseMaxOrderNum;
	private Integer classarrangeCaseMinStartNum;
	private Integer classarrangeCaseMaxStartNum;

	// Constructors

	/** default constructor */
	public ClassarrangeCase() {
	}

	/** minimal constructor */
	public ClassarrangeCase(Integer classarrangeCaseClassArrangeId,
			Integer classarrangeCaseExpId) {
		this.classarrangeCaseClassArrangeId = classarrangeCaseClassArrangeId;
		this.classarrangeCaseExpId = classarrangeCaseExpId;
	}

	/** full constructor */
	public ClassarrangeCase(Integer classarrangeCaseClassArrangeId,
			Integer classarrangeCaseExpId, Integer classarrangeCaseUnitedOrder,
			Integer classarrangeCaseMinOrderNum,
			Integer classarrangeCaseMaxOrderNum,
			Integer classarrangeCaseMinStartNum,
			Integer classarrangeCaseMaxStartNum) {
		this.classarrangeCaseClassArrangeId = classarrangeCaseClassArrangeId;
		this.classarrangeCaseExpId = classarrangeCaseExpId;
		this.classarrangeCaseUnitedOrder = classarrangeCaseUnitedOrder;
		this.classarrangeCaseMinOrderNum = classarrangeCaseMinOrderNum;
		this.classarrangeCaseMaxOrderNum = classarrangeCaseMaxOrderNum;
		this.classarrangeCaseMinStartNum = classarrangeCaseMinStartNum;
		this.classarrangeCaseMaxStartNum = classarrangeCaseMaxStartNum;
	}

	// Property accessors

	public Integer getClassarrangeCaseId() {
		return this.classarrangeCaseId;
	}

	public void setClassarrangeCaseId(Integer classarrangeCaseId) {
		this.classarrangeCaseId = classarrangeCaseId;
	}

	public Integer getClassarrangeCaseClassArrangeId() {
		return this.classarrangeCaseClassArrangeId;
	}

	public void setClassarrangeCaseClassArrangeId(
			Integer classarrangeCaseClassArrangeId) {
		this.classarrangeCaseClassArrangeId = classarrangeCaseClassArrangeId;
	}

	public Integer getClassarrangeCaseExpId() {
		return this.classarrangeCaseExpId;
	}

	public void setClassarrangeCaseExpId(Integer classarrangeCaseExpId) {
		this.classarrangeCaseExpId = classarrangeCaseExpId;
	}

	public Integer getClassarrangeCaseUnitedOrder() {
		return this.classarrangeCaseUnitedOrder;
	}

	public void setClassarrangeCaseUnitedOrder(
			Integer classarrangeCaseUnitedOrder) {
		this.classarrangeCaseUnitedOrder = classarrangeCaseUnitedOrder;
	}

	public Integer getClassarrangeCaseMinOrderNum() {
		return this.classarrangeCaseMinOrderNum;
	}

	public void setClassarrangeCaseMinOrderNum(
			Integer classarrangeCaseMinOrderNum) {
		this.classarrangeCaseMinOrderNum = classarrangeCaseMinOrderNum;
	}

	public Integer getClassarrangeCaseMaxOrderNum() {
		return this.classarrangeCaseMaxOrderNum;
	}

	public void setClassarrangeCaseMaxOrderNum(
			Integer classarrangeCaseMaxOrderNum) {
		this.classarrangeCaseMaxOrderNum = classarrangeCaseMaxOrderNum;
	}

	public Integer getClassarrangeCaseMinStartNum() {
		return this.classarrangeCaseMinStartNum;
	}

	public void setClassarrangeCaseMinStartNum(
			Integer classarrangeCaseMinStartNum) {
		this.classarrangeCaseMinStartNum = classarrangeCaseMinStartNum;
	}

	public Integer getClassarrangeCaseMaxStartNum() {
		return this.classarrangeCaseMaxStartNum;
	}

	public void setClassarrangeCaseMaxStartNum(
			Integer classarrangeCaseMaxStartNum) {
		this.classarrangeCaseMaxStartNum = classarrangeCaseMaxStartNum;
	}

}