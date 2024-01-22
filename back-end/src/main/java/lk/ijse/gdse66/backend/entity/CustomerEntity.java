package lk.ijse.gdse66.backend.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class CustomerEntity {

    private String cusID;
    private String cusName;
    private String cusAddress;
    private double cusSalary;

}
