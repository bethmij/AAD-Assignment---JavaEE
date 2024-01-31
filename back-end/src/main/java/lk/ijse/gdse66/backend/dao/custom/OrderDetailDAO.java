package lk.ijse.gdse66.backend.dao.custom;

import lk.ijse.gdse66.backend.dao.SuperDAO;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;

import java.util.List;

public interface OrderDetailDAO extends SuperDAO {

    boolean save( List<OrderDetailsEntity> dto);

    boolean update(List<OrderDetailsEntity> dto);


}
