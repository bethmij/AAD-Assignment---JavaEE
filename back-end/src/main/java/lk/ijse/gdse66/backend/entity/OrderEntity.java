package lk.ijse.gdse66.backend.entity;

import jakarta.json.bind.annotation.JsonbDateFormat;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Table(name = "orders")
public class OrderEntity {

    @Id
    @Column(name = "order_id")
    private String orderId;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    @CreationTimestamp
    @Column (name = "order_date", columnDefinition = "DATE")
    private LocalDate orderDate;

    @ManyToOne
    @JoinColumn(name = "customer_id", referencedColumnName = "customer_id", nullable = false)
    private CustomerEntity customer;

    @OneToMany (cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "orders")
    private List<OrderDetailsEntity> orderDetails = new ArrayList<>();

    public OrderEntity(String orderId, LocalDate orderDate, CustomerEntity customer) {
        this.orderId = orderId;
        this.orderDate = orderDate;
        this.customer = customer;
    }

    public OrderEntity(String orderId) {
        this.orderId = orderId;
    }
}
