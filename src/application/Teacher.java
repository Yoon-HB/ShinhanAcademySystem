package application;

import java.sql.PreparedStatement;
import java.sql.SQLException;

import lombok.Data;

@Data
public class Teacher extends User {

	public Teacher() {
	}

	public void login(String id, String pw) {
		String sql = "SELECT * FROM teacher WHERE tc_id = ? AND tc_pw = ?";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			dc.rs = dc.execute(pstmt);
			if (dc.rs.next()) {
				this.id = dc.rs.getString("tc_id");
				this.name = dc.rs.getString("tc_name");
			} else { // 로그인 실패
				System.out.println("아이디나 패스워드를 확인하세요");
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 로그아웃
	public void logout() {
		dc.close();
		id = null;
		name = null;
	}

	public void insertscore() { // 시험 성적 입력
		Test test = new Test();
		System.out.println("[시험 성적 입력]");
		System.out.print("학생 ID: ");
		test.setStd_id(sc.nextLine());
		System.out.print("테스트명: ");
		test.setTest_name(sc.nextLine());
		System.out.print("시험 점수 입력: ");
		test.setScore(sc.nextInt());
		sc.nextLine();

		String sql = "" + "INSERT INTO test (test_id, std_id, test_name, score) " + "VALUES(SEQ_TID.NEXTVAL, ?, ?, ?)";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, test.getStd_id());
			pstmt.setString(2, test.getTest_name());
			pstmt.setInt(3, test.getScore());
			pstmt.executeUpdate();
			pstmt.close();
			dc.conn.commit();
		} catch (Exception e) {
			e.printStackTrace();
			dc.close();
		}
		System.out.println("점수가 입력되었습니다.");
	}

	// 학생들 조회
	public void ListStudent(String id) {
		String sql = "SELECT std_id, std_name, attendance_time FROM student WHERE tc_id = ?";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, id);
			dc.rs = dc.execute(pstmt);

			System.out.println("-------------------------------------------");
			System.out.printf("%-10s|%-10s|%-10s\n", "학생ID", "학생명", "출석시간");
			System.out.println("-------------------------------------------");
			while (dc.rs.next()) {
				Student std = new Student();
				std.setId(dc.rs.getString("std_id"));
				std.setName(dc.rs.getString("std_name"));
				std.setAttendanceTime(dc.rs.getInt("attendance_time"));
				System.out.printf("%-10s|%-10s|%-10s\n", std.getId(), std.getName(), std.getAttendanceTime());
			}
			System.out.println("\n");

//			else {
//				System.out.println("학생이 존재하지 않습니다.");
//			}

		} catch (Exception e) {
			e.printStackTrace();
			dc.close();
		}
	}

	public void listTest() {
		String sql = "SELECT s.std_id, t.test_name, t.score FROM test t JOIN student s ON t.std_id = s.std_id WHERE tc_id = ? "
				+ "ORDER BY s.std_id, t.test_name";
		PreparedStatement pstmt;

		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, id);
			dc.rs = dc.execute(pstmt);

			System.out.println("-------------------------------------------");
			System.out.printf("%-15s%-15s%-15s\n", "학생ID", "테스트명", "점수");
			System.out.println("-------------------------------------------");
			while (dc.rs.next()) {
				System.out.printf("%-15s%-15s%-15d\n", dc.rs.getString("std_id"), dc.rs.getString("test_name"),
						dc.rs.getInt("score"));
			}
			System.out.println("\n");
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void run() {
		String option;
		while (id != null) {
			System.out.println("--------------------------------------------------------------------");
			System.out.println("1. 학생 조회 | 2. 시험 성적 입력 | 3. 시험 성적 조회 | 4. 로그아웃 | q. 프로그램 종료");
			System.out.println("--------------------------------------------------------------------");
			System.out.print("선택> ");
			option = sc.nextLine();
			if ("1".equals(option)) {
				ListStudent(getId());
			} else if ("2".equals(option)) {
				insertscore();
			} else if ("3".equals(option)) {
				listTest();
			} else if ("4".equals(option)) {
				logout();
				break;
			} else if ("q".equals(option)) {
				System.out.println("프로그램을 종료합니다.");
				System.exit(0);
			} else {
				System.out.println("보기를 입력해주세요.");
			}

		}
	}
}
