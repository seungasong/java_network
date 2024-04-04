package com.ezen.network.chat.serverv2;

import org.json.JSONObject;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutorService;

/**
 * 채팅 서버에 연결한 클라이언트와 소켓을 이용한 1:1 메시지 송수신
 * 채팅 서버 관점에서 SocketClient는 접속한 클라이언트를 의미한다.
 */
public class SocketClient {
    private ChatServer chatServer;

    private Socket socket;
    private DataInputStream in;
    private DataOutputStream out;
    private boolean stop;

    private String clientIp;
    private String nickName;

    public SocketClient(Socket socket, ChatServer chatServer) {
        this.socket = socket;
        this.chatServer = chatServer;
        InetSocketAddress isa = (InetSocketAddress) socket.getRemoteSocketAddress();
        clientIp = isa.getAddress().getHostAddress();

        try {
            in = new DataInputStream(socket.getInputStream());
            out = new DataOutputStream(socket.getOutputStream());
            receiveNsendMessage();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

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
        // 스레드풀에 관리되고 있는 스레드를 통해 실행할 작업
        // ChatServer 클래스의 스레드풀을 사용함
        ExecutorService threadPool = chatServer.getThreadPool();
        threadPool.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!stop) {
                        String jsonMessage = in.readUTF();
                        // 디버깅 차원으로 파싱되지 않은 클라이언트 메시지 출력
                        System.out.println("[채팅 서버] Chat Client -> Chat Server : " + jsonMessage);

                        // 클라이언트가 전송한 JSON 파싱
                        JSONObject jsonObject = new JSONObject(jsonMessage); // 문자열 -> 객체, 객체를 통해 파싱
                        String command = jsonObject.getString("command");
                        nickName = jsonObject.getString("nickName");
                        switch (command) {
                            case "CONNECT":
                                chatServer.addSocketClient(SocketClient.this);
                                chatServer.sendAllMessage(nickName, "님이 입장하셨습니다.");
                                break;
                            case "MULTI_CHAT":
                                String chatMessage = jsonObject.getString("message");
                                chatServer.sendAllMessage(nickName, " : " + chatMessage);
                                break;
                            case "DIS_CONNECT":
                                chatServer.sendAllMessage(nickName, "님이 퇴장하셨습니다.");
                                chatServer.removeSocketClient(SocketClient.this);
                                return;
                        }
                    }
                } catch (IOException e) {

                } finally {
                    close();
                }
            }
        });
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
            if (socket != null) {
                socket.close();
            }
        } catch (IOException e) {
        }
    }
}
