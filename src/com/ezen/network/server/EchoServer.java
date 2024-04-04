package com.ezen.network.server;

import java.io.*;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP/IP 기반의 서버 구현
 */
public class EchoServer {
    private static final int PORT = 2024; // 식별자
    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws IOException {
        System.out.println("--------------------------------------------------------------");
        System.out.println(" 서버를 종료하려면 q 또는 Q를 입력하고 Enter 키를 입력하세요. ");
        System.out.println("--------------------------------------------------------------");

        // TCP 서버 구동
        startServer();
        // while 반복문으로 인해 서버 구동 메소드가 스레드에서 구동되어야 다른 메소드가 실행될 수 있음

        // 키보드 입력
        Scanner scanner = new Scanner(System.in);
        while (true) { // 키보드 입력도 반복되도록 함
            String commend = scanner.nextLine();
            if (commend.equalsIgnoreCase("q")) { // 대소문자 구분안하고 비교, q나 Q가 입력되면
                break; // while 문 벗어남
            }
        }
        scanner.close();

        // TCP 서버 종료
        stopServer();
    }

    /**
     * 서버 시작(구동)
     */
    public static void startServer() {
        new Thread() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(PORT);
                    System.out.println("[서버] 실행됨");

                    boolean stop = false;
                    while (!stop) {
                        System.out.println("[서버] 클라이언트 연결 요청을 기다림");
                        Socket socket = serverSocket.accept();
                        InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                        String clientIp = isa.getAddress().getHostAddress();
                        // 연결된 클라이언트 소켓을 통해 해당 아이피 얻어오기
                        System.out.println("[서버] 원격 클라이언트(" + clientIp + ") 연결을 해옴");

                        // 연결해 온 클라이언트와 데이터 송수신
                        // 서버 쪽 Socket과 서버와의 입출력 스트림 생성
//                        InputStream in = socket.getInputStream();
//                        OutputStream out = socket.getOutputStream();
                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                        // 클라이언트가 전송한 데이터 읽기
//                        int data = in.read();
//                        byte[] buffer = new byte[1024];
//                        int count = in.read(buffer);
//                        // 문자 디코딩 필요
//                        String receiveMessage = new String(buffer, 0, count);
                        String receiveMessage = in.readUTF();
                        System.out.println("[서버] Client -> Server : " + receiveMessage);

                        // 메세지 에코(Echo) 받은 메세지를 다시 클라이언트에게 보냄
//                        out.write(buffer, 0, count);
                        out.writeUTF(receiveMessage);
                        out.flush();
                        System.out.println("[서버] 수신한 데이터를 다시 보냄");

                        in.close();
                        out.close();
                        // 원격 클라이언트와 연결 끊기(클라이언트와 연결되어 반환된 서버 쪽 소켓을 닫음)
                        socket.close();
                    }

                } catch (IOException e) {
                    System.err.println("[서버] " + e.getMessage());
                }
            }
        }.start();
    }

    /**
     * 서버 종료
     */
    public static void stopServer() {
        try {
            serverSocket.close();
            System.out.println("[서버] 종료됨");
        } catch (IOException e) {}
    }
}
