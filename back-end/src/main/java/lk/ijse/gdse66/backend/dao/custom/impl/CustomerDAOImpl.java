package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.config.SessionFactoryConfig;
import lk.ijse.gdse66.backend.dao.custom.CustomerDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public class CustomerDAOImpl implements CustomerDAO {
    Session session;

    @Override
    public boolean save(CustomerEntity customer) throws SQLException {
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.save(customer);
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
//        String sql = "INSERT INTO company.customer (id, name, address, salary) VALUES (?,?,?,?)";
//        return CrudUtil.execute(sql, connection, customer.getCusID(), customer.getCusName(),
//                customer.getCusAddress(), customer.getCusSalary());
    }

    @Override
    public boolean update(CustomerEntity customer) throws SQLException {
//        String sql = "UPDATE customer SET name=?, address=?, salary=? WHERE id=?";
//        return CrudUtil.execute(sql, connection, customer.getCusName(),
//                customer.getCusAddress(), customer.getCusSalary(), customer.getCusID());
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.update(customer);
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public boolean delete(String id) throws SQLException {
//        String sql = "DELETE FROM customer WHERE id=?";
//        return CrudUtil.execute(sql, connection, id);
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            CustomerEntity customer = session.get(CustomerEntity.class, id);
            session.delete(customer);
            transaction.commit();
            return true;
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
    }

    @Override
    public CustomerEntity search(String id) throws SQLException {


        session = SessionFactoryConfig.getInstance().getSession();
        CustomerEntity customer = session.get(CustomerEntity.class, id);
        session.close();

        return  customer;

//        String sql = "SELECT * FROM customer WHERE id=?";
//        ResultSet resultSet = CrudUtil.execute(sql, connection, id);
//
//        if (resultSet.next()) {
//            String cusId = resultSet.getString(1);
//            String name = resultSet.getString(2);
//            String address = resultSet.getString(3);
//            double salary = resultSet.getDouble(4);
//
//            customer = new CustomerEntity(cusId, name, address, salary);
//        }
    }

    @Override
    public int count() throws SQLException {
        session = SessionFactoryConfig.getInstance().getSession();
        Long countResult = (Long) session.createQuery("SELECT count (c.cusID) FROM CustomerEntity c").getSingleResult();
        session.close();
//
//        String sql = "SELECT count(id) FROM customer";
//        ResultSet resultSet = CrudUtil.execute(sql, connection);
//        if (resultSet.next()){
//            return resultSet.getInt(1);
//        }
        return countResult.intValue();
    }

    @Override
    public List<CustomerEntity> getAll() throws SQLException {
        List<CustomerEntity> customerList = new ArrayList<>();
        session = SessionFactoryConfig.getInstance().getSession();
        customerList = session.createQuery("FROM CustomerEntity c").list();
        session.close();
//        String sql = "SELECT * FROM customer";
//        ResultSet resultSet = CrudUtil.execute(sql, connection);
//
//        while (resultSet.next()) {
//            String id = resultSet.getString(1);
//            String name = resultSet.getString(2);
//            String address = resultSet.getString(3);
//            double salary = resultSet.getDouble(4);
//
//            CustomerEntity customer = new CustomerEntity(id, name, address, salary);
//            customerList.add(customer);
//        }
        return customerList;
    }

    @Override
    public List<String> getIDList() throws SQLException {
        List<String> cusIdList = new ArrayList<>();
        session = SessionFactoryConfig.getInstance().getSession();
        cusIdList = session.createQuery("SELECT c.cusID FROM CustomerEntity c").list();
        session.close();
//        List<String> cusIdList = new ArrayList<>();
//        String sql = "SELECT id FROM customer";
//        ResultSet resultSet = CrudUtil.execute(sql, connection);
//
//        while (resultSet.next()) {
//            String id = resultSet.getString(1);
//            cusIdList.add(id);
//        }
        return cusIdList;
    }
}
