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
@Table(name = "customer")
public class CustomerEntity {

    @Id
    @Column(name = "customer_id")
    private String cusID;

    @Column (name = "name")
    private String cusName;

    @Column (name = "address", columnDefinition = "TEXT")
    private String cusAddress;

    @Column (name = "salary")
    private double cusSalary;

    @OneToMany(cascade = CascadeType.ALL,fetch = FetchType.LAZY,mappedBy = "customer")
    private List<OrderEntity> orderList = new ArrayList<>();

    public CustomerEntity(String cusID, String cusName, String cusAddress, double cusSalary) {
        this.cusID = cusID;
        this.cusName = cusName;
        this.cusAddress = cusAddress;
        this.cusSalary = cusSalary;
    }

    public CustomerEntity(String cusID) {
        this.cusID = cusID;
    }
}
