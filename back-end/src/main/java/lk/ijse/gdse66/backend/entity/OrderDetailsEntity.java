package lk.ijse.gdse66.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetailsEntity {

   private String ordeDetailId;
   private String itemCode;
   private int qty;
   private double unitPrice;


}
