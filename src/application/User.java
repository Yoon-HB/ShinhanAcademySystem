package application;

import java.util.Scanner;

import lombok.Data;
import util.database.DbConnect;
@Data
public class User {
	String id = null;
	String name = null;
	Scanner sc = new Scanner(System.in);
	DbConnect dc = new DbConnect();

	public void login(String id, String pw) {
		System.out.println("로그인");
	};


	public void logout() {
		System.out.println("로그아웃");
		id = null;
		name = null;
	};

	public void listTest() {
		System.out.println("성적 조회");
	};

	public void run() {
	};
}