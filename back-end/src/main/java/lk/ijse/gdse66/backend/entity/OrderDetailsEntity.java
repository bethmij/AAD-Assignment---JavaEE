package lk.ijse.gdse66.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orderdetails")
public class OrderDetailsEntity {

   @Id
   @Column(name = "oid")
   private String orderId;

   @Column (name = "itemCode")
   private String itemCode;

   @Column (name = "qty")
   private int qtyOnHand;

   @Column (name = "unitPrice")
   private double unitPrice;

}
