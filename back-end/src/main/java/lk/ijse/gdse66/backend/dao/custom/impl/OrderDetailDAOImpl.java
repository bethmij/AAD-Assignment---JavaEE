package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.config.SessionFactoryConfig;
import lk.ijse.gdse66.backend.dao.custom.OrderDetailDAO;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class OrderDetailDAOImpl implements OrderDetailDAO {
    Session session;


    public boolean save(List<OrderDetailsEntity> orderDetailsList)  {

        int savedCount = 0;
        for (OrderDetailsEntity orderDetail : orderDetailsList) {
            session = SessionFactoryConfig.getInstance().getSession();
            Transaction transaction = session.beginTransaction();

            try {
                session.save(orderDetail);
                transaction.commit();
                savedCount++;
            }catch (Exception e){
                transaction.rollback();
                return false;
            }finally {
                session.close();
            }
        }

        return savedCount == orderDetailsList.size();

    }

    public boolean update(List<OrderDetailsEntity> orderDetailsList)  {

        for (OrderDetailsEntity orderDetail : orderDetailsList) {
            session = SessionFactoryConfig.getInstance().getSession();
            Transaction transaction = session.beginTransaction();

            try {
                Query query = session.createQuery("UPDATE OrderDetailsEntity o SET o.qtyOnHand = :qty, o.unitPrice = :uPrice" +
                        " WHERE o.item.itemCode = :itemCode AND o.orders.orderId = :orderID");
                query.setParameter("qty", orderDetail.getQtyOnHand());
                query.setParameter("uPrice", orderDetail.getUnitPrice());
                query.setParameter("itemCode", orderDetail.getItem().getItemCode());
                query.setParameter("orderID", orderDetail.getOrders().getOrderId());
                return query.executeUpdate() > 0;

            }catch (Exception e){
                transaction.rollback();
                return false;
            }finally {
                session.close();
            }

        }
        return false;
    }

}
