package app.program.menu;

import app.program.AppMainHandler;
import app.program.MainProgram;
import app.program.SQLOperations;
import app.program.data.StudentsColumns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.InputMismatchException;
import java.util.Scanner;

public class menuStudents extends AppMainHandler {

    public void menuStudentsInsert() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        SQLOperations sqlOperations = new SQLOperations();
        StudentsColumns studentsColumns = new StudentsColumns();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            Statement statement = MainProgram.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT student_id FROM javasqlcuoiki.students ORDER BY student_id DESC LIMIT 1");
            resultSet.next();
            int studentIDNewest = resultSet.getInt("student_id");
            studentIDNewest++;

            System.out.println("Nhập thông tin sinh viên thứ " + studentIDNewest);
            while (true) {
                System.out.print(MainProgram.inputHead() + "Nhập tên họ: ");
                while (true) {
                    studentsColumns.lastName = sc.nextLine();
                    if (studentsColumns.lastName.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.lastName, 50)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }

                System.out.print(MainProgram.inputHead() + "Nhập tên lót: ");
                while (true) {
                    studentsColumns.middleName = sc.nextLine();
                    if (studentsColumns.middleName.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.middleName, 50)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }

                System.out.print(MainProgram.inputHead() + "Nhập tên: ");
                while (true) {
                    studentsColumns.firstName = sc.nextLine();
                    if (studentsColumns.firstName.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.firstName, 50)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }


                System.out.print(MainProgram.inputHead() + "Nhập giới tính (Nam=M, Nữ=F): ");
                while (true) {
                    try {
                        studentsColumns.gender = sc.next().charAt(0);
                        if (studentsColumns.gender == 'M' || studentsColumns.gender == 'F') {
                            break;
                        } else {
                            System.out.print(MainProgram.errorHead() + "Vui lòng nhập 'M' cho Nam và 'F' cho nữ: ");
                        }
                    } catch (StringIndexOutOfBoundsException e) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống, vui lòng nhập lại: ");
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }

                System.out.print(MainProgram.inputHead() + "Nhập ngày sinh (dd/MM/yyyy): ");
                while (true) {
                    try {
                        String localDateString = sc.next();
                        studentsColumns.dateOfBirth = LocalDate.parse(localDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                        break;
                    } catch (Exception e) {
                        System.out.print(MainProgram.errorHead() + "Vui lòng nhập đúng format dd/MM/yyyy: ");
                        continue;
                    }
                }

                System.out.print(MainProgram.inputHead() + "Nhập quê quán: ");
                while (true) {
                    studentsColumns.birthplace = sc.next();
                    sc.nextLine();
                    if (studentsColumns.birthplace.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.birthplace, 128)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 128 ký tự: ");
                    } else break;
                }

                System.out.print(MainProgram.inputHead() + "Nhập số điện thoại: ");
                while (true) {
                    studentsColumns.contactNumber = sc.next();
                    sc.nextLine();
                    if (studentsColumns.contactNumber.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.contactNumber, 20)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 20 ký tự: ");
                    } else break;
                }

