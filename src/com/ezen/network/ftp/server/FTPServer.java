package com.ezen.network.ftp.server;

import java.io.IOException;
import java.net.*;

/**
 * FTP Server
 * TCP/IP(Socket) 기반의 바이트 스트림을 이용한 파일 다운로드 서비스 제공
 */
public class FTPServer {

	private static final int PORT = 2024;
	private boolean stop;
	
	private ServerSocket serverSocket;

	public ServerSocket getServerSocket() {
		return serverSocket;
	}
	
	/** 서버 구동 */
	public void start() throws IOException{
		serverSocket = new ServerSocket(PORT);
		System.out.println("FTPServer[" + PORT + "] Startup.....");
		while(!stop){
			Socket socket = serverSocket.accept();
			InetSocketAddress socketAddress = (InetSocketAddress) socket.getRemoteSocketAddress();
			String clientIp = socketAddress.getAddress().getHostAddress();
			System.out.println("FTP Client[" + clientIp + "] Connected...");
			FTPTask task = new FTPTask(socket);
			Thread thread = new Thread(task);
			thread.start();
		}		
	}
	
	/** 서버 종료 */
	public void stop() throws IOException{
		if(serverSocket != null) serverSocket.close();
	}
	
	public static void main(String[] args) {
		// 서버 생성
		FTPServer server = new FTPServer();
		try {
			// 서버 실행
			server.start();
		} catch (IOException e) {
			System.err.println("FTP 서버 실행 중 아래와 같은 오류가 발생하였습니다");
			System.err.println(e.getMessage());
		}
	}
}
