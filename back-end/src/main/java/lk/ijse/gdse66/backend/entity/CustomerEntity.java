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
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @Column(name = "id")
    private String cusID;

    @Column (name = "name")
    private String cusName;

    @Column (name = "address", columnDefinition = "TEXT")
    private String cusAddress;

    @Column (name = "salary")
    private double cusSalary;

}
