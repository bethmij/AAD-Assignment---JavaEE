package lk.ijse.gdse66.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "item")
public class ItemEntity {

    @Id
    @Column(name = "item_code")
    private String itemCode;

    @Column (name = "description", columnDefinition = "TEXT")
    private String description;

    @Column (name = "qty_on_hand")
    private int qtyOnHand;

    @Column (name = "unit_price")
    private double unitPrice;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "item")
    private List<OrderDetailsEntity> orderDetails = new ArrayList<>();

    public ItemEntity(String itemCode, String description, int qtyOnHand, double unitPrice) {
        this.itemCode = itemCode;
        this.description = description;
        this.qtyOnHand = qtyOnHand;
        this.unitPrice = unitPrice;
    }

    public ItemEntity(String itemCode) {
        this.itemCode = itemCode;
    }
}
