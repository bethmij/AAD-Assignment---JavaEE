package lk.ijse.gdse66.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "order_details")
public class OrderDetailsEntity {

   @Id
   @GeneratedValue (strategy = GenerationType.IDENTITY)
   @Column (name = "order_detail_id")
   private int ordeDetailId;

   @Column (name = "quantity")
   private int qtyOnHand;

   @Column (name = "unit_price")
   private double unitPrice;

   @ManyToOne
   @JoinColumn(name = "item_code", referencedColumnName = "item_code", nullable = false)
   private ItemEntity item;

   @ManyToOne
   @JoinColumn(name = "order_id", referencedColumnName = "order_id", nullable = false)
   private OrderEntity orders;


   public OrderDetailsEntity( int qtyOnHand, double unitPrice, ItemEntity item, OrderEntity orders) {
      this.qtyOnHand = qtyOnHand;
      this.unitPrice = unitPrice;
      this.item = item;
      this.orders = orders;
   }
}

