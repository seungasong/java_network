package com.ezen.network.chat.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Scanner;

/**
 * 채팅 클라이언트 실행 클래스
 */
public class EzenTalk {
    public static void main(String[] args) {
        ChatClient chatClient = new ChatClient();
//        ChatClient chatClient = new ChatClient("192.168.0.69", 2024);
//        ChatClient chatClient = new ChatClient("192.168.0.38", 2024);

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
            chatClient.sendMessage("CONNECT|*|" + nickName);
            // 서버에서 대화명인지 채팅내용인지 구분할 수 있게끔 구분자를 등록함

            System.out.println("------------------------------------------");
            System.out.println("서버에 전송하고자하는 메세지를 입력하세요.");
            System.out.println("종료하려면 q 또는 Q를 입력하세요.");
            System.out.println("------------------------------------------");

            // 서버 연결 완료 후 서버로부터 전송되는 메세지 수신을 위한 스레드 생성 및 시작
            chatClient.receive();

            // 클라이언트 실행 클래스에서 구분자 포함하여 서버에 메세지 전송
            boolean stop = false;
            while(!stop) {
                String inputMessage = scanner.nextLine();
                if (inputMessage.equalsIgnoreCase("q")) {
                    // 서버에 연결 종료메시지 전송
                    chatClient.sendMessage("DIS_CONNECT|*|" + nickName);
                    break;
                }else {
                    // 종료 메세지가 아닐 때 입력 메세지 전송
                    chatClient.sendMessage("MULTI_CHAT|*|" + nickName + "|*|" + inputMessage);
                }
            }
        } catch (IOException e) {
            System.err.println("네트워크 상태를 확인하여 주세요.");
        } finally {
            try {
                scanner.close();
                chatClient.unConnect(); // 서버에 연결된 소켓과 연결 종료
            } catch (IOException e) { }
        }
    }
}
