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

public class MenuEnrollments extends AppMainHandler {
    private Statement statement = connection.createStatement();
    private SQLOperations sqlOperations = new SQLOperations();

    private int studentID;
    private int courseID;
    private int credits;
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

        public void menuEnrollmentsInsert() {
            try {
                Scanner sc = new Scanner(System.in);
                sc.useDelimiter("\\n");
                while (true) {
                    System.out.print(StringPrefix.inputHead() + "Nhập MSSV: ");
                    while (true) {
                        try {
                            boolean success = false;
                            ResultSet resultSet = statement.executeQuery("SELECT student_id FROM enrollments GROUP BY student_id ORDER BY student_id");
                            studentID = sc.nextInt();
                            while (resultSet.next()) {
                                int studentID = resultSet.getInt("student_id");
                                if (studentID == studentID) {
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
                    System.out.print(StringPrefix.inputHead() + "Chọn ID khóa: ");
                    while (true) {
                        try {
                            boolean success = false;
                            ResultSet resultSet = statement.executeQuery("SELECT course_id FROM enrollments GROUP BY course_id ORDER BY course_id");
                            courseID = sc.nextInt();
                            while (resultSet.next()) {
                                int courseID = resultSet.getInt("course_id");
                                if (courseID == courseID) {
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
                    }
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
                int selectionStudentID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery("SELECT student_id FROM enrollments GROUP BY student_id ORDER BY student_id");
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

                queryForSelection = """
                        SELECT
                            CONCAT(students.last_name,' ',students.middle_name,' ',students.first_name) AS 'Họ và tên',
                            courses.course_name AS 'Tên khóa',
                            courses.course_id AS 'ID khóa',
                            grade
                        FROM enrollments
                        JOIN students ON enrollments.student_id = students.student_id
                        JOIN courses ON enrollments.course_id = courses.course_id
                        WHERE students.student_id =\s""" + selectionStudentID + """
                        """;

                printTableFromQuery(queryForSelection);
                System.out.print(StringPrefix.inputHead() + "Chọn ID khóa: ");
                int selectionCourseID;
                while (true) {
                    try {
                        boolean success = false;
                        ResultSet resultSet = statement.executeQuery(queryForSelection);
                        selectionCourseID = sc.nextInt();
                        while (resultSet.next()) {
                            int courseID = resultSet.getInt("ID khóa");
                            if (selectionCourseID == courseID) {
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

                queryForSelection = """
                        SELECT
                            CONCAT(students.last_name,' ',students.middle_name,' ',students.first_name) AS 'Họ và tên',
                            courses.course_name AS 'Tên khóa',
                            courses.course_id AS 'ID khóa',
                            grade
                        FROM enrollments
                        JOIN students ON enrollments.student_id = students.student_id
                        JOIN courses ON enrollments.course_id = courses.course_id
                        WHERE students.student_id = """ + selectionStudentID + """
                        \nAND courses.course_id = """ + selectionCourseID;
                printTableFromQuery(queryForSelection);
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
                                String query = "DELETE FROM enrollments WHERE student_id = " + selectionStudentID + " AND course_id = " + selectionCourseID;
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

    public void menuEnrollmentSelectEdit(int studentID, int courseID) {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        while (true) {
//                MainProgram.printTable("students");
//
//                System.out.println(MainProgram.inputHead() + "Nhập MSSV: ");
//                while (true) {
//                    try {
//                        boolean success = false;
//                        ResultSet resultSet = statement.executeQuery("SELECT student_id FROM enrollments GROUP BY student_id ORDER BY student_id");
//                        studentID = sc.nextInt();
//                        while (resultSet.next()) {
//                            int studentID = resultSet.getInt("student_id");
//                            if (studentID == studentID) {
//                                success = true;
//                                break;
//                            }
//                        }
//                        if (success) {
//                            break;
//                        } else {
//                            System.out.print("Không có trong danh sách: ");
//                        }
//                    } catch (InputMismatchException e) {
//                        System.out.print("Vui lòng nhập đúng số thứ tự: ");
//                        sc.next();
//                    }
//                }
//
//                System.out.println(MainProgram.inputHead() + "Nhập ID khóa: ");
//                while (true) {
//                    try {
//                        boolean success = false;
//                        ResultSet resultSet = statement.executeQuery("SELECT course_id FROM enrollments GROUP BY course_id ORDER BY course_id");
//                        courseID = sc.nextInt();
//                        while (resultSet.next()) {
//                            int courseID = resultSet.getInt("course_id");
//                            if (courseID == courseID) {
//                                success = true;
//                                break;
//                            }
//                        }
//                        if (success) {
//                            break;
//                        } else {
//                            System.out.print("Không có trong danh sách: ");
//                        }
//                    } catch (InputMismatchException e) {
//                        System.out.print("Vui lòng nhập đúng số thứ tự: ");
//                        sc.next();
//                    }
//                }
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
            System.out.println(StringPrefix.bulletHead() + "ID Khóa: " + courseID);
            System.out.println(StringPrefix.bulletHead() + "Điểm: " + grade);
            System.out.println("1. Xác nhận thay đổi");
            System.out.println("2. Nhập lại");
            System.out.println("0. Hủy");
            while (true) {
                try {
                    byte selection = sc.nextByte();
                    switch (selection) {
                        case 1:
                            String query = "UPDATE enrollments SET\n" +
                                        "student_id = " + studentID + ",\n" +
                                        "course_id = " + courseID + ",\n" +
                                        "grade = " + grade + "\n" +
                                        "WHERE student_id = " + studentID + " AND course_id = " + courseID;
                            sqlOperations.editRows(query);
                            System.out.print("Nhấn enter để tiếp tục: ");
                            sc.nextLine();
                            return;
                        case 2:
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
    }

    public void printTableFromQuery(String query) throws SQLException{
       ResultSet resultSet = statement.executeQuery(query);
       DBTablePrinter.printResultSet(resultSet);
    }

}