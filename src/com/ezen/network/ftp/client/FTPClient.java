package com.ezen.network.ftp.client;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.Scanner;

/**
 * FTP Client
 * TCP/IP(Socket) 기반의 바이트 스트림을 이용하여 파일 다운로드
 */
public class FTPClient {

	private static final String SERVER_IP = "localhost";
//	private static final String SERVER_IP = "192.168.0.38";
	private static final int SERVER_PORT = 2024;
	
	private Socket socket;
	private DataInputStream in;
	private DataOutputStream out;
	private boolean stop;

	// setter/getter 메소드
	public Socket getSocket() {
		return socket;
	}
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public DataInputStream getIn() {
		return in;
	}
	public void setIn(DataInputStream in) {
		this.in = in;
	}
	public DataOutputStream getOut() {
		return out;
	}
	public void setOut(DataOutputStream out) {
		this.out = out;
	}
	
	/** 서버 연결 */
	public void connect() throws UnknownHostException, IOException{
		socket = new Socket(SERVER_IP, SERVER_PORT);
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
	}
	
	/** 서버 연결 끊기 */
	public void disConnect(){
		try {
			if(in != null) in.close();
			if(out != null)	out.close();
			if(socket != null) socket.close();
		} catch (IOException e) {}
	}
	
	/** 파일 다운로드 */
	public void fileDownload() throws IOException {
		// #1. 서버로부터 파일 목록 수신
		// 파일명1, 파일명2, ...., 파일명n 형식의 텍스트 수신(CSV 텍스트)
		String fileList = in.readUTF();
		
		System.out.println("□□□□□□□□□□ 다운로드 가능한 파일 목록 □□□□□□□□□□");
		String[] fileNames = fileList.split(",");
		for (String fileName : fileNames) {
			System.out.println("  ▶ " + fileName);
		}
		System.out.println("-----------------------------------------------");
		// 수신한 파일 목록을 ,로 분리하여 콘솔창에 출력
		
		// #2. 다운로드하고자 하는 파일명 입력
		System.out.print(" ☞ 다운로드 파일명 입력 : ");
		Scanner scanner = new Scanner(System.in);
		String downFileName = scanner.nextLine();
		scanner.close();
		
		// #3. 다운로드 파일명 서버에 전송
		out.writeUTF(downFileName);
					
		// #4.다운로드하고자 하는 파일 사이즈 수신
		long downloadFileSize = in.readLong();
		
		// #5. 바이트 스트림을 이용한 파일 다운로드
		String saveDirectoryName = "C:/ezen-fullstack/downloads";
		File saveDirectory = new File(saveDirectoryName);
		if(!saveDirectory.exists()){
			saveDirectory.mkdir();
		}
		// 다운로드 파일을 저장하기 위한 바이트 출력 스트림 생성
		FileOutputStream fos = new FileOutputStream(saveDirectoryName + "/" + downFileName);
		
		byte[] buffer = new byte[1024];
		int count = 0;
		int copyRate = 0;
		double copySize = 0;

		try{
			while((count = in.read(buffer)) != -1){
				fos.write(buffer, 0, count);
				copySize += count;
				copyRate = (int) ((copySize/downloadFileSize) * 100);
//				System.out.println(copyRate + "% 다운로드");
				System.out.print("▶ ");
			}
			System.out.println();
			System.out.println("★★★★★ " + downFileName +" 파일 다운로드 완료 ★★★★★");
		}finally {
			if(fos != null) fos.close();
			disConnect();
		}
	}
	
	public static void main(String[] args) throws UnknownHostException {
		FTPClient ftpClient = new FTPClient();
		try {
			ftpClient.connect();
			System.out.println("FTPServer Connected.....");
			ftpClient.fileDownload();
		} catch (IOException e) {
			System.err.println("아래와 같은 오류가 발생하여 FTP Server와 연결할 수 없습니다.");
			System.err.println(e.toString());
		} finally {
			ftpClient.disConnect();
		}
	}
}
