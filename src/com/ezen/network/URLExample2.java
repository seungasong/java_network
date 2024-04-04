package com.ezen.network;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class URLExample2 {
    public static void main(String[] args) {
        // URL을 통해 이미지를 내 컴퓨터에 저장하기(복사)
        String image = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";
        InputStream in = null;
        OutputStream out = null;
        try {
            URL url = new URL(image);
            in = url.openStream(); // URL 객체를 통해 입력 스트림 생성
            out = new FileOutputStream("google.png");
            // 출력 스트림 생성, 현재 작업 디렉토리에 google.png라는 이름으로 저장
            byte[] buffer = new byte[1024]; // 파일의 모든 정보를 입력할 바이트 생성
            int count = 0;
            while ((count = in.read(buffer)) != -1) { // 파일을 모두 입력할 때까지 실행
                out.write(buffer, 0, count); // 파일을 모두 입력할 때까지 출력함
                // 읽어들인 바이트 배열을 내 컴퓨터의 특정 디렉토리에 저장(다운로드)
            }
            System.out.println("다운로드 완료");

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
                out.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
