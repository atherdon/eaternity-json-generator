package ch.generator;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

public class CSVParser {

	public final String CSV_DELIMITER = ";";
	
	public List<List<String>> parseFile(String csvFileName, StringBuilder errorMessage, String separator, int length) throws IOException {
		List<List<String>> linesList = new ArrayList<List<String>>();	
			 
		// read it with BufferedReader
		BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(csvFileName), "UTF-8"));
	 
		String line;
		int lineNumber = 1;
		while ((line = br.readLine()) != null) {
			List<String> splittedLine = Arrays.asList(line.split(separator, -1));
			
			if (splittedLine.size() < length){
				errorMessage.append("CSVParse: Error reading " + csvFileName + " on line number " + lineNumber + ": " + length + " semicolon (;) separated strings need to be provided."
						+ " Only " + splittedLine.size() + " found." + System.getProperty("line.separator"));
				continue;
			}
			linesList.add(splittedLine);
			lineNumber++;
		}
		
		br.close();
			
		return linesList;
	}

	
	public Map<Integer,String> parseMatchingItems(String csvFileName, StringBuilder errorMessage) throws IOException {
		Map<Integer,String> matchingItems = new HashMap<Integer,String>();
		
		List<List<String>> linesList = parseFile(csvFileName, errorMessage, CSV_DELIMITER, 2);

		for (List<String> singleLine : linesList) {
			matchingItems.put(Integer.valueOf(singleLine.get(0)), escapeJsonString(singleLine.get(1)));
		}
		
		return matchingItems;
	}

	
	public static String escapeJsonString(String string) {
        if (string == null || string.length() == 0) {
            return "\"\"";
        }

        char         c = 0;
        int          i;
        int          len = string.length();
        StringBuilder sb = new StringBuilder(len + 4);
        String       t;

        for (i = 0; i < len; i += 1) {
            c = string.charAt(i);
            switch (c) {
            case '\\':
            case '"':
                sb.append('\\');
                sb.append(c);
                break;
            case '/':
//                if (b == '<') {
                    sb.append('\\');
//                }
                sb.append(c);
                break;
            case '\b':
                sb.append("\\b");
                break;
            case '\t':
                sb.append("\\t");
                break;
            case '\n':
                sb.append("\\n");
                break;
            case '\f':
                sb.append("\\f");
                break;
            case '\r':
               sb.append("\\r");
               break;
            default:
                if (c < ' ') {
                    t = "000" + Integer.toHexString(c);
                    sb.append("\\u" + t.substring(t.length() - 4));
                } else {
                    sb.append(c);
                }
            }
        }
        return sb.toString();
    }
}