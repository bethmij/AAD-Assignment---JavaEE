package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<CustomerEntity> {
    @Override
    boolean save(Connection connection, CustomerEntity dto) throws SQLException;

    @Override
    boolean update(Connection connection, CustomerEntity dto) throws SQLException;

    @Override
    boolean delete(Connection connection, String id) throws SQLException;

    @Override
    CustomerEntity search(Connection connection, String id) throws SQLException;

    @Override
    List<CustomerEntity> getAll(Connection connection) throws SQLException;

    @Override
    List<String> getIDList(Connection connection) throws SQLException;
}
