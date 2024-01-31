package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;

import java.sql.SQLException;

public interface DashboardBO extends SuperBO {

    int getCustomerCount() throws SQLException;

    int getItemCount() throws SQLException;

    int getOrderCount() throws SQLException;
}
