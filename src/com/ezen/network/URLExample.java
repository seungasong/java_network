package com.ezen.network;

import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;

public class URLExample {
    public static void main(String[] args) {
        // 내 컴퓨터의 리소스를 가리키는 형식(Path)
        // String path = "C://xxx/yyy/some.txt"
        // File file = new File(path);

        // 다른 컴퓨터의 특정 리소스를 가리키는 형식(URL)
        String str = "https://www.naver.com"; // http 프로토콜 + 아이피
        String image = "https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png";

        // 인터넷 상의 특정 URL의 정보 얻기
        InputStream in = null;
        try {
            // URL 관련 정보 얻기
            URL url = new URL(str);
            String protocol = url.getProtocol(); // 해당 url의 프로토콜을 문자열로 반환
            String host = url.getHost(); // 해당 url의 아이피를 문자열로 반환
            int port = url.getPort(); // 해당 url의 포트번호를 정수로 반환
            
            System.out.println("protocol = " + protocol);
            System.out.println("host = " + host);
            System.out.println("port = " + port); // -1이면 해당 url에 포트 번호가 없다는 뜻

            // URL 상의 리소스 읽어오기
            in = url.openStream(); // 해당 url의 입력 스트림 생성
            int data = in.read(); // 하나씩 읽어옴
            System.out.println("data = " + data);

            byte[] buffer = new byte[1024]; // 1024는 가장 적절한 네트워크 상 자원 얻어올 때의 배열 크기
            int count = 0;
            while ((count = in.read(buffer)) != -1) { // 배열로 여러 개 읽어옴
                String html = new String(buffer, 0, count); // 읽어온 리소스가 html 정보임
                // 스트링 객체가 0에서 count까지 buffer에 저장된 데이터(html)를 하나씩 디코딩하여 읽어옴
                // 읽어오는 것이므로 출력할 때처럼 한줄씩 읽어오지 않아도됨, 저장된 데이터를 모두 갖고오기만 하면 됨
                System.out.print(html);
            }

            // 문자 스트림으로 여러 개 읽어옴
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(in, "utf-8"));
            String html = null;
            while ((html = bufferedReader.readLine()) != null) {
                System.out.println(html);
            }

        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } finally {
            try {
                in.close();
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
