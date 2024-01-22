package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.custom.CustomerDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {


    @Override
    public boolean save(Connection connection, CustomerEntity dto) {
        return false;
    }

    @Override
    public boolean update(Connection connection, CustomerEntity dto) {
        return false;
    }

    @Override
    public boolean delete(Connection connection, CustomerEntity dto) {
        return false;
    }

    @Override
    public CustomerEntity search(Connection connection, String id) {
        return null;
    }

    @Override
    public List<CustomerEntity> getAll(Connection connection) throws SQLException {
        List<CustomerEntity> customerList = new ArrayList<>();
        String sql = "SELECT * FROM customer";
        ResultSet resultSet = CrudUtil.execute(sql, connection);

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            double salary = resultSet.getDouble(4);

            CustomerEntity customer = new CustomerEntity(id, name, address, salary);
            customerList.add(customer);
        }
        return customerList;
    }
}
