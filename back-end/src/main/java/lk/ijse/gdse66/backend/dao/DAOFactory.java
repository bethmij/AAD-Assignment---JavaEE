package lk.ijse.gdse66.backend.dao;

import lk.ijse.gdse66.backend.dao.custom.impl.CustomerDAOImpl;
import lk.ijse.gdse66.backend.dao.custom.impl.ItemDAOImpl;
import lk.ijse.gdse66.backend.dao.custom.impl.OrderDAOImpl;
import lk.ijse.gdse66.backend.dao.custom.impl.OrderDetailDAOImpl;

public class DAOFactory {

    private static DAOFactory daoFactory;

    private DAOFactory(){}

    public static DAOFactory getDaoFactory(){
        return (daoFactory==null) ? daoFactory = new DAOFactory() : daoFactory;
    }

    public enum DAOTypes {
        CUSTOMERDAO, ITEMDAO, ORDERDAO, ORDERDETAILDAO
    }

    public <T extends SuperDAO> T getDAO (DAOFactory.DAOTypes daoTypes){

        switch (daoTypes){
            case CUSTOMERDAO:
                return (T) new CustomerDAOImpl();
            case ITEMDAO:
                return (T) new ItemDAOImpl();
            case ORDERDAO:
                return (T) new OrderDAOImpl();
            case ORDERDETAILDAO:
                return (T) new OrderDetailDAOImpl();
            default:
                return null;
        }
    }
}
