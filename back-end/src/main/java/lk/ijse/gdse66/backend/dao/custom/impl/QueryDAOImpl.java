//package lk.ijse.gdse66.backend.dao.custom.impl;
//
//import lk.ijse.gdse66.backend.dao.custom.QueryDAO;
//import lk.ijse.gdse66.backend.dto.OrderDTO;
//import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;
//import lk.ijse.gdse66.backend.util.CrudUtil;
//
//import java.sql.Connection;
//import java.sql.ResultSet;
//import java.sql.SQLException;
//import java.time.LocalDate;
//import java.util.ArrayList;
//import java.util.List;
//
//public class QueryDAOImpl implements QueryDAO {
//    @Override
//    public OrderDTO getOrderDetail(Connection connection, String id) throws SQLException {
//        OrderDTO orderDTO = null;
//        List<OrderDetailsEntity> orderList = new ArrayList<>();
//
//        String sql = "SELECT o.*, od.itemCode, od.qty, od.unitPrice FROM orders o JOIN orderdetails od on o.oid = od.oid WHERE o.oid=?";
//        ResultSet resultSet = CrudUtil.execute(sql, connection, id);
//
//        while (resultSet.next()) {
//            String orderID = resultSet.getString(1);
//            LocalDate date = resultSet.getDate(2).toLocalDate();
//            String customerID = resultSet.getString(3);
//            String itemCode = resultSet.getString(4);
//            int qtyOnHand = resultSet.getInt(5);
//            double unitPrice = resultSet.getDouble(6);
//
////            OrderEntity order = new OrderEntity(orderID, date, customerID);
//            OrderDetailsEntity orderDetails = new OrderDetailsEntity(orderID, itemCode, qtyOnHand, unitPrice);
//            orderList.add(orderDetails);
//
//            orderDTO = new OrderDTO(orderID, date, customerID, null);
//        }
//
//        assert orderDTO != null;
//        orderDTO.setOrderDetails(orderList);
//        return orderDTO;
//    }
//
//}
