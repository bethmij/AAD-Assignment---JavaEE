package lk.ijse.gdse66.backend.bo.custom.impl;

import lk.ijse.gdse66.backend.bo.custom.PlaceOrderBO;
import lk.ijse.gdse66.backend.config.SessionFactoryConfig;
import lk.ijse.gdse66.backend.dao.DAOFactory;
import lk.ijse.gdse66.backend.dao.custom.OrderDAO;
import lk.ijse.gdse66.backend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse66.backend.dao.custom.QueryDAO;
import lk.ijse.gdse66.backend.dto.OrderDTO;
import lk.ijse.gdse66.backend.dto.OrderDetailDTO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import lk.ijse.gdse66.backend.entity.ItemEntity;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;
import lk.ijse.gdse66.backend.entity.OrderEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PlaceOrderBOImpl implements PlaceOrderBO {
    OrderDAO orderDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDAO);
    OrderDetailDAO orderDetailDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDETAILDAO);
    QueryDAO queryDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.QUERYDAO);

    @Override
    public OrderDTO getOrderByID(String id) throws SQLException {
        OrderEntity order = queryDAO.getOrderDetail(id);
        List<OrderDetailDTO> orderDTOS = new ArrayList<>();

        for (OrderDetailsEntity details: order.getOrderDetails()) {
            OrderDetailDTO detailDTO = new OrderDetailDTO(details.getOrders().getOrderId(), details.getItem().getItemCode(),
                    details.getQtyOnHand(), details.getUnitPrice());
            orderDTOS.add(detailDTO);
        }
        return new OrderDTO(order.getOrderId(), order.getOrderDate(), order.getCustomer().getCusID(), orderDTOS);

    }

    @Override
    public List<String> getOrderIDList() throws SQLException {
        return orderDAO.getIDList();
    }

    @Override
    public boolean saveOrder(OrderDTO orderDTO)  {
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            CustomerEntity customer = new CustomerEntity(orderDTO.getCustomerId());
            OrderEntity orderEntity = new OrderEntity(orderDTO.getOrderId(), orderDTO.getOrderDate(), customer);
            boolean isOrderAdded = orderDAO.save(orderEntity);

            if (isOrderAdded){
                List<OrderDetailsEntity> orderDetailsList = new ArrayList<>();

                for (OrderDetailDTO orderDetails : orderDTO.getOrderDetails()) {
                    ItemEntity item = new ItemEntity(orderDetails.getItemCode());
                    OrderEntity order = new OrderEntity(orderDetails.getOrderId());

                    OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity( orderDetails.getQtyOnHand(),
                            orderDetails.getUnitPrice(), item, order);
                    orderDetailsList.add(orderDetailsEntity);
                }
                boolean isOrderDetailSaved =  orderDetailDAO.save(orderDetailsList);

                if (isOrderDetailSaved){
                    transaction.commit();
                    return true;
                }
            }
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
        return false;
    }

    @Override
    public boolean updateOrder( OrderDTO orderDTO){
        Session session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            CustomerEntity customer = new CustomerEntity(orderDTO.getCustomerId());
            OrderEntity orderEntity = new OrderEntity(orderDTO.getOrderId(), orderDTO.getOrderDate(), customer);
            System.out.println("orderEntity "+orderEntity);
            boolean isOrderUpdated = orderDAO.update(orderEntity);

            if (isOrderUpdated){
                List<OrderDetailsEntity> orderDetailsList = new ArrayList<>();

                for (OrderDetailDTO orderDetails : orderDTO.getOrderDetails()) {
                    ItemEntity item = new ItemEntity(orderDetails.getItemCode());
                    OrderEntity order = new OrderEntity(orderDetails.getOrderId());

                    OrderDetailsEntity orderDetailsEntity = new OrderDetailsEntity( orderDetails.getQtyOnHand(),
                            orderDetails.getUnitPrice(), item, order);
                    orderDetailsList.add(orderDetailsEntity);
                }
                System.out.println("orderDetailsList "+orderDetailsList);
                boolean isOrderDetailUpdated =  orderDetailDAO.update(orderDetailsList);

                if (isOrderDetailUpdated){
                    transaction.commit();
                    return true;
                }
            }
        }catch (Exception e){
            transaction.rollback();
            return false;
        }finally {
            session.close();
        }
        return false;
    }


    @Override
    public boolean deleteOrder(String id) throws SQLException {
        return  orderDAO.delete( id);
    }
}
