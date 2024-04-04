package com.ezen.network.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Scanner;

/**
 * TCP/IP 기반의 에코 서버 구현
 * 싱글 스레드 기반 - 클라이언트 다중 요청 처리 X
 */
public class EchoServerV2 {
    private static final int PORT = 2024;
    private static ServerSocket serverSocket = null;

    public static void main(String[] args) throws IOException {
        System.out.println("--------------------------------------------------------------");
        System.out.println(" 서버를 종료하려면 q 또는 Q를 입력하고 Enter 키를 입력하세요. ");
        System.out.println("--------------------------------------------------------------");

        startServer();

        Scanner scanner = new Scanner(System.in);
        while (true) {
            String commend = scanner.nextLine();
            if (commend.equalsIgnoreCase("q")) {
                break;
            }
        }
        scanner.close();

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
                        System.out.println("[서버] 원격 클라이언트(" + clientIp + ") 연결을 해옴");

                        DataInputStream in = new DataInputStream(socket.getInputStream());
                        DataOutputStream out = new DataOutputStream(socket.getOutputStream());

                        // 클라이언트에서 여러 메세지를 수신하고 재송신
                        boolean closed = false;
                        while (!closed) {
                            String receiveMessage = in.readUTF();
                            System.out.println("[서버] Client -> Server : " + receiveMessage);

                            // 클라이언트 연결 종료 메세지인 경우 while문 벗어남
                            if (receiveMessage.equalsIgnoreCase("exit")) {
                                break;
                            }

                            out.writeUTF(receiveMessage);
                            out.flush();
                            System.out.println("[서버] 수신한 데이터를 다시 보냄");
                        }

                        in.close();
                        out.close();
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
