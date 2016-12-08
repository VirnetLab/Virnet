package virnet.management.log.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import virnet.management.entity.Character;

public class CharacterOperation {
	
	public static List<Object> getOperation(Character character){
		List<Object> Character0 = new ArrayList<Object>();
		List<Object> Character1 = new ArrayList<Object>();
		List<Object> Character2 = new ArrayList<Object>();
		List<Object> Character3 = new ArrayList<Object>();

		Map<String, Object> Character00 = new HashMap<String, Object>();
		Map<String, Object> Character01 = new HashMap<String, Object>();
		Map<String, Object> Character02 = new HashMap<String, Object>();
		Map<String, Object> Character03 = new HashMap<String, Object>();
		
		Character00.put("operation", "exp-management");
		Character01.put("operation", "course-management");
		Character02.put("operation", "class-management");
		Character03.put("operation", "time-management");
		
		Character0.add(Character00);
		Character0.add(Character01);
		Character0.add(Character02);
		Character0.add(Character03);
		
		Map<String, Object> Character10 = new HashMap<String, Object>();
		Map<String, Object> Character11 = new HashMap<String, Object>();
		Map<String, Object> Character12 = new HashMap<String, Object>();
		
		Character10.put("operation", "exp-staff");
		Character11.put("operation", "teacher");
		Character12.put("operation", "student");
		
		Character1.add(Character10);
		Character1.add(Character11);
		Character1.add(Character12);
		
		Map<String, Object> Character20 = new HashMap<String, Object>();
		Map<String, Object> Character21 = new HashMap<String, Object>();
		Map<String, Object> Character22 = new HashMap<String, Object>();
		
		Character20.put("operation", "class-detail");
		Character21.put("operation", "group");
		Character22.put("operation", "exp-arrangement");
		
		Character2.add(Character20);
		Character2.add(Character21);
		Character2.add(Character22);

		Map<String, Object> Character30 = new HashMap<String, Object>();
		Map<String, Object> Character31 = new HashMap<String, Object>();
		Map<String, Object> Character32 = new HashMap<String, Object>();
		Map<String, Object> Character33 = new HashMap<String, Object>();
		
		Character30.put("operation", "my-class");
		Character31.put("operation", "my-group");
		Character32.put("operation", "my-exp");
		Character33.put("operation", "enter-exp");
		
		Character3.add(Character30);
		Character3.add(Character31);
		Character3.add(Character32);
		Character3.add(Character33);
		
		switch(character.getCharacterId()){
		case 0 : return Character0; 
		case 1 : return Character1; 
		case 2 : return Character2;
		case 3 : return Character3;
		default : return null;
		}
	}
}
