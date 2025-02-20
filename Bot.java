import java.net.*;
import java.net.http.*;
import java.time.*;
import java.util.*;

public class Bot {
    public static void main(String[] args){
        String webhookUrl = System.getenv("SLACK_WEBHOOK_URL");
        String message = System.getenv("SLACK_WEBHOOK_MSG");

        String aikey = System.getenv("GEMINI_KEY");
        String aiurl = "https://generativelanguage.googleapis.com/v1beta/models/gemini-1.5-flash:generateContent?key="+ aikey;



        HttpClient aiclient = HttpClient.newHttpClient();
        HttpRequest airequest = HttpRequest.newBuilder()
            .uri(URI.create(aiurl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"contents\": [{\"parts\":[{\"text\": \"웃는 표정 이모티콘 하나만 줘\"}]}]}")) 
            .build();

        HttpResponse<String> airesponse = null;
        try{
            HttpResponse<String> airesponse = aiclient.send(
                airequest, HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("요청 코드 : " + airesponse.statusCode());
            System.out.println("응답 결과 : " + airesponse.body());
        }
        catch(Exception e){
            e.printStackTrace();
        }



        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
            .uri(URI.create(webhookUrl))
            .header("Content-Type", "application/json")
            .POST(HttpRequest.BodyPublishers.ofString("{\"text\":\"" + airesponse.body() + "\"}")) 
            .build();

        try{
            HttpResponse<String> response = client.send(
                request, HttpResponse.BodyHandlers.ofString()
            );
            System.out.println("요청 코드 : " + response.statusCode());
            System.out.println("응답 결과 : " + response.body());
        }
        catch(Exception e){
            e.printStackTrace();
        }
    }
}
