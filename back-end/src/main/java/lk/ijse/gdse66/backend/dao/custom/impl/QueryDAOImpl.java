package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.config.SessionFactoryConfig;
import lk.ijse.gdse66.backend.dao.custom.QueryDAO;
import lk.ijse.gdse66.backend.entity.OrderEntity;
import org.hibernate.Session;

public class QueryDAOImpl implements QueryDAO {
    @Override
    public OrderEntity getOrderDetail(String id){
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {

            return session.createQuery(
                            "SELECT o FROM OrderEntity o LEFT JOIN FETCH o.orderDetails WHERE o.orderId = :orderId",
                            OrderEntity.class
                    )
                    .setParameter("orderId", id)
                    .getSingleResult();
        }

    }

}
