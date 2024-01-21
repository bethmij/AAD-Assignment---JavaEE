package lk.ijse.gdse66.backend.util;


import org.apache.commons.dbcp.BasicDataSource;


import java.sql.*;

public class CrudUtil {

    public static<T> T execute(String sql, Connection connection, Object... params) throws SQLException {

            PreparedStatement pstm = connection.prepareStatement(sql);

            for (int i = 0; i < params.length; i++) {
                pstm.setObject((i + 1), params[i]);
            }

            if (sql.trim().startsWith("SELECT")) {
                return (T) pstm.executeQuery();
            }else {
                return (T) (Boolean)(pstm.executeUpdate()>0);
            }

    }


}