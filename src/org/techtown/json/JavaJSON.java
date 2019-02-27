package org.techtown.json;


import java.io.FileReader;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
public class JavaJSON {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		String path = "D:\\Infra\\sample.json";
		System.out.println("parsing 완료");
		JSONParser parser = new JSONParser();
		
		try {
			//String path = "D:\\Infra\\sample.json";
			//JSONParser parser = new JSONParser();
			Object obj = parser.parse(new FileReader(path));
			JSONObject jsonObject = (JSONObject) obj;
			
			String name = (String) jsonObject.get("name");
			String email = (String) jsonObject.get("email");
			Long age = (Long) jsonObject.get("age");
			
			System.out.println(name + email + age);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
