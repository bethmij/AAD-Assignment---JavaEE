package lk.ijse.gdse66.backend.dao;

import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO{

    boolean save(Connection connection, T dto);

    boolean update(Connection connection, T dto);

    boolean delete(Connection connection, T dto);

    T search(Connection connection, String id);

    List<T> getAll(Connection connection) throws SQLException;


}
