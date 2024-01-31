package lk.ijse.gdse66.backend.dto;

import lk.ijse.gdse66.backend.entity.OrderEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailDTO {

    private String orderId;
    private String itemCode;
    private int qtyOnHand;
    private double unitPrice;

}