package lk.ijse.gdse66.backend.entity;

import jakarta.json.bind.annotation.JsonbDateFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderEntity {

    private String orderId;
    private LocalDate orderDate;
    private String customerId;

}
