package managers;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class SessionManager {

    private static Map<String, String> activeTokens = new HashMap<>();
    private static String lastGeneratedToken = ""; 
    
    // ========================================================
    // ===== CHANGE FIRST LETTER OF A STRING TO UPPERCASE =====
    // ========================================================
    
    public static String firstUpperCase(String str) {
    	return Character.toUpperCase(str.charAt(0)) + str.substring(1);
    }

    // ==================================
    // ===== CREATE A SESSION TOKEN =====
    // ==================================
    
    public static String createToken(String userId) {
    	// Generating a new token
        String token = generateUniqueToken();
        // To be sure the generated token isn't equal to any token already existing
        while (token.equals(lastGeneratedToken) || activeTokens.containsValue(token)) {
            token = generateUniqueToken();
        }
        // Put the new token is the HashMap, with it's id
        activeTokens.put(token, userId);
        // Return last generated token for the connected user
        lastGeneratedToken = token;
        return token;
    }

    // ===================================================
    // ===== VERIFY IS A TOKEN IS VALID AND IT'S KEY =====
    // ===================================================

    public static boolean isTokenValid(String userId, String token) {
        return activeTokens.containsKey(token) && activeTokens.get(token).equals(userId);
    }

    // ====================================
    // ===== GENERATE A SESSION TOKEN =====
    // ====================================

	private static String generateUniqueToken() {
		
		// Getting random value generator
        Random random = new Random();
        
        // Generating a random integer between 1 and 99999
        int randomInt = random.nextInt(99999) + 1;

        // Converting the integer to a string
        String token = String.valueOf(randomInt);

        // Adding 10 random letters at random positions
        for (int i = 0; i < 10; i++) {
            char randomLetter = (char) ('a' + random.nextInt(26));
            int position = random.nextInt(token.length() + 1);
            token = token.substring(0, position) + randomLetter + token.substring(position);
        }
        
        return token; // Returning the new token
    }
}
