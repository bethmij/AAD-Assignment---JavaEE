package lk.ijse.gdse66.backend.dao.custom.impl;

import lk.ijse.gdse66.backend.config.SessionFactoryConfig;
import lk.ijse.gdse66.backend.dao.custom.ItemDAO;
import lk.ijse.gdse66.backend.entity.ItemEntity;
import org.hibernate.Session;
import org.hibernate.Transaction;

import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    Session session;

    @Override
    public boolean save(ItemEntity item){
        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.save(item);
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
    public boolean update(ItemEntity item){

        session = SessionFactoryConfig.getInstance().getSession();
        Transaction transaction = session.beginTransaction();

        try {
            session.update(item);
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
            ItemEntity item = session.get(ItemEntity.class, id);
            session.delete(item);
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
    public ItemEntity search(String id) {

        try (Session session = SessionFactoryConfig.getInstance().getSession()){
            return session.get(ItemEntity.class, id);
        }
    }

    @Override
    public int count() {
        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            Long countResult = (Long) session.createQuery("SELECT count (i.itemCode) FROM ItemEntity i").getSingleResult();
            return countResult.intValue();
        }
    }

    @Override
    public List<ItemEntity> getAll(){

        try (Session session = SessionFactoryConfig.getInstance().getSession()) {
            return session.createQuery("SELECT i FROM ItemEntity i", ItemEntity.class).list();
        }
    }

    @Override
    public List<String> getIDList(){

        try (Session session = SessionFactoryConfig.getInstance().getSession()){
            return session.createQuery("SELECT i.itemCode FROM ItemEntity i", String.class).list();
        }
    }
}
