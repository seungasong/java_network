package com.ezen.network;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.Iterator;

public class JSONExample {
    public static void main(String[] args) {
        // JSON 표기법(JavaScript Object Notation)
        // {"키" : "값", "키" : 값}
        String json = "{\"name\" : \"송승아\", \"age\" : 30}";
        System.out.println(json);

        JSONObject root = new JSONObject();
        root.put("id", "bangry");
        root.put("name", "김기정");
        root.put("age", 40);
        root.put("isStudent", false);
        // JSON객체를 생성해서 put 메소드로 키와 값을 하나씩 넣음

        JSONObject tel = new JSONObject();
        tel.put("home", "02-1234-1234");
        tel.put("mobile", "010-1234-1234");

        JSONArray skill = new JSONArray();
        skill.put("Java");
        skill.put("SQL");
        skill.put("JavaSkill");
        // JSON배열을 생성해서 put 메소드로 값을 하나씩 넣음

        root.put("tel", tel); // 루트 객체에 tel 객체 추가
        root.put("skill", skill);

        String myInfo = root.toString(); // 루트 객체 -> JSON 문자열
        System.out.println(myInfo);

        // JSON 문자열 -> JSON 객체로 변환
        JSONObject member = new JSONObject(json);
        String name = member.getString("name");
        int age = member.getInt("age");
        System.out.println(name);
        System.out.println(age);
        // JSONObject 메소드로 파싱하여 객체의 값을 가져옴

        JSONObject member2 = new JSONObject(myInfo);
        System.out.println(member2.getString("id"));
        System.out.println(member2.getString("name"));
        System.out.println(member2.getInt("age"));
        System.out.println(member2.getBoolean("isStudent"));

        JSONObject tel2 = member2.getJSONObject("tel"); // 객체를 가져와서 반환
        System.out.println(tel2.getString("home"));
        System.out.println(tel2.getString("mobile"));
        // 루트 객체에 있는 tel 키의 tel 객체의 값을 파싱하는 것

        JSONArray skills = member2.getJSONArray("skill"); // 배열을 가져와서 반환
        System.out.println(skills.get(0)); // 배열은 키가 아닌 인덱스로 가져옴
        System.out.println(skills.get(1));
        System.out.println(skills.get(2));

        // 반복
        Iterator<Object> iter = skills.iterator();
        while (iter.hasNext()) {
            String ski = (String) iter.next();
            System.out.println(ski);
        }

        // 향상 for문 활용
        for (Object ski : skills) {
            System.out.println(ski);
        }
    }
}
