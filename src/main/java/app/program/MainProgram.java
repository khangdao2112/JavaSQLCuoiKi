package app.program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import app.program.menu.*;
import util.DBTablePrinter;

public class MainProgram extends AppMainHandler {
    private static final MenuStudents menuStudents;
    static {
        try {
            menuStudents = new MenuStudents();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final MenuCourses menuCourses;
    static {
        try {
            menuCourses = new MenuCourses();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private static final MenuEnrollments menuEnrollments;
    static {
        try {
            menuEnrollments = new MenuEnrollments();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void printASCII() {
        System.out.print("""
                ┌──────────────────────────────────┐
                │  ____  __  __ ____               │
                │ / ___||  \\/  / ___|       v1.0.1 │
                │ \\___ \\| |\\/| \\___ \\      Student │
                │  ___) | |  | |___) |  Management │
                │ |____/|_|  |_|____/       System │
                │                                  │
                └──────────────────────────────────┘
                """);
    }

    public static void menu() {
        Scanner sc = new Scanner(System.in);
        printASCII();
        while (true) {
            System.out.println();
            System.out.print("""
                    │ ░█▄█░█▀▀░█▀█░█░█
                    │ ░█░█░█▀▀░█░█░█░█
                    │ ░▀░▀░▀▀▀░▀░▀░▀▀▀
                    │ Sử dụng các phím số để chọn các chức năng tương ứng
                    │ 1. Quản lí sinh viên
                    │ 2. Quản lí khóa học
                    │ 3. Quản lí ghi danh
                    │ 9. Trợ giúp & giới thiệu
                    │ 0. Thoát chương trình
                    """);
            StringPrefix.selectionCursor();
            while (true){
                try {
                    byte selection = sc.nextByte();
                    switch (selection) {
                        case 1:
                            menuStudents.menuSelection();
                            break;
                        case 2:
                            menuCourses.menuSelection();
                            break;
                        case 3:
                            menuEnrollments.menuSelection();
                            break;
                        case 9:
                            menuAppInfo();
                            break;
                        case 0:
                            System.out.println();
                            System.out.print("""
                                            │ Bạn có chắc chắn muốn thoát?
                                            │ 1. Thoát
                                            │ 0. Hủy
                                            """);
                            StringPrefix.selectionCursor();
                            while (true) {
                                try {
                                    selection = sc.nextByte();
                                    switch (selection) {
                                        case 1:
                                            closeConnection();
                                            return;
                                        case 0:
                                            break;
                                        default:
                                            System.out.println(StringPrefix.errorHead() + "Vui lòng chọn đúng số");
                                            StringPrefix.selectionCursor();
                                            continue;
                                    }
                                    break;
                                } catch (InputMismatchException e) {
                                    System.out.println(StringPrefix.errorHead() + "Vui lòng chọn đúng số");
                                    StringPrefix.selectionCursor();
                                    sc.next();
                                }
                            }
                            break;
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

    public static void printTable(String table) {
        try {
            Statement statement = connection.createStatement();
            if (table == "students") {
                ResultSet resultSet = statement.executeQuery(
                        "SELECT\n" +
                                "    student_id as 'MSSV',\n" +
                                "    CONCAT(last_name,' ',middle_name,' ',first_name) AS 'Họ và tên',\n" +
                                "    gender AS 'Giới tính',\n" +
                                "    date_of_birth AS 'Ngày sinh',\n" +
                                "    birthplace AS 'Quê quán',\n" +
                                "    contact_number AS 'Số điện thoại',\n" +
                                "    email AS 'Email',\n" +
                                "    address AS 'Địa chỉ',\n" +
                                "    id_number AS 'Số CCCD',\n" +
                                "    class_id AS 'Lớp'\n" +
                                "FROM students;"
                );
                DBTablePrinter.printResultSet(resultSet);
            }
            if (table == "courses") {
                ResultSet resultSet = statement.executeQuery(
                        "SELECT\n" +
                                "    course_id AS 'Mã môn',\n" +
                                "    course_name AS 'Tên môn',\n" +
                                "    course_description AS 'Mô tả',\n" +
                                "    credits AS 'Tín chỉ',\n" +
                                "    CONCAT(last_name,' ',middle_name,' ',first_name) AS 'Tên giảng viên',\n" +
                                "    class_id AS 'Lớp'\n" +
                                "FROM courses\n" +
                                "JOIN javasqlcuoiki.instructors i on i.instructor_id = courses.instructor_id"
                );
                DBTablePrinter.printResultSet(resultSet);
            }
            if (table == "enrollments") {
                ResultSet resultSet = statement.executeQuery("""
                    SELECT
                        students.student_id AS 'MSSV',
                        CONCAT(students.last_name,' ',students.middle_name,' ',students.first_name) AS 'Họ và tên',
                        courses.course_id AS 'Mã môn học',
                        courses.course_name AS 'Tên môn học',
                        grade
                    FROM enrollments
                    JOIN students ON enrollments.student_id = students.student_id
                    JOIN courses ON enrollments.course_id = courses.course_id
                    ORDER BY students.student_id ASC
                    """);
                DBTablePrinter.printResultSet(resultSet);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public static void menuAppInfo() {
        Scanner pressEnter = new Scanner(System.in);
        System.out.println();
        System.out.print("""
                │ ░█▀█░█▀▄░█▀█░█░█░▀█▀
                │ ░█▀█░█▀▄░█░█░█░█░░█░
                │ ░▀░▀░▀▀░░▀▀▀░▀▀▀░░▀░
                │ Student Management System
                │ Version 1.0.0
                │ Credits @Khang Đào
                │         @Mỹ Duyên
                │         @Ngọc Nhi
                │ Nhấn enter để quay lại
                """);
        StringPrefix.selectionCursor();
        pressEnter.nextLine();
    }
}
