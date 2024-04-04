package com.ezen.network.chat.clientv2;

import org.json.JSONObject;

import java.io.IOException;
import java.util.Scanner;

/**
 * 채팅 클라이언트 실행 클래스
 */
public class EzenTalk {
    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
//        ChatClient chatClient = new ChatClient("192.168.0.38", 2024);
//        ChatClient chatClient = new ChatClient("192.168.0.69", 2024);
        try {
            chatClient.connect();
        } catch (IOException e) {
            System.err.println("[채팅 서버]에 연결할 수 없습니다.");
            System.err.println("네트워크 상태를 확인해주세요.");
            return;
        }

        Scanner scanner = new Scanner(System.in);
        try {
            System.out.print("대화명 입력 : ");
            String nickName = scanner.nextLine();
            chatClient.setNickName(nickName);
            // 서버에 연결 메세지 전송
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("command", "CONNECT");
            jsonObject.put("nickName", nickName);
            chatClient.sendMessage(jsonObject.toString());

            System.out.println("------------------------------------------");
            System.out.println("서버에 전송하고자하는 메세지를 입력하세요.");
            System.out.println("종료하려면 q 또는 Q를 입력하세요.");
            System.out.println("------------------------------------------");

            chatClient.receive();

            boolean stop = false;
            while(!stop) {
                String inputMessage = scanner.nextLine();
                if(inputMessage.equalsIgnoreCase("q")){
                    // 서버에 연결 종료 메세지 전송
                    jsonObject.put("command", "DIS_CONNECT");
                    jsonObject.put("nickName", nickName);
                    chatClient.sendMessage(jsonObject.toString());
                    break;
                }else {
                    // 서버에 입력 메세지 전송
                    jsonObject.put("command", "MULTI_CHAT");
                    jsonObject.put("nickName", nickName);
                    jsonObject.put("message", inputMessage);
                    chatClient.sendMessage(jsonObject.toString());
                }
            }
        } catch (IOException e) {
            System.err.println("네트워크 상태를 확인하여 주세요.");
        } finally {
            try {
                scanner.close();
                chatClient.unConnect();
            } catch (IOException e) { }
        }
    }
}
