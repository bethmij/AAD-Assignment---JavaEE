package lk.ijse.gdse66.backend.dao;

import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO{

    boolean save(Connection connection, T dto) throws SQLException;

    boolean update(Connection connection, T dto) throws SQLException;

    boolean delete(Connection connection, String id) throws SQLException;

    T search(Connection connection, String id) throws SQLException;

    int count(Connection connection) throws SQLException;

    List<T> getAll(Connection connection) throws SQLException;

    List<String> getIDList(Connection connection) throws SQLException;
}
