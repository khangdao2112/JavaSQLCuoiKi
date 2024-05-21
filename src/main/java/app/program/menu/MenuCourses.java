package app.program.menu;

import app.program.AppMainHandler;
import app.program.MainProgram;
import app.program.SQLOperations;
import app.program.StringPrefix;
import util.DBTablePrinter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.InputMismatchException;
import java.util.Scanner;

public class MenuCourses extends AppMainHandler {
    private Statement statement = connection.createStatement();
    private SQLOperations sqlOperations = new SQLOperations();

    private String courseID;
    private String courseName;
    private String courseDescription;
    private int credits;
    private String instructorID;
    private String classID;

    public MenuCourses() throws SQLException {

    }

    public void menuSelection() {
        Scanner sc = new Scanner(System.in);
        while(true) {
            System.out.println();
            System.out.println("Danh sách các môn học");
            MainProgram.printTable("courses");
            System.out.print("""
                    │ Sử dụng các phím số để chọn các chức năng tương ứng
                    │ 1. Thêm môn học
                    │ 2. Sửa môn học
                    │ 3. Xóa môn học
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

    public void menuCoursesInsert() {
        try {
            Scanner sc = new Scanner(System.in);
            sc.useDelimiter("\\n");
            while (true) {
//                while (true) {
//                    courseID = sc.next();
//                    if (courseID.isEmpty()) {
//                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
//                    } else if (sqlOperations.varcharLengthExceedCheck(courseID, 10)) {
//                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 10 ký tự: ");
//                    } else break;
//                }

                System.out.print(StringPrefix.inputHead() + "Nhập mã môn: ");
                while (true) {
                    try {
                        boolean fail = false;
                        ResultSet resultSet = statement.executeQuery("SELECT course_id FROM courses");
                        courseID = sc.next();
                        if (courseID.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        }
                        else {
                            while (resultSet.next()) {
                                String courseIDResult = resultSet.getString("course_id");
                                if (courseID.equals(courseIDResult)) {
                                    fail = true;
                                    break;
                                }
                            }
                            if (fail) {
                                System.out.print(StringPrefix.errorHead() + "Nội dung trùng hoặc không có trong danh sách: ");
                            } else {
                                if (sqlOperations.varcharLengthExceedCheck(courseID,10)) {
                                    System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 10 ký tự: ");
                                } else break;
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng số thứ tự: ");
                        sc.next();
                    }
                }

                System.out.print(StringPrefix.inputHead() + "Nhập tên môn: ");
                while (true) {
                    courseName = sc.nextLine();
                    if (courseName.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else if (sqlOperations.varcharLengthExceedCheck(courseName, 50)) {
                        System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập mô tả môn: ");
                while (true) {
                    courseDescription = sc.nextLine();
                    if (courseDescription.isEmpty()) {
                        System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                    } else break;
                }

                System.out.print(StringPrefix.inputHead() + "Nhập tín chỉ (1-3): ");
                try {
                    while (true) {
                        credits = sc.nextInt();
                        if (credits >= 1 && credits <= 3) {
                            break;
                        }
                        else {
                            System.out.print(StringPrefix.errorHead() + "Vui lòng nhập từ 1-3 tín chỉ: ");
                        }
                    }
                } catch (InputMismatchException e) {
                    System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng số tín chỉ: ");
                    sc.next();
                }

                ResultSet classTable = statement.executeQuery(
                        "SELECT class_id AS 'Lớp', department_name AS 'Khoa' FROM classes\n" +
                        "JOIN javasqlcuoiki.majors m on m.major_id = classes.major_id\n" +
                        "JOIN javasqlcuoiki.departments d on d.department_id = m.department_id"
                );
                DBTablePrinter.printResultSet(classTable);
                System.out.print(StringPrefix.inputHead() + "Nhập lớp: ");
                String selectionClassID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery("SELECT class_id FROM classes");
                        selectionClassID = sc.next();
                        while (resultSet.next()) {
                            classID = resultSet.getString("class_id");
                            if (classID.equals(selectionClassID)) {
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

                ResultSet instructorTable = statement.executeQuery(
                        "SELECT\n" +
                                "    instructor_id AS 'Mã số giảng viên',\n" +
                                "    CONCAT(last_name,' ',middle_name,' ',first_name) AS 'Họ và tên giảng viên'\n" +
                                "FROM instructors"
                );
                DBTablePrinter.printResultSet(instructorTable);
                System.out.print(StringPrefix.inputHead() + "Nhập mã số giảng viên: ");
                String selectionInstructorID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery("SELECT instructor_id, CONCAT(last_name,' ',middle_name,' ',first_name) FROM instructors ORDER BY instructor_id");
                        selectionInstructorID = sc.next();
                        while (resultSet.next()) {
                            instructorID = resultSet.getString("instructor_id");
                            if (instructorID.equals(selectionInstructorID)) {
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

                System.out.println();
                System.out.println(StringPrefix.bulletHead() + "Mã môn: " + courseID);
                System.out.println(StringPrefix.bulletHead() + "Tên môn: " + courseName);
                System.out.println(StringPrefix.bulletHead() + "Thông tin môn: " + courseDescription);
                System.out.println(StringPrefix.bulletHead() + "Tín chỉ: " + credits);
//                ResultSet instructorFullnameTable = statement.executeQuery(
//                        "SELECT\n" +
//                                "    CONCAT(last_name,' ',middle_name,' ',first_name)\n" +
//                                "FROM instructors WHERE instructor_id = " + instructorID
//                );
//                instructorFullnameTable.next();
//                String instructorFullname = instructorFullnameTable.getString("CONCAT(last_name,' ',middle_name,' ',first_name)");
//                System.out.println(StringPrefix.bulletHead() + "Giảng viên: " + instructorFullname);
                System.out.println(StringPrefix.bulletHead() + "Lớp: " + classID);
                System.out.println("1. Xác nhận");
                System.out.println("2. Nhập lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                String query = "INSERT INTO courses (course_id, course_name, course_description, credits, instructor_id, class_id) VALUE (\n"
                                        + "'" + courseID + "',\n"
                                        + "'" + courseName + "',\n"
                                        + "'" + courseDescription + "',\n"
                                        + "'" + credits + "',\n"
                                        + "'" + instructorID + "',\n"
                                        + "'" + classID + "')";
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
                System.out.print(StringPrefix.inputHead() + "Chọn môn cần sửa: ");
                String selectionCoursesID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery("SELECT course_id FROM courses");
                        selectionCoursesID = sc.next();
                        while (resultSet.next()) {
                            String courseID = resultSet.getString("course_id");
                            if (courseID.equals(selectionCoursesID)) {
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

                while (true) {
                    System.out.print(StringPrefix.inputHead() + "Nhập tên môn: ");
                    while (true) {
                        courseName = sc.nextLine();
                        if (courseName.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else if (sqlOperations.varcharLengthExceedCheck(courseName, 50)) {
                            System.out.print(StringPrefix.errorHead() + "Nội dung phải nhỏ hơn 100 ký tự: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập mô tả môn: ");
                    while (true) {
                        courseDescription = sc.nextLine();
                        if (courseDescription.isEmpty()) {
                            System.out.print(StringPrefix.errorHead() + "Không được để trống: ");
                        } else break;
                    }

                    System.out.print(StringPrefix.inputHead() + "Nhập tín chỉ (1-3): ");
                    try {
                        while (true) {
                            credits = sc.nextInt();
                            if (credits >= 1 && credits <= 3) {
                                break;
                            }
                            else {
                                System.out.print(StringPrefix.errorHead() + "Vui lòng nhập từ 1-3 tín chỉ: ");
                            }
                        }
                    } catch (InputMismatchException e) {
                        System.out.print(StringPrefix.errorHead() + "Vui lòng nhập đúng số tín chỉ: ");
                        sc.next();
                    }

                    ResultSet classTable = statement.executeQuery(
                            "SELECT class_id AS 'Lớp', department_name AS 'Khoa' FROM classes\n" +
                                    "JOIN javasqlcuoiki.majors m on m.major_id = classes.major_id\n" +
                                    "JOIN javasqlcuoiki.departments d on d.department_id = m.department_id"
                    );
                    DBTablePrinter.printResultSet(classTable);
                    System.out.print(StringPrefix.inputHead() + "Nhập lớp: ");
                    String selectionClassID;
                    while (true) {
                        try {
                            boolean success = false;
                            ResultSet resultSet = statement.executeQuery("SELECT class_id FROM classes");
                            selectionClassID = sc.next();
                            while (resultSet.next()) {
                                classID = resultSet.getString("class_id");
                                if (classID.equals(selectionClassID)) {
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

                    ResultSet instructorTable = statement.executeQuery(
                            "SELECT\n" +
                                    "    instructor_id AS 'Mã số giảng viên',\n" +
                                    "    CONCAT(last_name,' ',middle_name,' ',first_name) AS 'Họ và tên giảng viên'\n" +
                                    "FROM instructors"
                    );
                    DBTablePrinter.printResultSet(instructorTable);
                    System.out.print(StringPrefix.inputHead() + "Nhập mã số giảng viên: ");
                    String selectionInstructorID;
                    while (true) {
                        try {
                            boolean success = false;
                            ResultSet resultSet = statement.executeQuery("SELECT instructor_id, CONCAT(last_name,' ',middle_name,' ',first_name) FROM instructors ORDER BY instructor_id");
                            selectionInstructorID = sc.next();
                            while (resultSet.next()) {
                                instructorID = resultSet.getString("instructor_id");
                                if (instructorID.equals(selectionInstructorID)) {
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


                    System.out.println();
                    System.out.println(StringPrefix.bulletHead() + "Mã môn: " + selectionCoursesID);
                    System.out.println(StringPrefix.bulletHead() + "Tên môn: " + courseName);
                    System.out.println(StringPrefix.bulletHead() + "Thông tin môn: " + courseDescription);
                    System.out.println(StringPrefix.bulletHead() + "Tín chỉ: " + credits);
                    System.out.println(StringPrefix.bulletHead() + "Lớp: " + classID);
                    System.out.println("1. Xác nhận");
                    System.out.println("2. Nhập lại");
                    System.out.println("0. Hủy");
                    while (true) {
                        try {
                            byte selection = sc.nextByte();
                            switch (selection) {
                                case 1:
                                    String query = "UPDATE courses SET" +
                                            " course_name = '" + courseName +"',\n" +
                                            " course_description = '" + courseDescription + "',\n" +
                                            " credits = "+ credits + ",\n" +
                                            " instructor_id = '" + instructorID +"',\n" +
                                            " class_id = '" + classID + "'\n" +
                                            "WHERE course_id = '" + selectionCoursesID + "'";
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
                System.out.print(StringPrefix.inputHead() + "Chọn môn cần xóa: ");
                String selectionCoursesID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery("SELECT course_id FROM courses");
                        selectionCoursesID = sc.next();
                        while (resultSet.next()) {
                            String courseID = resultSet.getString("course_id");
                            if (courseID.equals(selectionCoursesID)) {
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

                System.out.println();
                System.out.println("Xác nhận xóa môn " + selectionCoursesID + ":");
                System.out.println("1. Xác nhận");
                System.out.println("2. Nhập lại");
                System.out.println("0. Hủy");
                while (true) {
                    try {
                        byte selection = sc.nextByte();
                        switch (selection) {
                            case 1:
                                String query = "DELETE FROM courses WHERE course_id = '" + selectionCoursesID + "'";
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
                    }
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}