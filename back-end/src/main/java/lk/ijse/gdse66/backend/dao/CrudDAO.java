package lk.ijse.gdse66.backend.dao;

import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CrudDAO<T> extends SuperDAO{

    boolean save( T dto) throws SQLException;

    boolean update(T dto) throws SQLException;

    boolean delete( String id) throws SQLException;

    T search(String id) throws SQLException;

    int count() throws SQLException;

    List<T> getAll() throws SQLException;

    List<String> getIDList() throws SQLException;
}