                System.out.print(MainProgram.inputHead() + "Nhập email: ");
                while (true) {
                    studentsColumns.email = sc.next();
                    sc.nextLine();
                    if (studentsColumns.email.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.email, 100)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                    } else break;
                }

                System.out.print(MainProgram.inputHead() + "Nhập địa chỉ: ");
                while (true) {
                    studentsColumns.address = sc.next();
                    sc.nextLine();
                    if (studentsColumns.address.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.address, 255)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 255 ký tự: ");
                    } else break;
                }

                System.out.println();
                System.out.println("Thông tin sinh viên thứ " + studentIDNewest + ":");
                studentsColumns.fullName = studentsColumns.lastName + " " + studentsColumns.middleName + " " + studentsColumns.firstName;
                System.out.println(MainProgram.bulletHead() + "Họ và tên: " + studentsColumns.fullName);
                System.out.print(MainProgram.bulletHead() + "Giới tính: ");
                if (studentsColumns.gender == 'M') {
                    System.out.println("Nam");
                } else {
                    System.out.println("Nữ");
                }
                System.out.println(MainProgram.bulletHead() + "Ngày sinh: " + studentsColumns.dateOfBirth.format(dateTimeFormatter));
                System.out.println(MainProgram.bulletHead() + "Nơi sinh: " + studentsColumns.birthplace);
                System.out.println(MainProgram.bulletHead() + "Số điện thoại: " + studentsColumns.contactNumber);
                System.out.println(MainProgram.bulletHead() + "Email: " + studentsColumns.email);
                System.out.println(MainProgram.bulletHead() + "Địa chỉ: " + studentsColumns.address);
                System.out.println("1. Xác nhận");
                System.out.println("2. Nhập lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                String query = "INSERT INTO javasqlcuoiki.students (last_name, middle_name, first_name, gender, date_of_birth, birthplace, contact_number, email, address) VALUE" +
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
                                System.out.print(MainProgram.errorHead() + "Vui lòng nhập lại: ");
                                continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.print(MainProgram.errorHead() + "Vui lòng nhập lại: ");
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
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        SQLOperations sqlOperations = new SQLOperations();
        StudentsColumns studentsColumns = new StudentsColumns();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            while (true) {
                System.out.print(MainProgram.inputHead() + "Chọn MSSV cần sửa: ");
                int selectionStudentID;
                while (true) {
                    try {
                        boolean success = false;
                        Statement statement = MainProgram.connection.createStatement();
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
                    System.out.print(MainProgram.inputHead() + "Nhập tên họ: ");
                    while (true) {
                        studentsColumns.lastName = sc.nextLine();
                        if (studentsColumns.lastName.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.lastName, 50)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                        } else break;
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập tên lót: ");
                    while (true) {
                        studentsColumns.middleName = sc.nextLine();
                        if (studentsColumns.middleName.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.middleName, 50)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                        } else break;
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập tên: ");
                    while (true) {
                        studentsColumns.firstName = sc.nextLine();
                        if (studentsColumns.firstName.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.firstName, 50)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                        } else break;
                    }


                    System.out.print(MainProgram.inputHead() + "Nhập giới tính (Nam=M, Nữ=F): ");
                    while (true) {
                        try {
                            studentsColumns.gender = sc.next().charAt(0);
                            if (studentsColumns.gender == 'M' || studentsColumns.gender == 'F') {
                                break;
                            } else {
                                System.out.print(MainProgram.errorHead() + "Vui lòng nhập 'M' cho Nam và 'F' cho nữ: ");
                            }
                        } catch (StringIndexOutOfBoundsException e) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống, vui lòng nhập lại: ");
                        } catch (Exception e) {
                            throw new RuntimeException(e);
                        }
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập ngày sinh (dd/MM/yyyy): ");
                    while (true) {
                        try {
                            String localDateString = sc.next();
                            studentsColumns.dateOfBirth = LocalDate.parse(localDateString, DateTimeFormatter.ofPattern("dd/MM/yyyy"));
                            break;
                        } catch (Exception e) {
                            System.out.print(MainProgram.errorHead() + "Vui lòng nhập đúng format dd/MM/yyyy: ");
                            continue;
                        }
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập quê quán: ");
                    while (true) {
                        studentsColumns.birthplace = sc.next();
                        sc.nextLine();
                        if (studentsColumns.birthplace.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.birthplace, 128)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 128 ký tự: ");
                        } else break;
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập số điện thoại: ");
                    while (true) {
                        studentsColumns.contactNumber = sc.next();
                        sc.nextLine();
                        if (studentsColumns.contactNumber.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.contactNumber, 20)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 20 ký tự: ");
                        } else break;
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập email: ");
                    while (true) {
                        studentsColumns.email = sc.next();
                        sc.nextLine();
                        if (studentsColumns.email.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.email, 100)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                        } else break;
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập địa chỉ: ");
                    while (true) {
                        studentsColumns.address = sc.next();
                        sc.nextLine();
                        if (studentsColumns.address.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(studentsColumns.address, 255)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 255 ký tự: ");
                        } else break;
                    }

                    System.out.println();
                    System.out.println("Thông tin sinh viên thứ " + selectionStudentID + ":");
                    studentsColumns.fullName = studentsColumns.lastName + " " + studentsColumns.middleName + " " + studentsColumns.firstName;
                    System.out.println(MainProgram.bulletHead() + "Họ và tên: " + studentsColumns.fullName);
                    System.out.print(MainProgram.bulletHead() + "Giới tính: ");
                    if (studentsColumns.gender == 'M') {
                        System.out.println("Nam");
                    } else {
                        System.out.println("Nữ");
                    }
                    System.out.println(MainProgram.bulletHead() + "MSSV: " + selectionStudentID);
                    System.out.println(MainProgram.bulletHead() + "Ngày sinh: " + studentsColumns.dateOfBirth.format(dateTimeFormatter));
                    System.out.println(MainProgram.bulletHead() + "Nơi sinh: " + studentsColumns.birthplace);
                    System.out.println(MainProgram.bulletHead() + "Số điện thoại: " + studentsColumns.contactNumber);
                    System.out.println(MainProgram.bulletHead() + "Email: " + studentsColumns.email);
                    System.out.println(MainProgram.bulletHead() + "Địa chỉ: " + studentsColumns.address);
                    System.out.println("1. Xác nhận");
                    System.out.println("2. Nhập lại");
                    System.out.println("0. Hủy");
                    while (true) {
                        try {
                            byte selection = sc.nextByte();
                            switch (selection) {
                                case 1:
                                    String query = "UPDATE students SET " +
                                            "last_name = '" + studentsColumns.lastName + "'," +
                                            "middle_name = '" + studentsColumns.middleName + "'," +
                                            "first_name = '" + studentsColumns.firstName + "'," +
                                            "gender = '" + studentsColumns.gender + "'," +
                                            "date_of_birth = '" + studentsColumns.dateOfBirth + "'," +
                                            "birthplace = '" + studentsColumns.birthplace + "'," +
                                            "contact_number = '" + studentsColumns.contactNumber + "'," +
                                            "email = '" + studentsColumns.email + "'," +
                                            "address = '" + studentsColumns.address + "'" +
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
                                    System.out.print(MainProgram.errorHead() + "Vui lòng nhập lại: ");
                                    continue;
                            }
                            break;
                        } catch (InputMismatchException e) {
                            System.out.print(MainProgram.errorHead() + "Vui lòng nhập lại: ");
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
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        SQLOperations sqlOperations = new SQLOperations();
        StudentsColumns studentsColumns = new StudentsColumns();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        try {
            while (true) {
                System.out.print(MainProgram.inputHead() + "Chọn MSSV cần xóa: ");
                int selectionStudentID;
                while (true) {
                    try {
                        boolean success = false;
                        Statement statement = MainProgram.connection.createStatement();
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
                                System.out.print(MainProgram.errorHead() + "Vui lòng nhập lại: ");
                                continue;
                        }
                        break;
                    } catch (InputMismatchException e) {
                        System.out.print(MainProgram.errorHead() + "Vui lòng nhập lại: ");
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