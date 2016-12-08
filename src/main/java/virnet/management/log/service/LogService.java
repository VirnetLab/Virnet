package virnet.management.log.service;

import java.util.ArrayList;
import java.util.List;
import virnet.management.dao.UserDAO;
import virnet.management.entity.User;
import virnet.management.entity.Character;
import virnet.management.log.action.LogEntity;

public class LogService {
	private Sha256 passwordprocessor = new Sha256();
	private UserDAO userDao = new UserDAO();
	
	/**
	 * three steps : check user, check key result(password), get character and operation list
	 * @param user_id the primary key of user table, also the user name in page
	 * @param password the key of user table, also the password in page
	 * @return for state, 0 stands for no such user,  
	 */
	@SuppressWarnings("unchecked")
	public LogEntity login(String user_id, String password){
		System.out.println("this is LogService.login method, and the argv[2] is" + user_id + ", " + password);
		
		LogEntity data = new LogEntity();
		
		//get user
		User instance = this.userDao.getUniqueByProperty("userId", user_id);
		
		if(instance == null){
			//no such user
			data.setState(1);
			data.setStatement("no such user");
			data.setCharacterPowerlist(null);
			return data;
		}
		
		//check key result
		boolean check = checkKeyResult(instance.getUserKeyResult(), this.passwordprocessor.encode(password));//password encode with sha1/sha256
		
		if(!check){
			//wrong password
			data.setState(2);
			data.setStatement("wrong password");
			data.setCharacterPowerlist(null);
			return data;
		}
		
		//get character and operation list
		List<Character> clist = this.userDao.getCharacterGroup(instance);
		
		if(clist != null && clist.size() != 0){
			//log successfully
			List<Object> cha_p = new ArrayList<Object>();
			
			int size = clist.size();
			for(int i = 0; i < size; i++){
				cha_p.addAll(CharacterOperation.getOperation(clist.get(i)));
			}
			
			data.setState(0);
			data.setStatement("log in");
			data.setCharacterPowerlist(cha_p);
			return data;
		}
		
		//exist user with proper password but have no characters, deny
		data.setState(3);
		data.setStatement("user with no character");
		data.setCharacterPowerlist(null);
		
		return data;
	}
	
	private boolean checkKeyResult(Integer saved, String sent){
		boolean check = saved.equals(sent);
		
		return check;
	}
}