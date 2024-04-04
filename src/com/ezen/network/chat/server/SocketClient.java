package com.ezen.network.chat.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;

/**
 * 채팅 서버에 연결된 클라이언트 소켓을 이용한 1:1 메시지 송수신 스레드
 * 채팅 서버 관점에서 SocketClient는 접속한 클라이언트를 의미한다.
 */
public class SocketClient extends Thread {
    private Socket socket; // 데이터 통신을 위한 소켓
    private DataInputStream in;
    private DataOutputStream out;
    private boolean stop;

    private ChatServer chatServer; // 서버와 연결을 위해 ChatServer 변수 선언
    private String clientIp; // 접속 클라이언트 아이피
    private String nickName; // 접속 클라이언트 대화명

    public SocketClient(Socket socket, ChatServer chatServer) { // 연결된 클라이언트 소켓과 서버를 전달받으면 생성됨
        this.socket = socket;
        this.chatServer = chatServer;
        InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
        clientIp = isa.getAddress().getHostAddress();

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    // 생성 시 소켓과 서버를 전달받음 + 소켓 아이피 설정 + 입출력 스트림 생성

    public String getClientIp() {
        return clientIp;
    }

    public String getNickName() {
        return nickName;
    }

    /**
     * 채팅 클라이언트가 전송한 메세지 수신 및 전송
     */
    private void receiveNsendMessage() {
        String messageDelimiter =  "\\|\\*\\|"; // 정규식
        try {
            while (!stop) {
                String message = in.readUTF();
                // "CONNECT|*|대화명"
                // 디버깅 차원의 클라이언트 파싱되지 않은 메시지 출력
                System.out.println("[채팅 서버] Chat Client -> Chat Server : " + message);

                // 클라이언트 전송한 메시지 파싱(메시지 토큰링)
                String[] tokens = message.split(messageDelimiter); // 정규식을 기준으로 파싱
                // 클라이언트가 전송한 메시지 종류 : CONNECT, DIS_CONNECT, MULTI_CHAT
                // CONNECT|*|클라이언트닉네임
                // MULTI_CHAT|*|클라이언트닉네임|*|채팅메세지
                // DIS_CONNECT|*|클라이언트닉네임
                String command = tokens[0];
                nickName = tokens[1];
                switch (command) {
                    case "CONNECT":
                        chatServer.addSocketClient(this); // 서버에 연결된 클라이언트를 넘겨서 목록에 저장시킴
                        chatServer.sendAllMessage(nickName, "님이 입장하셨습니다.");
                        break;
                    case "MULTI_CHAT":
                        String chatMessage = tokens[2];
                        chatServer.sendAllMessage(nickName, " : " + chatMessage);
                        break;
                    case "DIS_CONNECT":
                        chatServer.sendAllMessage(nickName, "님이 퇴장하셨습니다.");
                        chatServer.removeSocketClient(this);
                        return;
                }
            }
        } catch (IOException e) {

        } finally {
            close();
        }
    }

    /**
     * 현재 연결한 클라이언트에게 메시지 전송 (1:1)
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) {
        try {
            out.writeUTF(message);
            out.flush();
        } catch (IOException e) {
        }
    }

    public void close() {
        try {
            if (socket != null) { // 연결된 소켓이 있다면 닫아줌
                socket.close();
            }
        } catch (IOException e) {
        }
    }

    @Override
    public void run() {
        // 채팅 클라이언트가 전송한 메시지 수신 및 전송
        receiveNsendMessage();
    }
}
