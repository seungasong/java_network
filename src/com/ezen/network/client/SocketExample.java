package com.ezen.network.client;

import java.io.IOException;
import java.net.Socket;

/**
 * TCP/IP 기반의 클라이언트 구현
 */
public class SocketExample {
    public static void main(String[] args) throws IOException {
        // ServerSocket에 연결하기 위해 Socket 생성
//        String severIp = "192.168.0.38";
        String severIp = "localhost"; // 내 컴퓨터 아이피를 나타내는 별칭
//        String severIp = "127.0.0.1"; // 내 컴퓨터 아이피를 나타내는 가상 아이피

        // ip(내 컴퓨터 아이피), port(통신하고자 하는 포트 번호)를 이용하여 ServerSocket 연결
        int port = 2024;
        Socket socket = new Socket(severIp, port);
        System.out.println("[클라이언트] 서버와 연결되었습니다.");

        // 서버와 데이터 송수신

        // 연결 해제
        socket.close();
    }
}
