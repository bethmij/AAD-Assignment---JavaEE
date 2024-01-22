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
    public boolean save(Connection connection, CustomerEntity customer) throws SQLException {
        String sql = "INSERT INTO company.customer (id, name, address, salary) VALUES (?,?,?,?)";
        return CrudUtil.execute(sql, connection, customer.getCusID(), customer.getCusName(),
                customer.getCusAddress(), customer.getCusSalary());
    }

    @Override
    public boolean update(Connection connection, CustomerEntity customer) throws SQLException {
        String sql = "UPDATE customer SET name=?, address=?, salary=? WHERE id=?";
        return CrudUtil.execute(sql, connection, customer.getCusName(),
                customer.getCusAddress(), customer.getCusSalary(), customer.getCusID());
    }

    @Override
    public boolean delete(Connection connection, String id) throws SQLException {
        String sql = "DELETE FROM customer WHERE id=?";
        return CrudUtil.execute(sql, connection, id);
    }

    @Override
    public CustomerEntity search(Connection connection, String id) throws SQLException {
        CustomerEntity customer = null;

        String sql = "SELECT * FROM customer WHERE id=?";
        ResultSet resultSet = CrudUtil.execute(sql, connection, id);

        if (resultSet.next()) {
            String cusId = resultSet.getString(1);
            String name = resultSet.getString(2);
            String address = resultSet.getString(3);
            double salary = resultSet.getDouble(4);

            customer = new CustomerEntity(cusId, name, address, salary);
        }
        return customer;
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

    @Override
    public List<String> getIDList(Connection connection) throws SQLException {
        List<String> cusIdList = new ArrayList<>();
        String sql = "SELECT id FROM customer";
        ResultSet resultSet = CrudUtil.execute(sql, connection);

        while (resultSet.next()) {
            String id = resultSet.getString(1);
            cusIdList.add(id);
        }
        return cusIdList;
    }
}
