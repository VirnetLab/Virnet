package virnet.management.entity;

public class Appointment implements java.io.Serializable {

	private static final long serialVersionUID = 5846227340990550662L;
	/**
	 * 实验机柜预约表
	 */
	private Integer appointmentTimeId;
	private Integer appointmentCabinetId;
	
	public Appointment(){
		
	}
	public Appointment(Integer appointmentTimeId, Integer appointmentCabinetId) {
		super();
		this.appointmentTimeId = appointmentTimeId;
		this.appointmentCabinetId = appointmentCabinetId;
	}
	public Integer getAppointmentTimeId() {
		return appointmentTimeId;
	}
	public void setAppointmentTimeId(Integer appointmentTimeId) {
		this.appointmentTimeId = appointmentTimeId;
	}
	public Integer getAppointmentCabinetId() {
		return appointmentCabinetId;
	}
	public void setAppointmentCabinetId(Integer appointmentCabinetId) {
		this.appointmentCabinetId = appointmentCabinetId;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	

	
}
