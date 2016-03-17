import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Comparator;
import java.util.HashMap;

public class LogAnalysis {
	
	public HashMap<String, Node> logAnalysis(File folder) throws IOException {
		HashMap<String, Node> map = new HashMap<String, Node>();
		File[] files = folder.listFiles();
		Arrays.sort(files, new Comparator<File>() {
			public int compare(File f1, File f2) {
				return f1.getName().compareTo(f2.getName());
			}
		});
		for(File file : files) {
			if(file.getName().equals(".DS_Store")) {
				continue;
			}
			eachFile(file, map);
		}
		return map;
	}
	
	private void eachFile(File file, HashMap<String, Node> map) throws IOException {
		InputStream is = new FileInputStream(file);
		int size = is.available();
		int count = 0;
		for(int i = 0; i < size; i++) {
			StringBuilder sb = new StringBuilder();
			char temp = 'a';
			while(i < size && temp != '\n' && temp != -1 && temp != '\r') {
				temp = (char) is.read();
				if(temp != -1 && temp != '\n' && temp != '\r') {
					sb.append(temp);
					i++;
				}
			}
			if(count++ >= 3 && sb.length() > 0) {
				String[] strs = getStrArray(sb.toString().trim());
				if(strs[1] == null || strs[2] == null || strs[3] == null) {
					continue;
				}
				if(!map.containsKey(strs[3])) {
					Node node = new Node();
					map.put(strs[3], node);
				}
				Node node = map.get(strs[3]);
				node.InsNum.add(Integer.parseInt(strs[1]));
				node.Bytes.add(Integer.parseInt(strs[2]));
			}
		}
		is.close();
		return;
	}
	
	private String[] getStrArray(String s) {
		String[] res = new String[4];
		int index = 0;
		for(int i = 0; i < s.length(); i++) {
			StringBuilder sb = new StringBuilder();
			while(i < s.length() && s.charAt(i) == ' ') {
				i++;
			}
			while(i < s.length() && s.charAt(i) != ' ') {
				sb.append(s.charAt(i));
				i++;
			}
			res[index++] = sb.toString();
		}
		return res;
	}
	
	public void outPutResult(HashMap<String, Node> map) throws IOException {
		for(String key : map.keySet()) {
			File f = new File("./output/" + key + ".csv");
			OutputStream os = new FileOutputStream(f);
			OutputStreamWriter writer = new OutputStreamWriter(os, "UTF-8");
			writer.append("Time").append(',').append(key + " Data").append('\n');
			for(int i = 0; i < map.get(key).InsNum.size(); i++) {
				writer.append(String.valueOf(i)).append(',').append(String.valueOf(map.get(key).InsNum.get(i))).append('\n');
			}
			writer.close();
			os.close();
		}
	}
	
	public static void main(String[] args) throws IOException {
		LogAnalysis test = new LogAnalysis();
		File f = new File("./jmapLog");
//		test.logAnalysis(f);
		HashMap<String, Node> res = test.logAnalysis(f);
//		for(String key : res.keySet()) {
//			Node node = res.get(key);
//			System.out.println("Class: " + key);
//			System.out.println("Instance: " + node.InsNum);
//			System.out.println("Bytes: " + node.Bytes);
//		}
		test.outPutResult(res);
	}
	
}
