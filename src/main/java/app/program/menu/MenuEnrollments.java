package app.program.menu;

import app.program.AppMainHandler;
import app.program.MainProgram;
import app.program.SQLOperations;
import app.program.StringPrefix;
import util.DBTablePrinter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLSyntaxErrorException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuEnrollments extends AppMainHandler {
    private Statement statement = connection.createStatement(ResultSet.TYPE_SCROLL_SENSITIVE,ResultSet.CONCUR_READ_ONLY);
    private SQLOperations sqlOperations = new SQLOperations();

    private String studentID;
    private String courseID;
    private float grade;

    public MenuEnrollments() throws SQLException {

    }

    public void menuSelection() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println();
            System.out.println("Danh sách các mục ghi danh");
            MainProgram.printTable("enrollments");
            System.out.print("""
                    │ Sử dụng các phím số để chọn các chức năng tương ứng
                    │ 1. Thêm ghi danh
                    │ 2. Chọn ghi danh (Sửa hoặc xóa)
                    │ 0. Quay về menu
                    """);
            StringPrefix.selectionCursor();
            while (true){
                try {
                    byte selection = sc.nextByte();
                    switch (selection) {
                        case 1:
                            menuEnrollmentsInsert();
                            break;
                        case 2:
                            menuEnrollmentsSelect();
                            break;
                        case 0:
                            return;
                        default:
                            System.out.println(StringPrefix.errorHead() + "Vui lòng nhập lại");
                            StringPrefix.selectionCursor();
                            continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println(StringPrefix.errorHead() + "Vui lòng nhập lại");
                    StringPrefix.selectionCursor();
                    sc.next();
                }
            }
        }
    }

        public void menuEnrollmentsInsert() {
            try {
                Scanner sc = new Scanner(System.in);
                sc.useDelimiter("\\n");
                while (true) {
                    ResultSet studentTable = statement.executeQuery("""
                        SELECT
                            student_id AS 'MSSV',
                            CONCAT(last_name,' ',middle_name,' ',first_name) AS 'Họ và tên sinh viên'
                        FROM students
                         """);
                    DBTablePrinter.printResultSet(studentTable);
                    System.out.print(StringPrefix.inputHead() + "Nhập MSSV: ");
                    while (true) {
                        try {
                            boolean success = false;
                            ResultSet resultSet = statement.executeQuery("SELECT student_id FROM students ORDER BY student_id");
                            studentID = sc.next();
                            while (resultSet.next()) {
                                String studentIDResult = resultSet.getString("student_id");
                                if (studentID.equals(studentIDResult)) {
                                    success = true;
                                    break;
                                }
                            }
                            if (success) {
                                break;
                            } else {
                                System.out.print(StringPrefix.errorHead() + "Không có trong danh sách: ");
                            }
                        } catch (InputMismatchException e) {
                            System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng số thứ tự: ");
                            sc.next();
                        }
                    }

                    ResultSet courseTable = statement.executeQuery("""
                        SELECT
                            course_id AS 'Mã môn học',
                            course_name AS 'Tên môn học',
                            class_id AS 'Lớp'
                        FROM courses                        
                        """);
                    DBTablePrinter.printResultSet(courseTable);
                    System.out.print(StringPrefix.inputHead() + "Chọn mã môn học: ");
                    while (true) {
                        try {
                            boolean success = false;
                            ResultSet resultSet = statement.executeQuery("SELECT course_id FROM courses");
                            courseID = sc.next();
                            while (resultSet.next()) {
                                String courseIDResult = resultSet.getString("course_id");
                                if (courseID.equals(courseIDResult)) {
                                    success = true;
                                    break;
                                }
                            }
                            if (success) {
                                break;
                            } else {
                                System.out.print(StringPrefix.errorHead() + "Không có trong danh sách: ");
                            }
                        } catch (InputMismatchException e) {
                            System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng số thứ tự: ");
                            sc.next();
                        }
                    }

                    ResultSet checkDuplicate = statement.executeQuery("SELECT * FROM enrollments WHERE student_id = '" + studentID + "' AND course_id = '" + courseID + "'");
                    if (!checkDuplicate.isBeforeFirst()) {
                        System.out.print(StringPrefix.inputHead() + "Nhập điểm (>=0 và <= 10): ");
                        while (true) {
                            try {
                                grade = sc.nextFloat();
                                if (grade >= 0 && grade <= 10) {
                                    break;
                                } else {
                                    System.out.print(StringPrefix.errorHead() + "Điểm phải >=0 và <=10: ");
                                }
                            } catch (InputMismatchException e) {
                                System.out.println(StringPrefix.errorHead() + "Vui lòng nhập đúng điểm: ");
                            }
                        }
                            System.out.println();
                            System.out.println("Thông tin ghi danh: ");
                            System.out.println(StringPrefix.bulletHead() + "MSSV: " + studentID);
                            System.out.println(StringPrefix.bulletHead() + "Mã môn: " + courseID);
                            System.out.println(StringPrefix.bulletHead() + "Điểm: " + grade);
                            System.out.println("1. Xác nhận");
                            System.out.println("2. Nhập lại");
                            System.out.println("0. Hủy");
                            while (true) {
                                try {
                                    byte selection = sc.nextByte();
                                    switch (selection) {
                                        case 1:
                                            String query = "INSERT INTO enrollments (student_id, course_id, grade)\n" +
                                                    "VALUE ('" + studentID + "','" + courseID + "'," + grade + ")";
                                            sqlOperations.insertRows(query);
                                            System.out.print("Nhấn enter để tiếp tục: ");
                                            sc.nextLine();
                                            return;
                                        case 2:
                                            break;
                                        case 0:
                                            return;
                                        default:
                                            System.out.print(StringPrefix.errorHead() + "Vui lòng nhập lại: ");
                                            continue;
                                    }
                                    break;
                                } catch (InputMismatchException e) {
                                    System.out.print(StringPrefix.errorHead() + "Vui lòng nhập lại: ");
                                    sc.next();
                                }
                            }
                    } else {
                        checkDuplicate.next();
                        String checkDuplicateStudentID = checkDuplicate.getString("student_id");
                        String checkDuplicateCourseID = checkDuplicate.getString("course_id");
                        if (studentID.equals(checkDuplicateStudentID) & courseID.equals(checkDuplicateCourseID)) {
                            System.out.println(StringPrefix.errorHead() + "Dữ liệu trùng lặp trong bảng, vui lòng nhập lại");
                            checkDuplicate.previous();
                            DBTablePrinter.printResultSet(checkDuplicate);
                        }
                    }
                    /* while (true) {
                        ResultSet resultSetDuplicateCheck = statement.executeQuery("SELECT student_id FROM enrollments GROUP BY student_id ORDER BY student_id");
                        int studentIDDuplicateCheck;
                        int courseIDDuplicateCheck;
                        while (resultSetDuplicateCheck.next()) {
                            studentIDDuplicateCheck = resultSetDuplicateCheck.getInt("student_id");
                            if (studentIDDuplicateCheck == studentID) {
                                resultSetDuplicateCheck = statement.executeQuery("SELECT student_id, course_id FROM enrollments WHERE student_id = " + courseID);
                                courseIDDuplicateCheck = resultSetDuplicateCheck.getInt("course_id");
                                if (courseIDDuplicateCheck == courseID) {
                                    System.out.println(StringPrefix.errorHead() + "MSSV và ID khóa trùng trong danh sách! Vui lòng nhập lại");
                                }
                            }
                            else {
                                System.out.print(StringPrefix.inputHead() + "Nhập điểm (>=0 và <= 10): ");
                                while (true) {
                                    try {
                                        grade = sc.nextFloat();
                                        if (grade >= 0 && grade <= 10) {
                                            break;
                                        }
                                        else {
                                            System.out.print(StringPrefix.errorHead() + "Điểm phải >=0 và <=10: ");
                                        }
                                    } catch (InputMismatchException e) {
                                        System.out.println(StringPrefix.errorHead() + "Vui lòng nhập đúng điểm: ");
                                    }
                                }

                                System.out.println();
                                System.out.println("Thông tin ghi danh: ");
                                System.out.println(StringPrefix.bulletHead() + "MSSV: " + studentID);
                                System.out.println(StringPrefix.bulletHead() + "ID khóa: " + courseID);
                                System.out.println(StringPrefix.bulletHead() + "Điểm: " + grade);
                                System.out.println("1. Xác nhận");
                                System.out.println("2. Nhập lại");
                                System.out.println("0. Hủy");
                                while (true) {
                                    try {
                                        byte selection = sc.nextByte();
                                        switch (selection) {
                                            case 1:
                                                String query = "INSERT INTO enrollments (student_id, course_id, grade)\n" +
                                                        "VALUE (" + studentID + "," + courseID + "," + grade + ")";
                                                sqlOperations.insertRows(query);
                                                System.out.print("Nhấn enter để tiếp tục: ");
                                                sc.nextLine();
                                                return;
                                            case 2:
                                                break;
                                            case 0:
                                                return;
                                            default:
                                                System.out.print(StringPrefix.errorHead() + "Vui lòng nhập lại: ");
                                                continue;
                                        }
                                        break;
                                    } catch (InputMismatchException e) {
                                        System.out.print(StringPrefix.errorHead() + "Vui lòng nhập lại: ");
                                        sc.next();
                                        continue;
                                    }
                                }
                            }
                        }
                    }*/
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }

    public void menuEnrollmentsSelect() {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            while (true) {
                String queryForSelection;
                System.out.print(StringPrefix.inputHead() + "Nhập MSSV để chọn sinh viên tương ứng: ");
                String selectionStudentID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery("SELECT student_id FROM enrollments GROUP BY student_id ORDER BY student_id");
                        selectionStudentID = sc.next();
                        while (resultSet.next()) {
                            studentID = resultSet.getString("student_id");
                            if (studentID.equals(selectionStudentID)) {
                                success = true;
                                break;
                            }
                        }
                        if (success) {
                            break;
                        } else {
                            System.out.print(StringPrefix.errorHead() + "Không có trong danh sách: ");
                        }
                    } catch (InputMismatchException e) {
                        System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng số thứ tự: ");
                        sc.next();
                    }
                }

                printTableFromQuery("""
                        SELECT
                            CONCAT(students.last_name,' ',students.middle_name,' ',students.first_name) AS 'Họ và tên',
                            courses.course_name AS 'Tên môn học',
                            courses.course_id AS 'Mã môn',
                            grade
                        FROM enrollments
                                 JOIN students ON enrollments.student_id = students.student_id
                                 JOIN courses ON enrollments.course_id = courses.course_id
                        WHERE students.student_id = '""" + selectionStudentID + "'");
                System.out.print(StringPrefix.inputHead() + "Chọn mã môn học: ");
                String selectionCourseID;
                while (true) {
                    try {
                        boolean success = false;
                        selectionCourseID = sc.next();
                        ResultSet resultSet = statement.executeQuery("SELECT course_id FROM enrollments WHERE student_id = '" + selectionStudentID + "'");
                        while (resultSet.next()) {
                            courseID = resultSet.getString("course_id");
                            if (courseID.equals(selectionCourseID)) {
                                success = true;
                                break;
                            }
                        }
                        if (success) {
                            break;
                        } else {
                            System.out.print(StringPrefix.errorHead() + "Không có trong danh sách: ");
                        }
                    }
                    catch (SQLSyntaxErrorException | InputMismatchException e) {
                        System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng số thứ tự: ");
                        sc.next();
                    }
                }

                System.out.println("Sử dụng các phím số để chọn các chức năng tương ứng");
                System.out.println("1. Sửa điểm");
                System.out.println("2. Xóa");
                System.out.println("3. Chọn lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                menuEnrollmentSelectEdit(selectionStudentID, selectionCourseID);
                                return;
                            case 2:
                                String query = "DELETE FROM enrollments WHERE student_id = '" + selectionStudentID + "' AND course_id = '" + selectionCourseID + "'";
                                sqlOperations.removeRows(query);
                                System.out.print("Nhấn enter để tiếp tục: ");
                                sc.nextLine();
                                return;
                            case 3:
                                break;
                            case 0:
                                return;
                            default:
                                System.out.println("Vui lòng nhập lại");
                                StringPrefix.selectionCursor();
                                continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.println("Vui lòng nhập lại");
                        StringPrefix.selectionCursor();
                        sc.next();
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void menuEnrollmentSelectEdit(String studentID, String courseID) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        while (true) {
            System.out.print(StringPrefix.inputHead() + "Nhập điểm (>=0 và <= 10): ");
            while (true) {
                try {
                    grade = sc.nextFloat();
                    if (grade >= 0 && grade <= 10) {
                        break;
                    }
                    else {
                        System.out.print(StringPrefix.errorHead() + "Điểm phải >=0 và <=10: ");
                    }
                } catch (InputMismatchException e) {
                    System.out.println(StringPrefix.errorHead() + "Vui lòng nhập đúng điểm: ");
                }
            }

            System.out.println();
            System.out.println(StringPrefix.bulletHead() + "MSSV: " + studentID);
            System.out.println(StringPrefix.bulletHead() + "Mã môn: " + courseID);
            System.out.println(StringPrefix.bulletHead() + "Điểm: " + grade);
            System.out.println("1. Xác nhận thay đổi");
            System.out.println("2. Nhập lại");
            System.out.println("0. Hủy");
            while (true) {
                try {
                    byte selection = sc.nextByte();
                    switch (selection) {
                        case 1:
                            String query = "UPDATE enrollments SET grade = " + grade + " WHERE student_id = '" + studentID + "' AND course_id = '" + courseID + "'\n";
                            sqlOperations.editRows(query);
                            System.out.print("Nhấn enter để tiếp tục: ");
                            sc.nextLine();
                            return;
                        case 2:
                            break;
                        case 0:
                            return;
                        default:
                            System.out.print(StringPrefix.errorHead() + "Vui lòng nhập lại: ");
                            continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println(StringPrefix.errorHead() + "Vui lòng nhập lại");
                    sc.next();
                }
            }
        }
    }

    public void printTableFromQuery(String query) throws SQLException{
       ResultSet resultSet = statement.executeQuery(query);
       DBTablePrinter.printResultSet(resultSet);
    }
}