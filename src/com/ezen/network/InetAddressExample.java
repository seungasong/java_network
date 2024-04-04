package com.ezen.network;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class InetAddressExample {
    public static void main(String[] args) throws UnknownHostException {
        // 내컴퓨터의 IP 주소 동적으로 얻기
        InetAddress inetAddress = InetAddress.getLocalHost(); // 싱글톤 패턴, InetAddress 객체 반환
        String localIp = inetAddress.getHostAddress(); // InetAddress 객체를 통해 내 컴퓨터의 아이피를 얻음
        System.out.println("localIp = " + localIp);

        // 네이버 서버의 아이피 얻기
//        String domainName = "www.naver.com";
        String domainName = "www.google.com";
        InetAddress remoteIp = InetAddress.getByName(domainName); // DNS와 통신하여 도메인명을 통해 등록된 아이피를 '하나' 얻어옴
        System.out.println("remoteIp = " + remoteIp.getHostAddress());

        InetAddress[] inetAddresses = InetAddress.getAllByName(domainName); // 등록된 아이피를 '여러 개' 얻어옴
        for (InetAddress address : inetAddresses) {
            System.out.println("address = " + address.toString());
        }
    }
}
