package app.program.menu;

import app.program.AppMainHandler;
import app.program.MainProgram;
import app.program.SQLOperations;
import app.program.StringPrefix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuStudents extends AppMainHandler {
    private Statement statement = connection.createStatement();
    private SQLOperations sqlOperations = new SQLOperations();
    private DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

    private String fullName;
    private String lastName;
    private String middleName;
    private String firstName;
    private char gender;
    private LocalDate dateOfBirth;
    private String birthplace;
    private String contactNumber;
    private String email;
    private String address;

    public MenuStudents() throws SQLException {

    }

    public void menuSelection() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println();
            System.out.println("Danh sách các sinh viên");
            MainProgram.printTable("students");
            System.out.print("""
                    │ Sử dụng các phím số để chọn các chức năng tương ứng
                    │ 1. Thêm sinh viên
                    │ 2. Sửa sinh viên
                    │ 3. Xóa sinh viên
                    │ 0. Quay về menu
                    """);
            StringPrefix.selectionCursor();
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
                            menuStudentsRemove();
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
                    continue;
                }
            }
        }
    }

    public void menuStudentsInsert() {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
            ResultSet resultSet = statement.executeQuery("SELECT student_id FROM students ORDER BY student_id DESC LIMIT 1");
            resultSet.next();
            int studentIDNewest = resultSet.getInt("student_id");
            studentIDNewest++;

            System.out.println("Nhập thông tin sinh viên thứ " + studentIDNewest);
            while (true) {
                System.out.print(StringPrefix.inputHead() + "Nhập tên họ: ");
                while (true) {
                    lastName = sc.nextLine();
                    if (lastName.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(lastName, 50)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập tên lót: ");
                while (true) {
                    middleName = sc.nextLine();
                    if (middleName.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(middleName, 50)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập tên: ");
                while (true) {
                    firstName = sc.nextLine();
                    if (firstName.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(firstName, 50)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }


                System.out.print(StringPrefix.inputHead() + "Nhập giới tính (Nam=M, Nữ=F): ");
                while (true) {
                    try {
                        gender = sc.next().charAt(0);
                        if (gender == 'M' || gender == 'F') {
                            break;
                        } else {
                            System.out.print(StringPrefix.errorHead() + "Vui lòng nhập 'M' cho Nam và 'F' cho nữ: ");
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống, vui lòng nhập lại: ");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.print(StringPrefix.inputHead() + "Nhập ngày sinh (dd/MM/yyyy): ");
                while (true) {
                    try {
                        String localDateString = sc.next();
                        dateOfBirth = LocalDate.parse(localDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        break;
                    } catch (Exception e) {
                        System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng format dd/MM/yyyy: ");
                        continue;
                    }
                }

                System.out.print(StringPrefix.inputHead() + "Nhập quê quán: ");
                while (true) {
                    birthplace = sc.next();
                    sc.nextLine();
                    if (birthplace.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(birthplace, 128)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 128 ký tự: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập số điện thoại: ");
                while (true) {
                    contactNumber = sc.next();
                    sc.nextLine();
                    if (contactNumber.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(contactNumber, 20)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 20 ký tự: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập email: ");
                while (true) {
                    email = sc.next();
                    sc.nextLine();
                    if (email.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(email, 100)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập địa chỉ: ");
                while (true) {
                    address = sc.next();
                    sc.nextLine();
                    if (address.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(address, 255)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 255 ký tự: ");
                    } else break;
                }

                System.out.println();
                System.out.println("Thông tin sinh viên thứ " + studentIDNewest + ":");
                fullName = lastName + " " + middleName + " " + firstName;
                System.out.println(StringPrefix.bulletHead() + "Họ và tên: " + fullName);
                System.out.print(StringPrefix.bulletHead() + "Giới tính: ");
                if (gender == 'M') {
                    System.out.println("Nam");
                } else {
                    System.out.println("Nữ");
                }
                System.out.println(StringPrefix.bulletHead() + "Ngày sinh: " + dateOfBirth.format(dateTimeFormatter));
                System.out.println(StringPrefix.bulletHead() + "Nơi sinh: " + birthplace);
                System.out.println(StringPrefix.bulletHead() + "Số điện thoại: " + contactNumber);
                System.out.println(StringPrefix.bulletHead() + "Email: " + email);
                System.out.println(StringPrefix.bulletHead() + "Địa chỉ: " + address);
                System.out.println("1. Xác nhận");
                System.out.println("2. Nhập lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                String query = "INSERT INTO students (last_name, middle_name, first_name, gender, date_of_birth, birthplace, contact_number, email, address) VALUE" +
                                        "(" +
                                        "'" + lastName + "'," +
                                        "'" + middleName + "'," +
                                        "'" + firstName + "'," +
                                        "'" + gender + "'," +
                                        "'" + dateOfBirth + "'," +
                                        "'" + birthplace + "'," +
                                        "'" + contactNumber + "'," +
                                        "'" + email + "'," +
                                        "'" + address + "')";
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void menuStudentsEdit() {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            while (true) {
                System.out.print(StringPrefix.inputHead() + "Chọn MSSV cần sửa: ");
                int selectionStudentID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery("SELECT student_id FROM students ORDER BY student_id");
                        selectionStudentID = sc.nextInt();
                        while (resultSet.next()) {
                            int studentID = resultSet.getInt("student_id");
                            if (selectionStudentID == studentID) {
                                success = true;
                                break;
                            }
                        }
                        if (success) {
                            break;
                        } else {
                            System.out.print("Không có trong danh sách: ");
                        }
                    } catch (InputMismatchException e) {
                        System.out.print("Vui lòng nhập đúng số thứ tự: ");
                        sc.next();
                    }
                }

                while (true) {
                    System.out.print(StringPrefix.inputHead() + "Nhập tên họ: ");
                    while (true) {
                        lastName = sc.nextLine();
                        if (lastName.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(lastName, 50)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập tên lót: ");
                    while (true) {
                        middleName = sc.nextLine();
                        if (middleName.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(middleName, 50)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập tên: ");
                    while (true) {
                        firstName = sc.nextLine();
                        if (firstName.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(firstName, 50)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                        } else break;
                    }


                    System.out.print(StringPrefix.inputHead() + "Nhập giới tính (Nam=M, Nữ=F): ");
                    while (true) {
                        try {
                            gender = sc.next().charAt(0);
                            if (gender == 'M' || gender == 'F') {
                                break;
                            } else {
                                System.out.print(StringPrefix.errorHead() + "Vui lòng nhập 'M' cho Nam và 'F' cho nữ: ");
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống, vui lòng nhập lại: ");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập ngày sinh (dd/MM/yyyy): ");
                    while (true) {
                        try {
                            String localDateString = sc.next();
                            dateOfBirth = LocalDate.parse(localDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            break;
                        } catch (Exception e) {
                            System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng format dd/MM/yyyy: ");
                            continue;
                        }
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập quê quán: ");
                    while (true) {
                        birthplace = sc.next();
                        sc.nextLine();
                        if (birthplace.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(birthplace, 128)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 128 ký tự: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập số điện thoại: ");
                    while (true) {
                        contactNumber = sc.next();
                        sc.nextLine();
                        if (contactNumber.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(contactNumber, 20)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 20 ký tự: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập email: ");
                    while (true) {
                        email = sc.next();
                        sc.nextLine();
                        if (email.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(email, 100)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập địa chỉ: ");
                    while (true) {
                        address = sc.next();
                        sc.nextLine();
                        if (address.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(address, 255)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 255 ký tự: ");
                        } else break;
                    }

                    System.out.println();
                    System.out.println("Thông tin sinh viên thứ " + selectionStudentID + ":");
                    fullName = lastName + " " + middleName + " " + firstName;
                    System.out.println(StringPrefix.bulletHead() + "Họ và tên: " + fullName);
                    System.out.print(StringPrefix.bulletHead() + "Giới tính: ");
                    if (gender == 'M') {
                        System.out.println("Nam");
                    } else {
                        System.out.println("Nữ");
                    }
                    System.out.println(StringPrefix.bulletHead() + "MSSV: " + selectionStudentID);
                    System.out.println(StringPrefix.bulletHead() + "Ngày sinh: " + dateOfBirth.format(dateTimeFormatter));
                    System.out.println(StringPrefix.bulletHead() + "Nơi sinh: " + birthplace);
                    System.out.println(StringPrefix.bulletHead() + "Số điện thoại: " + contactNumber);
                    System.out.println(StringPrefix.bulletHead() + "Email: " + email);
                    System.out.println(StringPrefix.bulletHead() + "Địa chỉ: " + address);
                    System.out.println("1. Xác nhận");
                    System.out.println("2. Nhập lại");
                    System.out.println("0. Hủy");
                    while (true) {
                        try {
                            byte selection = sc.nextByte();
                            switch (selection) {
                                case 1:
                                    String query = "UPDATE students SET " +
                                            "last_name = '" + lastName + "'," +
                                            "middle_name = '" + middleName + "'," +
                                            "first_name = '" + firstName + "'," +
                                            "gender = '" + gender + "'," +
                                            "date_of_birth = '" + dateOfBirth + "'," +
                                            "birthplace = '" + birthplace + "'," +
                                            "contact_number = '" + contactNumber + "'," +
                                            "email = '" + email + "'," +
                                            "address = '" + address + "'" +
                                            "WHERE student_id = '" + selectionStudentID + "';";
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
                            System.out.print(StringPrefix.errorHead() + "Vui lòng nhập lại: ");
                            sc.next();
                            continue;
                        }
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void menuStudentsRemove() {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            while (true) {
                System.out.print(StringPrefix.inputHead() + "Chọn MSSV cần xóa: ");
                int selectionStudentID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery("SELECT student_id FROM students ORDER BY student_id");
                        selectionStudentID = sc.nextInt();
                        while (resultSet.next()) {
                            int studentID = resultSet.getInt("student_id");
                            if (selectionStudentID == studentID) {
                                success = true;
                                break;
                            }
                        }
                        if (success) {
                            break;
                        } else {
                            System.out.print("Không có trong danh sách: ");
                        }
                    } catch (InputMismatchException e) {
                        System.out.print("Vui lòng nhập đúng số thứ tự: ");
                        sc.next();
                    }
                }

                System.out.println();
                System.out.println("Xác nhận xóa sinh viên thứ " + selectionStudentID + ":");
                System.out.println("1. Xác nhận");
                System.out.println("2. Nhập lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                String query = "DELETE FROM students WHERE student_id = " + selectionStudentID + "";
                                sqlOperations.removeRows(query);
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}