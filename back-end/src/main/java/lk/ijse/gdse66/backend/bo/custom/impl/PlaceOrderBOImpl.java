package lk.ijse.gdse66.backend.bo.custom.impl;

import jakarta.servlet.http.HttpServletResponse;
import lk.ijse.gdse66.backend.bo.custom.PlaceOrderBO;
import lk.ijse.gdse66.backend.dao.DAOFactory;
import lk.ijse.gdse66.backend.dao.custom.OrderDAO;
import lk.ijse.gdse66.backend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse66.backend.dto.CustomerDTO;
import lk.ijse.gdse66.backend.dto.OrderDTO;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;
import lk.ijse.gdse66.backend.entity.OrderEntity;
import lk.ijse.gdse66.backend.util.CrudUtil;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class PlaceOrderBOImpl implements PlaceOrderBO {
    OrderDAO orderDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDAO);
    OrderDetailDAO orderDetailDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILDAO);

    @Override
    public OrderDTO getOrderByID(Connection connection, String id) throws SQLException {
        return null;
    }

    @Override
    public List<String> getOrderIDList(Connection connection) throws SQLException {
        return null;
    }

    @Override
    public boolean saveOrder(Connection connection, OrderDTO orderDTO) throws SQLException {
        try {
            connection.setAutoCommit(false);

            OrderEntity orderEntity = new OrderEntity(orderDTO.getOrderId(), orderDTO.getOrderDate(), orderDTO.getCustomerId());
            boolean isOrderAdded = orderDAO.save(connection, orderEntity);

            if (isOrderAdded) {

                List<OrderDetailsEntity> orderDetailsList = orderDTO.getOrderDetails();
                boolean isOrderDetailsAdded = orderDetailDAO.save(connection, orderDetailsList);

                if (isOrderDetailsAdded) {
                    connection.commit();
                    return true;
                }
            }
        } catch (SQLException e) {
            connection.rollback();
            return false;
        }
        return false;
    }




    @Override
    public boolean updateOrder(Connection connection, CustomerDTO customerDTO) throws SQLException {
        return false;
    }
}
