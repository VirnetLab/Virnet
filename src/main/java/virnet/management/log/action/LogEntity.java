package virnet.management.log.action;

import java.util.ArrayList;
import java.util.List;

public class LogEntity {
	private int state;
	private String statement;
	private List<Object> character_power = new ArrayList<Object>();
	
	public int getState(){
		return this.state;
	}
	
	public void setState(int s){
		this.state = s;
	}
	
	public String getStatement(){
		return this.statement;
	}
	
	public void setStatement(String s){
		this.statement = new String(s);
	}
	
	public List<Object> getCharacterPowerlist(){
		return this.character_power;
	}
	
	public void setCharacterPowerlist(List<Object> cha_p){
		this.character_power.clear();
		if(cha_p != null){
			this.character_power.addAll(cha_p);
		}
	}
}
