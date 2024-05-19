package app.program.menu;

import app.program.AppMainHandler;
import app.program.MainProgram;
import app.program.SQLOperations;
import app.program.StringPrefix;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuCourses extends AppMainHandler {
    private Statement statement = connection.createStatement();
    private SQLOperations sqlOperations = new SQLOperations();

    private String courseName;
    private String courseDescription;
    private String instructor;
    private String department;

    public MenuCourses() throws SQLException {

    }

    public void menuSelection() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println();
            System.out.println("Danh sách các khóa học");
            MainProgram.printTable("courses");
            System.out.print("""
                    │ Sử dụng các phím số để chọn các chức năng tương ứng
                    │ 1. Thêm khóa học
                    │ 2. Sửa khóa học
                    │ 3. Xóa khóa học
                    │ 0. Quay về menu
                    """);
            StringPrefix.selectionCursor();
            while (true){
                try {
                    byte selection = sc.nextByte();
                    switch (selection) {
                        case 1:
                            menuCoursesInsert();
                            break;
                        case 2:
                            menuCoursesEdit();
                            break;
                        case 3:
                            menuCoursesRemove();
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

    public void menuCoursesInsert() {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            ResultSet resultSet = statement.executeQuery("SELECT course_id FROM courses ORDER BY course_id DESC LIMIT 1");
            resultSet.next();
            int coursesIDNewest = resultSet.getInt("course_id");
            coursesIDNewest++;

            System.out.println("Nhập thông tin khóa thứ " + coursesIDNewest);
            while (true) {
                System.out.print(StringPrefix.inputHead() + "Nhập tên khóa: ");
                while (true) {
                    courseName = sc.nextLine();
                    if (courseName.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(courseName, 50)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập mô tả khóa: ");
                while (true) {
                    courseDescription = sc.nextLine();
                    if (courseDescription.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập tên giảng viên: ");
                while (true) {
                    instructor = sc.nextLine();
                    if (instructor.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(instructor, 50)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập tên khoa: ");
                while (true) {
                    department = sc.nextLine();
                    if (department.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(department, 50)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                    } else break;
                }

                System.out.println();
                System.out.println("Thông tin khóa thứ " + coursesIDNewest + ":");
                System.out.println(StringPrefix.bulletHead() + "Tên khóa: " + courseName);
                System.out.println(StringPrefix.bulletHead() + "Thông tin khóa: " + courseDescription);
                System.out.println(StringPrefix.bulletHead() + "Giảng viên: " + instructor);
                System.out.println(StringPrefix.bulletHead() + "Khoa: " + department);
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
                                        "'" + courseName + "'," +
                                        "'" + courseDescription + "'," +
                                        "'" + instructor +"'," +
                                        "'" + department + "'" +
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

    public void menuCoursesEdit() {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            while (true) {
                System.out.print(StringPrefix.inputHead() + "Chọn khóa cần sửa: ");
                int selectionCoursesID;
                while (true) {
                    try {
                        boolean success = false;
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
                    System.out.print(StringPrefix.inputHead() + "Nhập tên khóa: ");
                    while (true) {
                        courseName = sc.nextLine();
                        if (courseName.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(courseName, 50)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập mô tả khóa: ");
                    while (true) {
                        courseDescription = sc.nextLine();
                        if (courseDescription.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập tên giảng viên: ");
                    while (true) {
                        instructor = sc.nextLine();
                        if (instructor.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(instructor, 50)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 50 ký tự: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập tên khoa: ");
                    while (true) {
                        department = sc.nextLine();
                        if (department.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(department, 50)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                        } else break;
                    }

                    System.out.println();
                    System.out.println("Thông tin khóa thứ " + selectionCoursesID + ":");
                    System.out.println(StringPrefix.bulletHead() + "Tên khóa: " + courseName);
                    System.out.println(StringPrefix.bulletHead() + "Thông tin khóa: " + courseDescription);
                    System.out.println(StringPrefix.bulletHead() + "Giảng viên: " + instructor);
                    System.out.println(StringPrefix.bulletHead() + "Khoa: " + department);
                    System.out.println("1. Xác nhận");
                    System.out.println("2. Nhập lại");
                    System.out.println("0. Hủy");
                    while (true) {
                        try {
                            byte selection = sc.nextByte();
                            switch (selection) {
                                case 1:
                                    String query = "UPDATE courses SET" +
                                            " course_name = '" + courseName +"'," +
                                            " course_description = '" + courseDescription + "'," +
                                            " instructor = '" + instructor +"'," +
                                            " department = '" + department + "'" +
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

    public void menuCoursesRemove() {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            while (true) {
                System.out.print(StringPrefix.inputHead() + "Chọn khóa cần xóa: ");
                int selectionCoursesID;
                while (true) {
                    try {
                        boolean success = false;
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