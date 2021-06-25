package com.expertworks.lms.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

public class DynamoDBUtil {
	
	public static String img = "https://d3s24np0er9fug.cloudfront.net/phase1/courses/python/expert/expert.jpg";
	public static String prefix = "https://d3s24np0er9fug.cloudfront.net/phase1/courses/python/expert/videos/hls/";
	public static String courseid = "6700";
	
	public static HashMap<String,List> readFile() throws Exception {

		List<String> videoList = new ArrayList();
		List<String> titleList = new ArrayList();
		
		HashMap<String,List> map = new HashMap();
		
		
		Scanner scanner = new Scanner(new File("D:\\dynamo\\fileNames5.txt"));
		int lineNo = 0;
		while (scanner.hasNextLine()) {
			String line = scanner.nextLine();
			System.out.println(lineNo++  + ":line :  " + prefix + line);
			System.out.println("-----------------------------------------");
			String[] stringArray = line.split(",");
			
			for (int i = 0; i < stringArray.length; i++) {
				
				System.out.println(i +") "+stringArray[i].trim());
			}
			
			titleList.add(stringArray[0].trim());  // first title
			videoList.add(prefix+stringArray[1].trim().replace(".mp4",".m3u8") );// video
			// process the line
		}
		System.out.println("-----------------------------------------");
		map.put("videoList", videoList);
		map.put("titleList", titleList);
		
		return map;
	}

	@SuppressWarnings("unchecked")
	public static void main(String[] args) throws Exception {
		
		HashMap<String,List> map = DynamoDBUtil.readFile();

		List<String> videoList = map.get("videoList");
		List<String> titleList = map.get("titleList");

		// JSON parser object to parse read file
		JSONParser jsonParser = new JSONParser();

		try (FileReader reader = new FileReader("D:\\dynamo\\row.json")) {
			// Read JSON file
			Object row = jsonParser.parse(reader);

			for (int i = 0; i < videoList.size(); i++) {

				JSONObject item = (JSONObject) row;

				JSONObject putRequest = (JSONObject) item.get("PutRequest");

				JSONObject itemmmm = (JSONObject) putRequest.get("Item");

				JSONObject sk = (JSONObject) itemmmm.get("sk");
				
				JSONObject courseId = (JSONObject) itemmmm.get("courseId");
				
				courseId.put("S", DynamoDBUtil.courseid);
				
				JSONObject secsubtitle = (JSONObject) itemmmm.get("subtitle");
				JSONObject sectitle = (JSONObject) itemmmm.get("title");
				
				sectitle.put("S", "Section "+(i+1)+":"+titleList.get(i));
				secsubtitle.put("S", "Section "+(i+1)+":"+titleList.get(i));
				
				String sec="S#"+(i+1);

				sk.put("S", sec);

				JSONObject videoLinks = (JSONObject) itemmmm.get("videoLinks");

				JSONArray L = (JSONArray) videoLinks.get("L");

				JSONObject videoobj = (JSONObject) L.get(0);

				JSONObject M = (JSONObject) videoobj.get("M");

				JSONObject url = (JSONObject) M.get("url");
				
				JSONObject img = (JSONObject) M.get("img");
				
				img.put("S", DynamoDBUtil.img);
				
				JSONObject videotitle = (JSONObject) M.get("title");
				
				videotitle.put("S", titleList.get(i));

				// JSONObject S = (JSONObject) url.get("S");

				url.put("S", videoList.get(i));
				//System.out.println(url.get("S"));

				JSONObject objs = new JSONObject(item);
				
				
				
				System.out.println(objs+",");

			}

		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	private static void parseEmployeeObject(JSONObject employee) {
		// Get employee object within list
		JSONObject employeeObject = (JSONObject) employee.get("Item");

		// Get employee first name
		// String firstName = (String) employeeObject.get("courseId");
		System.out.println(employeeObject);

	}

}
