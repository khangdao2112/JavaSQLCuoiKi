package app.program;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public class SQLOperations extends AppMainHandler {
    public boolean varcharLengthExceedCheck(String string, int length) {
        if (string.length() > length) {
            return true;
        }
        else return false;
    }

    public void insertRows(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            System.out.println("✅ Thêm thành công");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void editRows(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            System.out.println("✅ Sửa thành công");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void removeRows(String query) {
        try {
            PreparedStatement statement = connection.prepareStatement(query);
            statement.executeUpdate();
            System.out.println("✅ Xóa thành công");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
