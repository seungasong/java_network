package com.ezen.network.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP/IP 기반의 클라이언트 구현
 */
public class EchoClientV2 {
    public static void main(String[] args) throws IOException {
        String severIp = "localhost";
        int port = 2024;
        Socket socket = new Socket(severIp, port);
        System.out.println("[클라이언트] 서버와 연결되었습니다.");

        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        System.out.println("------------------------------------------");
        System.out.println("서버에 전송하고자하는 메세지를 입력하세요.");
        System.out.println("종료하려면 q 또는 Q를 입력하세요.");
        System.out.println("------------------------------------------");

        Scanner scanner = new Scanner(System.in);
        boolean stop = false;
        while (!stop) {
            String inputMessage = scanner.nextLine();
            if (inputMessage.equalsIgnoreCase("q")) {
                // 서버에 연결 종료 메세지 전송
                out.writeUTF("exit");
                out.flush();
                break;
            }

            out.writeUTF(inputMessage);
            out.flush();

            String receiveMessage = in.readUTF();
            System.out.println("[클라이언트] Server -> Client : " + receiveMessage);
        }

        scanner.close();
        out.close();
        in.close();
        socket.close();
        System.out.println("[클라이언트] 서버와 연결을 종료합니다.");
    }
}
