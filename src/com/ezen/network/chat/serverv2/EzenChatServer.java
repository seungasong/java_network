package com.ezen.network.chat.serverv2;

import java.util.Scanner;

/**
 * 채팅 서버 실행 클래스
 */
public class EzenChatServer {
    public static void main(String[] args) {
        ChatServer chatServer = new ChatServer();
//        ChatServer chatServer = new ChatServer(7777);
        chatServer.start();

        System.out.println("--------------------------------------------------------------");
        System.out.println(" 서버를 종료하려면 q 또는 Q를 입력하고 Enter 키를 입력하세요. ");
        System.out.println("--------------------------------------------------------------");

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String commend = scanner.nextLine();
            if (commend.equalsIgnoreCase("q")) {
                break;
            }
        }
        scanner.close();

        chatServer.stop();
    }
}
