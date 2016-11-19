package virnet.management.log.service;

public class Sha256 {
	public String encode(String password){
		return password.hashCode() + "";
	}
}
