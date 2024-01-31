//package lk.ijse.gdse66.backend.dao.custom.impl;
//
//import lk.ijse.gdse66.backend.dao.custom.OrderDAO;
//import lk.ijse.gdse66.backend.entity.OrderEntity;
//import lk.ijse.gdse66.backend.util.CrudUtil;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.util.ArrayList;
//import java.util.List;
//
//public class OrderDAOImpl implements OrderDAO {
//
//
//    @Override
//    public boolean save(Connection connection, OrderEntity order) throws SQLException {
//        String sql = "INSERT INTO orders (oid, date, customerID) VALUES (?,?,?)";
//        return CrudUtil.execute(sql, connection, order.getOrderId(), order.getOrderDate(), order.getCustomerId());
//    }
//
//    @Override
//    public boolean update(Connection connection, OrderEntity order) throws SQLException {
//        String sql = "UPDATE orders SET date=?, customerID=? WHERE oid=?";
//        return CrudUtil.execute(sql, connection, order.getOrderDate(), order.getCustomerId(), order.getOrderId());
//    }
//
//    @Override
//    public boolean delete(Connection connection, String id) throws SQLException {
//        String sql = "DELETE FROM orders WHERE oid=?";
//        return CrudUtil.execute(sql, connection, id);
//    }
//
//    @Override
//    public OrderEntity search(Connection connection, String id) {
//        return null;
//    }
//
//    @Override
//    public int count(Connection connection) throws SQLException {
//        String sql = "SELECT count(oid) FROM orders";
//        ResultSet resultSet = CrudUtil.execute(sql, connection);
//        if (resultSet.next()){
//            return resultSet.getInt(1);
//        }
//        return 0;
//    }
//
//    @Override
//    public List<OrderEntity> getAll(Connection connection) {
//        return null;
//    }
//
//    @Override
//    public List<String> getIDList(Connection connection) throws SQLException {
//        List<String> orderIDList = new ArrayList<>();
//
//        String sql = "SELECT oid FROM orders";
//        ResultSet resultSet = CrudUtil.execute(sql, connection);
//
//        while (resultSet.next()) {
//            orderIDList.add(resultSet.getString(1));
//        }
//        return orderIDList;
//    }
//}
