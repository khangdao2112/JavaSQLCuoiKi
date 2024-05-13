package app.program.menu;

import app.program.AppMainHandler;
import app.program.MainProgram;
import app.program.SQLOperations;
import app.program.data.CoursesColumns;
import app.program.data.StudentsColumns;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class menuCourses extends AppMainHandler {
    public void menuCoursesInsert() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        SQLOperations sqlOperations = new SQLOperations();
        CoursesColumns coursesColumns = new CoursesColumns();
        try {
            Statement statement = MainProgram.connection.createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT course_id FROM courses ORDER BY course_id DESC LIMIT 1");
            resultSet.next();
            int coursesIDNewest = resultSet.getInt("course_id");
            coursesIDNewest++;

            System.out.println("Nhập thông tin khóa thứ " + coursesIDNewest);
            while (true) {
                System.out.print(MainProgram.inputHead() + "Nhập tên khóa: ");
                while (true) {
                    coursesColumns.courseName = sc.nextLine();
                    if (coursesColumns.courseName.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(coursesColumns.courseName, 50)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                    } else break;
                }

                System.out.print(MainProgram.inputHead() + "Nhập mô tả khóa: ");
                while (true) {
                    coursesColumns.courseDescription = sc.nextLine();
                    if (coursesColumns.courseDescription.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else break;
                }

                System.out.print(MainProgram.inputHead() + "Nhập tên giảng viên: ");
                while (true) {
                    coursesColumns.instructor = sc.nextLine();
                    if (coursesColumns.instructor.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(coursesColumns.instructor, 50)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }

                System.out.print(MainProgram.inputHead() + "Nhập tên khoa: ");
                while (true) {
                    coursesColumns.department = sc.nextLine();
                    if (coursesColumns.department.isEmpty()) {
                        System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(coursesColumns.department, 50)) {
                        System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                    } else break;
                }

                System.out.println();
                System.out.println("Thông tin khóa thứ " + coursesIDNewest + ":");
                System.out.println(MainProgram.bulletHead() + "Tên khóa: " + coursesColumns.courseName);
                System.out.println(MainProgram.bulletHead() + "Thông tin khóa: " + coursesColumns.courseDescription);
                System.out.println(MainProgram.bulletHead() + "Giảng viên: " + coursesColumns.instructor);
                System.out.println(MainProgram.bulletHead() + "Khoa: " + coursesColumns.department);
                System.out.println("1. Xác nhận");
                System.out.println("2. Nhập lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                String query = "INSERT INTO courses (course_name, course_description, instructor, department)" +
                                        "VALUE (\n" +
                                        "'" + coursesColumns.courseName + "'," +
                                        "'" + coursesColumns.courseDescription + "'," +
                                        "'" + coursesColumns.instructor +"'," +
                                        "'" + coursesColumns.department + "'" +
                                        ")";
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

    public void menuCoursesEdit() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        SQLOperations sqlOperations = new SQLOperations();
        CoursesColumns coursesColumns = new CoursesColumns();
        try {
            while (true) {
                System.out.print(MainProgram.inputHead() + "Chọn khóa cần sửa: ");
                int selectionCoursesID;
                while (true) {
                    try {
                        boolean success = false;
                        Statement statement = MainProgram.connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT course_id FROM courses ORDER BY course_id");
                        selectionCoursesID = sc.nextInt();
                        while (resultSet.next()) {
                            int courseID = resultSet.getInt("course_id");
                            if (selectionCoursesID == courseID) {
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

                System.out.println("Nhập thông tin khóa thứ " + selectionCoursesID);
                while (true) {
                    System.out.print(MainProgram.inputHead() + "Nhập tên khóa: ");
                    while (true) {
                        coursesColumns.courseName = sc.nextLine();
                        if (coursesColumns.courseName.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(coursesColumns.courseName, 50)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                        } else break;
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập mô tả khóa: ");
                    while (true) {
                        coursesColumns.courseDescription = sc.nextLine();
                        if (coursesColumns.courseDescription.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else break;
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập tên giảng viên: ");
                    while (true) {
                        coursesColumns.instructor = sc.nextLine();
                        if (coursesColumns.instructor.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(coursesColumns.instructor, 50)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                        } else break;
                    }

                    System.out.print(MainProgram.inputHead() + "Nhập tên khoa: ");
                    while (true) {
                        coursesColumns.department = sc.nextLine();
                        if (coursesColumns.department.isEmpty()) {
                            System.out.print(MainProgram.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(coursesColumns.department, 50)) {
                            System.out.print(MainProgram.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                        } else break;
                    }

                    System.out.println();
                    System.out.println("Thông tin khóa thứ " + selectionCoursesID + ":");
                    System.out.println(MainProgram.bulletHead() + "Tên khóa: " + coursesColumns.courseName);
                    System.out.println(MainProgram.bulletHead() + "Thông tin khóa: " + coursesColumns.courseDescription);
                    System.out.println(MainProgram.bulletHead() + "Giảng viên: " + coursesColumns.instructor);
                    System.out.println(MainProgram.bulletHead() + "Khoa: " + coursesColumns.department);
                    System.out.println("1. Xác nhận");
                    System.out.println("2. Nhập lại");
                    System.out.println("0. Hủy");
                    while (true) {
                        try {
                            byte selection = sc.nextByte();
                            switch (selection) {
                                case 1:
                                    String query = "UPDATE courses SET" +
                                            " course_name = '" + coursesColumns.courseName +"'," +
                                            " course_description = '" + coursesColumns.courseDescription + "'," +
                                            " instructor = '" + coursesColumns.instructor +"'," +
                                            " department = '" + coursesColumns.department + "'" +
                                            "WHERE course_id = " + selectionCoursesID + "";
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

    public void menuCoursesRemove() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        SQLOperations sqlOperations = new SQLOperations();
        CoursesColumns coursesColumns = new CoursesColumns();
        try {
            while (true) {
                System.out.print(MainProgram.inputHead() + "Chọn khóa cần xóa: ");
                int selectionCoursesID;
                while (true) {
                    try {
                        boolean success = false;
                        Statement statement = MainProgram.connection.createStatement();
                        ResultSet resultSet = statement.executeQuery("SELECT course_id FROM courses ORDER BY course_id");
                        selectionCoursesID = sc.nextInt();
                        while (resultSet.next()) {
                            int courseID = resultSet.getInt("course_id");
                            if (selectionCoursesID == courseID) {
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
                System.out.println("Xác nhận xóa khóa thứ " + selectionCoursesID + ":");
                System.out.println("1. Xác nhận");
                System.out.println("2. Nhập lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                String query = "DELETE FROM courses WHERE course_id = " + selectionCoursesID + "";
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