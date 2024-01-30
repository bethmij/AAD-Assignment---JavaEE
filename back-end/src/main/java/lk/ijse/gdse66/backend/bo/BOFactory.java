package lk.ijse.gdse66.backend.bo;

import lk.ijse.gdse66.backend.bo.custom.impl.CustomerBOImpl;
import lk.ijse.gdse66.backend.bo.custom.impl.DashboardBOImpl;
import lk.ijse.gdse66.backend.bo.custom.impl.ItemBOImpl;
import lk.ijse.gdse66.backend.bo.custom.impl.PlaceOrderBOImpl;

public class BOFactory {
    private static BOFactory boFactory;

    private BOFactory(){}

    public static BOFactory getBoFactory(){
        return (boFactory==null) ? boFactory = new BOFactory() : boFactory;
    }

    public enum BOTypes {
        CUSTOMERBO, ITEMBO, PLACEORDERBO, DASHBOARDBO
    }

    public <T extends SuperBO> T getBO (BOTypes boTypes){
        switch (boTypes){
            case CUSTOMERBO:
                return (T) new CustomerBOImpl();
            case ITEMBO:
                return (T) new ItemBOImpl();
            case PLACEORDERBO:
                return (T) new PlaceOrderBOImpl();
            case DASHBOARDBO:
                return (T) new DashboardBOImpl();
            default:
                return null;
        }
    }

}
