package lk.ijse.gdse66.backend.util;


import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class CrudUtil {
    static String url = "jdbc:mysql://localhost:3306/company";
    static String username = "root";
    static String password = "1234";
    
    public static <T> T execute (String sql, HttpServletResponse response, Object...params) {

        Connection connection = null;
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            connection = DriverManager.getConnection(url, username, password);
            PreparedStatement pstm = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstm.setObject((i + 1), params[i]);
            }

            if (sql.startsWith("SELECT"))
                return (T) pstm.executeQuery();
            else
                return (T) (Boolean) (pstm.executeUpdate() > 0);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
            try {
                response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getLocalizedMessage());
            } catch (IOException ex) {
                ex.printStackTrace();
            }
        }
        return null;
    }


}
