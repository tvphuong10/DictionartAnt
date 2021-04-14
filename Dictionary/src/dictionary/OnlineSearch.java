/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dictionary;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 *
 * @author Yorovy
 */
public class OnlineSearch {
    private static final String URL = "https://script.google.com/macros/s/AKfycbwbI3qUTz0rVD7cGJCP79iwLqMu7kJZQ9BYaY0bcfFMeAUMLl4/exec";
    
    public static String translate(String fromLang, String toLang, String text) {
        try {
            String urlStr = URL +
                "?q=" + URLEncoder.encode(text, "UTF-8") +
                "&target=" + toLang +
                "&source=" + fromLang;

            URL url = new URL(urlStr);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setDoOutput(true);
            conn.setRequestProperty("User-Agent", "Mozilla/5.0");
            
            StringBuilder output = new StringBuilder();
            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
                String line;
                while ((line = in.readLine()) != null) {
                    output.append(line).append("\n");
                }
            } catch (Exception e) {
                System.out.println("lỗi đọan lấy kết quả ông ơii");
                return "!";
            }
            return output.toString();

        } catch (IOException e) {
            System.out.println("kết nối hỏng rồi ông ơi");
            return "!";
        }
    }
}
