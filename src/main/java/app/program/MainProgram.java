package app.program;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

import app.program.data.*;

import util.DBTablePrinter;

public class MainProgram extends AppMainHandler {
    private static void selectionCursor() {
        System.out.print("└─► ");
    }

    private static String inputHead() {
        return "↪ ";
    }

    private static String bulletHead() {
        return "• ";
    }

    private static String errorHead() {
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
                            menuStudentsInsert();
                            break;
                        case 2:
                            menuStudentsEdit();
                            break;
                        case 3:
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

    private void menuStudentsInsert() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        SQLOperations sqlOperations = new SQLOperations();
        StudentsColumns studentsColumns = new StudentsColumns();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT student_id FROM javasqlcuoiki.students ORDER BY student_id DESC LIMIT 1");
            resultSet.next();
            int studentIDNewest = resultSet.getInt("student_id");
            studentIDNewest++;
            System.out.println("Nhập thông tin sinh viên thứ " + studentIDNewest);
            while (true) {
                System.out.print(inputHead() + "Nhập tên họ: ");
                while (true) {
                    studentsColumns.lastName = sc.nextLine();
                    if (studentsColumns.lastName.isEmpty()) {
                        System.out.print(errorHead() + "Không được để trống: ");
                    }
                    else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.lastName,50)) {
                        System.out.print(errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }

                System.out.print(inputHead() + "Nhập tên lót: ");
                while (true) {
                    studentsColumns.middleName = sc.nextLine();
                    if (studentsColumns.middleName.isEmpty()) {
                        System.out.print(errorHead() + "Không được để trống: ");
                    }
                    else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.middleName,50)) {
                        System.out.print(errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }

                System.out.print(inputHead() + "Nhập tên: ");
                while (true) {
                    studentsColumns.firstName = sc.nextLine();
                    if (studentsColumns.firstName.isEmpty()) {
                        System.out.print(errorHead() + "Không được để trống: ");
                    }
                    else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.firstName,50)) {
                        System.out.print(errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }


                System.out.print(inputHead() + "Nhập giới tính (Nam=M, Nữ=F): ");
                while (true) {
                    try {
                        studentsColumns.gender = sc.next().charAt(0);
                        if (studentsColumns.gender == 'M' || studentsColumns.gender == 'F') {
                            break;
                        }
                        else {
                            System.out.print(errorHead() + "Vui lòng nhập 'M' cho Nam và 'F' cho nữ: ");
                        }
                    }
                    catch (StringIndexOutOfBoundsException e) {
                        System.out.print(errorHead() + "Không được để trống, vui lòng nhập lại: ");
                    }
                    catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.print(inputHead() + "Nhập ngày sinh (dd/MM/yyyy): ");
                while (true) {
                    try {
                        String localDateString = sc.next();
                        studentsColumns.dateOfBirth = LocalDate.parse(localDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        break;
                    } catch (Exception e) {
                        System.out.print(errorHead() + "Vui lòng nhập đúng format dd/MM/yyyy: ");
                        continue;
                    }
                }

                System.out.print(inputHead() + "Nhập quê quán: ");
                while (true) {
                    studentsColumns.birthplace = sc.next();
                    sc.nextLine();
                    if (studentsColumns.birthplace.isEmpty()) {
                        System.out.print(errorHead() + "Không được để trống: ");
                    }
                    else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.birthplace,128)) {
                        System.out.print(errorHead() + "Nội dung phải nhỏ hơn 128 ký tự: ");
                    } else break;
                }

                System.out.print(inputHead() + "Nhập số điện thoại: ");
                while (true) {
                    studentsColumns.contactNumber = sc.next();
                    sc.nextLine();
                    if (studentsColumns.contactNumber.isEmpty()) {
                        System.out.print(errorHead() + "Không được để trống: ");
                    }
                    else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.contactNumber,20)) {
                        System.out.print(errorHead() + "Nội dung phải nhỏ hơn 20 ký tự: ");
                    } else break;
                }

