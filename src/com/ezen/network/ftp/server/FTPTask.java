package com.ezen.network.ftp.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * FTP 파일 다운로드 서비스
 */
public class FTPTask implements Runnable {
	
	// 서버 파일 저장 위치
	private String fileRepository;
	
	private Socket socket;
	private DataOutputStream out;
	private DataInputStream in;
	
	public FTPTask(Socket socket) throws IOException{
		this.socket = socket;
		out = new DataOutputStream(socket.getOutputStream());
		in = new DataInputStream(socket.getInputStream());
		// 사용자 홈디렉토리의 Downloads 디렉토리를 파일 저장 위치로 설정
		fileRepository = System.getProperty("user.home") + "/Downloads";
	}

	// 연결된 클라이언트에게 파일 다운로드 서비스
	private void service() throws IOException{
		// #1. 서버 파일 저장 디렉토리의 파일 목록 전송
		// 파일명1, 파일명2, ...., 파일명n 형식의 텍스트 전송(CSV 텍스트 = 콤마로 구분하는 텍스트)
		File directory = new File(fileRepository);
		// 파일 저장 디렉토리가 없을 경우 디렉토리 생성
		if(!directory.exists()){
			directory.mkdir();
		}
		
		File[] listFiles = directory.listFiles();
		StringBuilder sb = new StringBuilder();
		for (File file : listFiles) {
			String fileName = file.getName();
			long fileSize = file.length()/1024;
			sb.append(fileName + " [" + fileSize + "KB],");
			// 파일 목록이 문자열로 스트링빌더에 추가됨
		}
		// 클라이언트에게 파일목록 출력
		out.writeUTF(sb.toString());

		// #2. 클라이언트가 다운로드 받고자 하는 파일명 수신
		String downloadFileName = in.readUTF();
		
		// #3. 클라이언트에게 파일 전송
		sendFile(downloadFileName);
	}
	
	private void sendFile(String fileName) throws IOException{
		File file = new File(fileRepository, fileName);
		// 다운로드 파일 용량
		long fileSize = file.length();
		
		// #1. 파일 용량 출력 -> 클라이언트 파일 다운로드 진행 상황 출력 시 필요
		out.writeLong(fileSize);
		
		// #2. 바이트 스트림으로 파일 전송
		FileInputStream fin = null;
		byte[] buffer = new byte[1024];
		int count = 0;
		try{
			fin = new FileInputStream(file);
			while((count = fin.read(buffer)) != -1){
				out.write(buffer, 0, count);
			}
		} finally {
			if(fin != null) fin.close();
			if(out != null) out.close();
		}
	}
	
	public void run(){
		try {
			service();
		} catch (IOException e) {
			System.out.println("네트워크 오류로 서비스를 제공할 수 없습니다.");
		} finally {
			try {
				if(socket != null) socket.close();
			} catch(IOException e) {}
		}
	}
}
