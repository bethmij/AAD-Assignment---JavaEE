package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.config.SessionFactoryConfig;
import lk.ijse.gdse66.backend.dao.custom.CustomerDAO;
import lk.ijse.gdse66.backend.entity.CustomerEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;


public class CustomerDAOImpl implements CustomerDAO {
    Session session;

    @Override
    public boolean save(CustomerEntity customer) {
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
    }

    @Override
    public boolean update(CustomerEntity customer){

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
    public boolean delete(String id){

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
    public CustomerEntity search(String id){

        try (Session session = SessionFactoryConfig.getInstance().getSession()){
            return session.get(CustomerEntity.class, id);
        }
    }

    @Override
    public int count() {

        try (Session session = SessionFactoryConfig.getInstance().getSession()){
            Long countResult = (Long) session.createQuery("SELECT count (c.cusID) FROM CustomerEntity c").getSingleResult();
            return countResult.intValue();
        }
    }

    @Override
    public List<CustomerEntity> getAll()  {

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            return session.createQuery("SELECT c FROM CustomerEntity c", CustomerEntity.class).list();
        }
    }

    @Override
    public List<String> getIDList() {

        try (Session session = SessionFactoryConfig.getInstance().getSession()){
            return session.createQuery("SELECT c.cusID FROM CustomerEntity c", String.class).list();
        }
    }
}
