package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.SuperDAO;
import lk.ijse.gdse66.backend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {


    public boolean save(Connection connection, List<OrderDetailsEntity> orderDetailsList) throws SQLException {

        int savedCount = 0;

        for (OrderDetailsEntity orderDetail : orderDetailsList) {

            String oid = orderDetail.getOrderId();
            String code = orderDetail.getItemCode();
            int qty = orderDetail.getQtyOnHand();
            double unitPrice = orderDetail.getUnitPrice();

            String sql1 = "INSERT INTO orderdetails (oid, itemCode, qty, unitPrice) VALUES (?,?,?,?)";
            boolean isAdded = CrudUtil.execute(sql1, connection, oid, code, qty, unitPrice);
            savedCount +=  isAdded ? 1:0;

        }

        return savedCount == orderDetailsList.size();

    }

    public boolean update(Connection connection, List<OrderDetailsEntity> orderDetailsList) throws SQLException {
        int updatedCount = 0;

        for (OrderDetailsEntity orderDetail : orderDetailsList) {

            String oid = orderDetail.getOrderId();
            String code = orderDetail.getItemCode();
            int qty = orderDetail.getQtyOnHand();
            double unitPrice = orderDetail.getUnitPrice();

            String sql1 = "UPDATE orderdetails SET qty=?, unitPrice=? WHERE oid=? AND itemCode=?";
            boolean isUpdated = CrudUtil.execute(sql1, connection, qty, unitPrice, oid, code);
            updatedCount +=  isUpdated ? 1:0;

        }

        return updatedCount == orderDetailsList.size();
    }

}
