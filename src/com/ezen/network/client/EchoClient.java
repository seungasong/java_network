package com.ezen.network.client;

import java.io.*;
import java.net.Socket;

/**
 * TCP/IP 기반의 클라이언트 구현
 */
public class EchoClient {
    public static void main(String[] args) throws IOException {
        // ServerSocket에 연결하기 위해 Socket 생성
//        String severIp = "192.168.0.38";
        String severIp = "localhost"; // 내 컴퓨터 아이피를 나타내는 별칭
//        String severIp = "127.0.0.1"; // 내 컴퓨터 아이피를 나타내는 가상 아이피
        int port = 2024;

        // ip(내 컴퓨터 아이피), port(통신하고자 하는 포트 번호)를 이용하여 ServerSocket 연결
        Socket socket = new Socket(severIp, port); // 매개변수 없이 connect 메소드 호출을 통해 연결할 수도 있다.
        // Domain Name으로 ServerSocket 연결
//        Socket socket1 = new Socket(new InetAddress.getByName("www.bangry.com", 2024));
        System.out.println("[클라이언트] 서버와 연결되었습니다.");

        // 서버와 데이터 송수신
        // 클라이언트와 클라이언트 쪽 Socket과의 입출력 스트림 생성
//        InputStream in = socket.getInputStream();
//        OutputStream out = socket.getOutputStream();
        DataOutputStream out = new DataOutputStream(socket.getOutputStream());
        DataInputStream in = new DataInputStream(socket.getInputStream());

        // 서버 내 Socket에 데이터 전송
        String sendMessage = "즐거운 하루 되세요";
//        byte[] bytes = sendMessage.getBytes("UTF-8"); // 문자 인코딩
//        out.write(10);
//        out.write(bytes);
//        out.flush();
        out.writeUTF(sendMessage);
        out.flush();

        // 서버 내 Socket에서 데이터 수신
//        byte[] buffer = new byte[1024];
//        int count = in.read(buffer);
//        String receiveMessage = new String(buffer, 0, count);
        String receiveMessage = in.readUTF();
        System.out.println("[클라이언트] Server -> Client : " + receiveMessage);

        out.close();
        in.close();
        // 클라이언트 연결 종료
        socket.close();

        // 클라이언트(출력) -> 서버(입력), 서버(출력) -> 클라이언트(입력)
        // 서버와 클라이언트에 모두 입출력 스트림이 있는 경우 위의 루틴으로 양방향 통신이 가능하다.
        // 둘 중에 하나라도 입력 또는 출력 스트림이 없다면 단방향 통신만 가능함.
    }
}
