package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.CrudDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public interface CustomerDAO extends CrudDAO<CustomerEntity> {
    @Override
    boolean save(CustomerEntity dto, Connection connection);

    @Override
    boolean update(CustomerEntity dto, Connection connection);

    @Override
    boolean delete(CustomerEntity dto, Connection connection);

    @Override
    CustomerEntity search(String id, Connection connection);

    @Override
    List<CustomerEntity> getAll(Connection connection) throws SQLException;
}
