package lk.ijse.gdse66.backend.dto;

import jakarta.json.bind.annotation.JsonbDateFormat;
import lk.ijse.gdse66.backend.entity.OrderDetailsEntity;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class OrderDTO {
    private String orderId;

    @JsonbDateFormat(value = "yyyy-MM-dd")
    private LocalDate orderDate;

    private String customerId;
    private List<OrderDetailDTO> orderDetails;


}
