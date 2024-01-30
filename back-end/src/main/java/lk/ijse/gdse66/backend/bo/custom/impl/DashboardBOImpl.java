package lk.ijse.gdse66.backend.bo.custom.impl;

import lk.ijse.gdse66.backend.bo.custom.DashboardBO;
import lk.ijse.gdse66.backend.dao.DAOFactory;
import lk.ijse.gdse66.backend.dao.custom.CustomerDAO;
import lk.ijse.gdse66.backend.dao.custom.ItemDAO;
import lk.ijse.gdse66.backend.dao.custom.OrderDAO;

import java.sql.Connection;
import java.sql.SQLException;

public class DashboardBOImpl implements DashboardBO {
    CustomerDAO customerDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.CUSTOMERDAO);
    ItemDAO itemDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ITEMDAO);
    OrderDAO orderDAO = DAOFactory.getDaoFactory().getDAO(DAOFactory.DAOTypes.ORDERDAO);

    @Override
    public int getCustomerCount(Connection connection) throws SQLException {
        return customerDAO.count(connection);
    }

    @Override
    public int getItemCount(Connection connection) throws SQLException {
        return itemDAO.count(connection);
    }

    @Override
    public int getOrderCount(Connection connection) throws SQLException {
        return orderDAO.count(connection);
    }

}
