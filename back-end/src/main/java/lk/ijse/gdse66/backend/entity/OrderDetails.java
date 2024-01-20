package lk.ijse.gdse66.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDetails {

   private int ordeDetailId;
   private int qty;
   private double unitPrice;
   private double totalPrice;


}
