package lk.ijse.gdse66.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderEntity {

    private int orderId;
    private LocalDate orderDate;
    private String customerId;

}
