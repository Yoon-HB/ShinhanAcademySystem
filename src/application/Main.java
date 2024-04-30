package application;

import java.util.Scanner;

public class Main {
	public static void main(String[] args) {
		User user;
		Scanner scanner = new Scanner(System.in);
		String option;
		String id;
		String pw;
		while (true) {
			System.out.println("----------------------------------------");
			System.out.println("1. 학생 로그인 | 2. 강사 로그인 | q. 프로그램 종료");
			System.out.println("----------------------------------------");
			System.out.print("선택> ");
			option = scanner.nextLine();
			if ("1".equals(option)) { // 학생 로그인
				user = new Student();
				System.out.println("-------------------------");
				System.out.print("ID> ");
				id = scanner.nextLine();
				System.out.print("PW> ");
				pw = scanner.nextLine();
				user.login(id, pw);
				System.out.println("\n");

				user.run();
			}

			if ("2".equals(option)) { // 강사 로그인
				user = new Teacher();
				System.out.println("-------------------------");
				System.out.print("ID> ");
				id = scanner.nextLine();
				System.out.print("PW> ");
				pw = scanner.nextLine();
				user.login(id, pw);
				System.out.println("\n");

				user.run();
			}

			if ("q".equals(option)) {
				System.out.println("포로그램 종료");
				break;
			}
		}
	}

}
