package app.program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

import app.program.menu.menuStudents;
import util.DBTablePrinter;

public class MainProgram extends AppMainHandler {
    public menuStudents menuStudents = new menuStudents();

    public static void selectionCursor() {
        System.out.print("└─► ");
    }

    public static String inputHead() {
        return "↪ ";
    }

    public static String bulletHead() {
        return "• ";
    }

    public static String errorHead() {
        return "⚠ ";
    }

    public void printASCII() {
        System.out.print("""
                ┌──────────────────────────────────┐
                │  ____  __  __ ____               │
                │ / ___||  \\\\/  / ___|       v1.0.0 │
                │ \\\\___ \\\\| |\\\\/| \\\\___ \\\\      Student │
                │  ___) | |  | |___) |  Management │
                │ |____/|_|  |_|____/       System │
                │                                  │
                └──────────────────────────────────┘
                """);
    }

    public void menu() {
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
            selectionCursor();
            while (true){
                try {
                    byte selection = sc.nextByte();
                    switch (selection) {
                        case 1:
                            menuStudents();
                            break;
                        case 2:
                            //menuCourses();
                            break;
                        case 3:
                            //menuEnrollments();
                            break;
                        case 9:
                            menuAppInfo();
                            break;
                        case 0:
                            closeConnection();
                            return;
                        default:
                            System.out.println("Vui lòng nhập lại");
                            selectionCursor();
                            continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Vui lòng nhập lại");
                    selectionCursor();
                    sc.next();
                    continue;
                }
            }
        }
    }

    public void menuStudents() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println();
            System.out.println("Danh sách các sinh viên");
            printStudentsList();
            System.out.print("""
                    │ Sử dụng các phím số để chọn các chức năng tương ứng
                    │ 1. Thêm sinh viên
                    │ 2. Sửa sinh viên
                    │ 3. Xóa sinh viên
                    │ 4. Chọn sinh viên
                    │ 0. Quay về menu
                    """);
            selectionCursor();
            while (true){
                try {
                    byte selection = sc.nextByte();
                    switch (selection) {
                        case 1:
                            menuStudents.menuStudentsInsert();
                            break;
                        case 2:
                            menuStudents.menuStudentsEdit();
                            break;
                        case 3:
                            menuStudents.menuStudentsRemove();
                            break;
                        case 4:
                            break;
                        case 0:
                            return;
                        default:
                            System.out.println("Vui lòng nhập lại");
                            selectionCursor();
                            continue;
                    }
                    break;
                } catch (InputMismatchException e) {
                    System.out.println("Vui lòng nhập lại");
                    selectionCursor();
                    sc.next();
                    continue;
                }
            }
        }
    }

    public void printStudentsList() {
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(
                "SELECT\n" +
                "    student_id as 'MSSV',\n" +
                "    CONCAT(last_name,' ',middle_name,' ',first_name) AS 'Họ và tên',\n" +
                "    gender AS 'Giới tính',\n" +
                "    date_of_birth AS 'Ngày sinh',\n" +
                "    birthplace AS 'Quê quán',\n" +
                "    contact_number AS 'Số điện thoại',\n" +
                "    email AS 'Email',\n" +
                "    address AS 'Địa chỉ'\n" +
                "FROM students;"
            );
            DBTablePrinter.printResultSet(resultSet);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void menuAppInfo() {
        Scanner pressEnter = new Scanner(System.in);
        System.out.println();
        System.out.println("│ ░█▀█░█▀▄░█▀█░█░█░▀█▀");
        System.out.println("│ ░█▀█░█▀▄░█░█░█░█░░█░");
        System.out.println("│ ░▀░▀░▀▀░░▀▀▀░▀▀▀░░▀░");
        System.out.println("│ Student Management System");
        System.out.println("│ Version 1.0.0");
        System.out.println("│ Credits @Khang Đào");
        System.out.println("│         @Mỹ Duyên");
        System.out.println("│         @Ngọc Nhi");
        System.out.println("│ Nhấn enter để quay lại   ");
        selectionCursor();
        pressEnter.nextLine();
    }
}
