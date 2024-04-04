package com.ezen.network.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * TCP/IP 기반의 서버 구현
 */
public class ServerSocketExample {
    public static void main(String[] args) throws IOException {
        System.out.println("[서버] 실행됨");
        // 네트워크에 연결된 원격의 클라이언트 연결을 수신하기 위해 ServerSocket 생성
        int port = 2024;
        ServerSocket serverSocket = new ServerSocket(port); // 해당 포트 번호의 포트를 가진 서버 소켓을 생성함
//        ServerSocket serverSocket = new ServerSocket();
//        serverSocket.bind(new InetSocketAddress(port)); // 랜카드가 여러 개라 특정 IP를 지정할 경우에 사용하는 방법

        boolean stop = false;
        // 멀티 클라이언트의 연결을 수신하기 위해 Loop
        while (!stop) {
            Socket socket = serverSocket.accept(); // 블락메소드(연결이 될 때까지 멈춰있음, 연결되면 연결된 Socket 반환)
            InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
            // 해당 메소드의 반환타입은 SocketAddress인데 이 클래스는 추상클래스이므로
            // 실제로는 구현클래스인 InetSocketAddress 타입으로 반환되어야 한다. 따라서 강제 형변환을 해줘야 함.
            // 연결된 Socket의 아이피 정보를 담은 객체를 반환해주는 getRemoteSocketAddress() 메소드
            // InetSocketAddress 클래스는 아이피와 포트 번호 정보 관련 클래스이다.
            String clientIp = isa.getAddress().getHostAddress();
            // getAddress는 아이피를 바이트형태로 반환, 바이트형태의 아이피를 getHostAddress가 문자열형태로 변환
//            int clientPort = isa.getPort();
            System.out.println("원격 클라이언트(" + clientIp + ") 연결을 해옴");

            // 연결해 온 클라이언트와 데이터 송수신
        }
        serverSocket.close();
        System.out.println("[서버] 종료됨");
    }
}
