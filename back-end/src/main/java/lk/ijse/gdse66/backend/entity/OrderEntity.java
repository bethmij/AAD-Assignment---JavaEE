package lk.ijse.gdse66.backend.entity;

import jakarta.json.bind.annotation.JsonbDateFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(name = "oid")
    private String orderId;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    @Column (name = "date", columnDefinition = "DATE")
    private LocalDate orderDate;

    @Column (name = "custonerID")
    private String customerId;

}
