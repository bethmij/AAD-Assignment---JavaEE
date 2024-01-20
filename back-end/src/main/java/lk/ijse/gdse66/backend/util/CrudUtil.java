package lk.ijse.gdse66.backend.util;


import jakarta.servlet.http.HttpServletResponse;
import org.apache.commons.dbcp.BasicDataSource;


import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {

    public static <T> T execute(String sql, BasicDataSource source,  Object... params){

        try (Connection connection = source.getConnection()) {
            PreparedStatement pstm = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstm.setObject((i + 1), params[i]);
            }

            if (sql.startsWith("SELECT"))
                return (T) pstm.executeQuery();
            else
                return (T) (Boolean) (pstm.executeUpdate() > 0);

        } catch (SQLException e) {
            return null;
        }
    }
}

