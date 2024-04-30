package application;

import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Date;

import lombok.Data;

@Data
public class Student extends User {
	private int attendanceTime = 0;

	public Student() {
	}

	public Student(String stdId, String stdName, int attendanceTime) {
		this.id = stdId;
		this.name = stdName;
		this.attendanceTime = attendanceTime;
	}

	// 로그인
	public void login(String id, String pw) {
		String sql = "SELECT * FROM student WHERE std_id = ? AND std_pw = ?";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, id);
			pstmt.setString(2, pw);
			dc.rs = dc.execute(pstmt);
			if (dc.rs.next()) {
				this.id = dc.rs.getString("std_id");
				this.name = dc.rs.getString("std_name");
				this.attendanceTime = dc.rs.getInt("attendance_time");
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
		attendanceTime = 0;
		dc = null;
	}

	// 입실 기록하기
	public void checkin() {
		// attendance 테이블에 insert
		String sql = "INSERT INTO attendance VALUES(seq_aid.nextval, '" + id + "', sysdate, null)";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			int rows = dc.update(pstmt);
			if (rows == 1) {
				System.out.println(name + "님 입실을 완료하였습니다.");
			} else {
				System.out.println("입실 실패.  관리자에게 문의하세요");
			}
			dc.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	// 퇴실 기록하기
	public void checkout(int atdId, int atdTime) {
		// attendance 테이블에 insert
		String sql1 = "UPDATE attendance SET checkout_time = sysdate WHERE atd_id = ?";
		String sql2 = "UPDATE student SET attendance_time = attendance_time + ?  WHERE std_id = ?";
		String sql3 = "SELECT attendance_time FROM student WHERE std_id = '" + id + "'";
		PreparedStatement pstmt1;
		PreparedStatement pstmt2;
		PreparedStatement pstmt3;
		try {
			pstmt1 = dc.conn.prepareStatement(sql1);
			pstmt1.setInt(1, atdId);
			pstmt2 = dc.conn.prepareStatement(sql2);
			pstmt2.setInt(1, atdTime);
			pstmt2.setString(2, id);
			if (dc.update(pstmt1) == 1 && dc.update(pstmt2) == 1) {
				System.out.println(name + "님 퇴실을 완료하였습니다.");
				pstmt3 = dc.conn.prepareStatement(sql3);
				dc.rs = dc.execute(pstmt3);
				if (dc.rs.next()) {
					attendanceTime = dc.rs.getInt("attendance_time");
				}
			} else {
				System.out.println("퇴실 실패. 관리자에게 문의하세요.");
			}
			dc.conn.commit();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 입실 기록 확인하고 처리
	public void attendance(int option) {
		// 오늘 출석 기록이 있는지 확인
		String sql = "SELECT atd_id, ROUND((sysdate - checkin_time)*24*60) AS atd_time  FROM attendance"
				+ " WHERE TO_CHAR(checkin_time, 'YYYYMMDD') = TO_CHAR(sysdate, 'YYYYMMDD') AND std_id = ?";
		PreparedStatement pstmt;
		try {
			pstmt = dc.conn.prepareStatement(sql);
			pstmt.setString(1, id);
			dc.rs = dc.execute(pstmt);
			if (dc.rs.next()) { // 입실 기록 있음
				if (option == 2) { // 퇴실을 기록하려 함
					checkout(dc.rs.getInt("atd_id"), dc.rs.getInt("atd_time"));
				} else { // 입실을 기록하려함
					System.out.println("입실은 이미 기록되어 있습니다.");
				}
			} else { // 입실 기록 없음
				if (option == 1) { // 입실을 기록하려 함
					checkin();
				} else { // 퇴실을 기록하려 함
					System.out.println("입실은 먼저 기록하세요");
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	// 시험 성적 확인
	public void listTest() {
		String sql = "SELECT std_id, test_name, score FROM test WHERE std_id = ?";
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
			System.out.println("------------------------------------------------------");
			System.out.printf("%s  (출석시간 : %d)\n\n", id, attendanceTime);
			System.out.println("1. 입실 | 2. 퇴실 | 3. 시험성적 조회 | 4.로그아웃 | q.프로그램 종료");
			System.out.println("------------------------------------------------------");
			System.out.print("선택> ");
			option = sc.nextLine();
			if ("1".equals(option)) {
				attendance(1);
			} else if ("2".equals(option)) {
				attendance(2);
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
