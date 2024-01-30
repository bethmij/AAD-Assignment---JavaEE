package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;

import java.sql.Connection;
import java.sql.SQLException;

public interface DashboardBO extends SuperBO {

    int getCustomerCount(Connection connection) throws SQLException;

    int getItemCount(Connection connection) throws SQLException;

    int getOrderCount(Connection connection) throws SQLException;
}