                System.out.print(inputHead() + "Nhập email: ");
                while (true) {
                    studentsColumns.email = sc.next();
                    sc.nextLine();
                    if (studentsColumns.email.isEmpty()) {
                        System.out.print(errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.email, 100)) {
                        System.out.print(errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                    } else break;
                }

                System.out.print(inputHead() + "Nhập địa chỉ: ");
                while (true) {
                    studentsColumns.address = sc.next();
                    sc.nextLine();
                    if (studentsColumns.address.isEmpty()) {
                        System.out.print(errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.address, 255)) {
                        System.out.print(errorHead() + "Nội dung phải nhỏ hơn 255 ký tự: ");
                    } else break;
                }

                System.out.println();
                System.out.println("Thông tin sinh viên thứ " + studentIDNewest + ":");
                studentsColumns.fullName = studentsColumns.lastName + " " + studentsColumns.middleName + " " + studentsColumns.firstName;
                System.out.println(bulletHead() + "Họ và tên: " + studentsColumns.fullName);
                System.out.print(bulletHead() + "Giới tính: ");
                if (studentsColumns.gender == 'M') {
                    System.out.println("Nam");
                } else {
                    System.out.println("Nữ");
                }
                System.out.println(bulletHead() + "Ngày sinh: " + studentsColumns.dateOfBirth.format(dateTimeFormatter));
                System.out.println(bulletHead() + "Nơi sinh: " + studentsColumns.birthplace);
                System.out.println(bulletHead() + "Số điện thoại: " + studentsColumns.contactNumber);
                System.out.println(bulletHead() + "Email: " + studentsColumns.email);
                System.out.println(bulletHead() + "Địa chỉ: " + studentsColumns.address);
                System.out.println("1. Xác nhận");
                System.out.println("2. Nhập lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                String query ="INSERT INTO javasqlcuoiki.students (last_name, middle_name, first_name, gender, date_of_birth, birthplace, contact_number, email, address) VALUE" +
                                        "(" +
                                        "'" + studentsColumns.lastName + "'," +
                                        "'" + studentsColumns.middleName + "'," +
                                        "'" + studentsColumns.firstName + "'," +
                                        "'" + studentsColumns.gender + "'," +
                                        "'" + studentsColumns.dateOfBirth + "'," +
                                        "'" + studentsColumns.birthplace + "'," +
                                        "'" + studentsColumns.contactNumber + "'," +
                                        "'" + studentsColumns.email + "'," +
                                        "'" + studentsColumns.address + "')";
                                sqlOperations.insertRows(query);
                                System.out.print("Nhấn enter để tiếp tục: ");
                                sc.nextLine();
                                return;
                            case 2:
                                break;
                            case 0:
                                return;
                            default:
                                System.out.print(errorHead() + "Vui lòng nhập lại: ");
                                continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.print(errorHead() + "Vui lòng nhập lại: ");
                        sc.next();
                        continue;
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private void menuStudentsEdit() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        while (true) {
            System.out.print(inputHead() + "Chọn MSSV cần sửa: ");
            while (true) {
                try {
                    boolean success = false;
                    Statement statement = connection.createStatement();
                    ResultSet resultSet = statement.executeQuery("SELECT student_id FROM students ORDER BY student_id");
                    int selection = sc.nextInt();
                    int studentID = resultSet.getInt("student_id");
                    resultSet.first();
                    while (resultSet.next()) {
                        if (selection == studentID) {
                            success = true;
                            break;
                        }
                    }
                    if (success) {
                        break;
                    }
                    else {
                        System.out.print("Không có trong danh sách: ");
                    }
                }
                catch (InputMismatchException e) {
                    System.out.print("Vui lòng nhập đúng số thứ tự: ");
                }
                catch (SQLException e) {
                    throw new RuntimeException(e);
                }
            }
        }
    }

    private void menuStudentsRemove() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        System.out.println(inputHead() + "Chọn MSSV cần xóa: ");
        int selection;
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
