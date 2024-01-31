package lk.ijse.gdse66.backend.bo.custom;

import lk.ijse.gdse66.backend.bo.SuperBO;

public interface DashboardBO extends SuperBO {

    int getCustomerCount();

    int getItemCount();

    int getOrderCount();
}
