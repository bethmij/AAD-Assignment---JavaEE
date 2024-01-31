package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<CustomerEntity> {
    @Override
    boolean save(CustomerEntity dto) throws SQLException;

    @Override
    boolean update(CustomerEntity dto) throws SQLException;

    @Override
    boolean delete(String id) throws SQLException;

    @Override
    CustomerEntity search(String id) throws SQLException;

    @Override
    List<CustomerEntity> getAll() throws SQLException;

    @Override
    List<String> getIDList() throws SQLException;
}
