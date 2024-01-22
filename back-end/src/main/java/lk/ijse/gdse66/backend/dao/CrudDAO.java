package lk.ijse.gdse66.backend.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO{

    boolean save(T dto, Connection connection);

    boolean update(T dto, Connection connection);

    boolean delete(T dto, Connection connection);

    T search(String id, Connection connection);

    List<T> getAll(Connection connection) throws SQLException;


}
