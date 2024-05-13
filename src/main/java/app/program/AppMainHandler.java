package app.program;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Scanner;

public abstract class AppMainHandler {
    protected String username;
    protected String password;
    protected static Connection connection;

    public void login() {
        Scanner sc = new Scanner(System.in);
        sc.useDelimiter("\\n");
        System.out.println("Đăng nhập vào database");
        while(true) {
            System.out.print("Username: ");
            this.username = sc.next();
            sc.nextLine();
            System.out.print("Password: ");
            this.password = sc.next();
            sc.nextLine();
            if (connectionTest()) {
                System.out.println("\n🟢 Kết nối database thành công");
                break;
            }
            else {
                System.out.println("\n🔴 Không kết nối được database, kiểm tra lại thông tin đăng nhập");
            }
        }
    }

    public void closeConnection() {
        try {
            connection.close();
            System.out.println("\nĐã ngắt kết nối tới database");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean connectionTest() {
        try {
            String url = "jdbc:mysql://localhost:3306/javasqlcuoiki";
            connection = DriverManager.getConnection(url, this.username, this.password);
            return true;
        } catch (SQLException e) {
            return false;
        }
    }
}
