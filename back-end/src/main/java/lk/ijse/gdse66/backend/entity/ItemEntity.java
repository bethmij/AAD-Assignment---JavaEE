package lk.ijse.gdse66.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemEntity {

    private String code;
    private String description;
    private int qtyOnHand;
    private double unitPrice;

}
