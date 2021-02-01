package se.dendromeda.receipts.util;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import se.dendromeda.receipts.data.ParseErrorsDao;
import se.dendromeda.receipts.model.ParseError;
import se.dendromeda.receipts.model.Receipt;

public class StringHandler {
	
	public static List<String> parseLines(Receipt receipt, String text){
		ParseErrorsDao errorDao = new ParseErrorsDao();
		List<String> list = new ArrayList<String>();
		String[] splitString = text.split("\n");
		String regex = ".*[ ]-?[0-9]+[., ]*([0-9]{2})";

		for (int i = 0; i < splitString.length; i++) {
			String line = splitString[i];
			System.out.println(line);
			if(line.matches(regex)) {
				list.add(line);
			
			} else {
				if (!line.isEmpty()) {
					System.err.println("\"" + line + "\" not parsed");
					errorDao.add(new ParseError(0, receipt, line));					
				}
			}
		}
		return list;
	}
	
	public static int calculateDifference(String x, String y) {
	    int[][] dp = new int[x.length() + 1][y.length() + 1];
	 
	    for (int i = 0; i <= x.length(); i++) {
	        for (int j = 0; j <= y.length(); j++) {
	            if (i == 0) {
	                dp[i][j] = j;
	            }
	            else if (j == 0) {
	                dp[i][j] = i;
	            }
	            else {
	                dp[i][j] = min(dp[i - 1][j - 1] 
	                 + costOfSubstitution(x.charAt(i - 1), y.charAt(j - 1)), 
	                  dp[i - 1][j] + 1, 
	                  dp[i][j - 1] + 1);
	            }
	        }
	    }
	 
	    return dp[x.length()][y.length()];
	}
	
	private static int costOfSubstitution(char a, char b) {
        return a == b ? 0 : 1;
    }
	
	private static int min(int... numbers) {
        return Arrays.stream(numbers)
          .min().orElse(Integer.MAX_VALUE);
    }
	
	
	
}
