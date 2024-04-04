package com.ezen.network.chat.server;

import com.ezen.network.chat.client.ChatClient;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Scanner;

/**
 * TCP/IP 기반의 멀티 채팅 서버 구현
 * 멀티 스레드 기반 - 클라이언트 다중 요청 처리 O
 */
public class ChatServer {
    private int port; // 클래스로 재사용화를 위해 상수 -> 인스턴스 변수
    private ServerSocket serverSocket;
    boolean stop; // 스코프 넓힘

    // 연결된 클라이언트를 저장할 리스트 변수 선언
    private HashMap<String, SocketClient> socketClients;

    public ChatServer() {
        this(2024); // 아래 생성자를 호출하여 매개변수에 디폴트 포트번호 2024 할당
        socketClients = new HashMap<>();
    }

    public ChatServer(int port) {
        this.port = port;
        socketClients = new HashMap<>();
    }

    public int getPort() {
        return port;
    }

    public ServerSocket getServerSocket() {
        return serverSocket;
    }

    /**
     * 서버 구동
     */
    public void start() {
        new Thread() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(port);
                    System.out.println("########## [채팅 서버(" + port + ")] 실행됨 ##########");
                    while (!stop) {
                        System.out.println("[채팅 서버] 채팅 클라이언트 연결 요청을 기다림");
                        Socket socket = serverSocket.accept();
                        InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
                        String clientIp = isa.getAddress().getHostAddress();
                        System.out.println("[채팅 서버] 채팅 클라이언트(" + clientIp + ") 연결을 해옴");

                        // 생성된 소켓을 가지고 메세지를 송수신하는 스레드 생성 및 실행
                        // 따로 SocketClient라는 클래스를 만들어 관리
                        SocketClient socketClient = new SocketClient(socket, ChatServer.this);
                        socketClient.start();
                    }
                } catch (IOException e) {
                    System.err.println("[서버] " + e.getMessage());
                }
            }
        }.start();
    }

    /**
     * 채팅 서버에 클라이언트(SocketClient) 연결 시 Map에 클라이언트 추가
     * @param socketClient
     */
    public void addSocketClient(SocketClient socketClient) {
        // key = 사용자닉네임@클라이언트아이피
        String key = socketClient.getNickName() + "@" + socketClient.getClientIp();
        socketClients.put(key, socketClient);
        System.out.println("※ 클라이언트 입장 " + key);
        System.out.println("※ 현재 채팅 서버에 연결된 클라이언트 수 : " + socketClients.size());
    }

    /**
     * 채팅 서버에 연결된 모든 클라이언트(SocketClient)들에게 메시지 전송
     * @param senderName, message
     */
    public void sendAllMessage(String senderName, String message) throws IOException {
        // 현재 채팅 서버에 연결된 모든 클라이언트 목록
        Collection<SocketClient> connectionList = socketClients.values(); // 리스트에 저장된 모든 값
        for (SocketClient socketClient : connectionList) {
            socketClient.sendMessage("[" + senderName + "]" + message);
        }
        // 리스트에 저장된 모든 값(전체 클라이언트 수)이 반복될 떄 까지 for문 실행
        // 서버와 연결된 클라이언트 소켓인 socketClient의 sendMessage 메소드를 사용해 전체 클라이언트에게 메세지 전송
    }

    /**
     * 클라이언트(SocketClient) 연결 종료 시 Map에서 클라이언트 제거
     * @param socketClient
     */
    public void removeSocketClient(SocketClient socketClient){
        // key = 사용자닉네임@클라이언트아이피
        String key = socketClient.getNickName() + "@" + socketClient.getClientIp();
        socketClients.remove(key);
        System.out.println("▣ 클라이언트 퇴장 : " + key);
        System.out.println("▣ 현재 채팅서버에 연결된 클라이언트 수 : " + socketClients.size());
    }

    /**
     * 채팅 서버 종료
     */
    public void stop(){
        try {
            serverSocket.close();
            for (SocketClient sc : socketClients.values()) {
                if(sc != null) { // 연결된 클라이언트의 소켓이 있다면 닫아줌
                    sc.close();
                }
            }
            System.out.println("[채팅서버] 종료됨.");
        } catch (IOException e) { }
    }
}
