package lk.ijse.gdse66.backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class ItemDTO {
    private String itemCode;
    private String description;
    private double unitPrice;
    private int qtyOnHand;
}
