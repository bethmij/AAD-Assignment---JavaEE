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
@Table(name = "item")
public class ItemEntity {

    @Id
    @Column(name = "code")
    private String itemCode;

    @Column (name = "description", columnDefinition = "TEXT")
    private String description;

    @Column (name = "qtyOnHand")
    private int qtyOnHand;

    @Column (name = "unitPrice")
    private double unitPrice;

}
