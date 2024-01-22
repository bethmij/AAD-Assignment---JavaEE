package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {


    @Override
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



    @Override
    public boolean update(Connection connection, List<OrderDetailsEntity> dto) {
        return false;
    }

    @Override
    public boolean delete(Connection connection, String id) {
        return false;
    }

    @Override
    public List<OrderDetailsEntity> search(Connection connection, String id) {
        return null;
    }

    @Override
    public List<List<OrderDetailsEntity>> getAll(Connection connection) {
        return null;
    }

    @Override
    public List<String> getIDList(Connection connection) throws SQLException {
        return null;
    }
}
