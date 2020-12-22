package lk.crystal.asset.customer.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.common_asset.model.enums.Title;
import lk.crystal.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.validation.constraints.Size;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Customer")
public class Customer extends AuditEntity {

    @Enumerated(EnumType.STRING)
    private Title title;

    @Size(min = 5, message = "Your name cannot be accepted")
    private String name;

    @Size(max = 12, min = 10, message = "NIC number is formed by 9 numbers with X/V OR 12 numbers ")
    @Column(unique = true)
    private String nic;

    @Size(max = 10, min = 9, message = "Mobile number length should be contained 10 and 9")
    @Column(unique = true)
    private String mobile;

    @Column(columnDefinition = "VARCHAR(255) CHARACTER SET utf8 COLLATE utf8_bin NULL", length = 255)
    private String address;

    @Column(unique = true)
    private String code; // ex. {yearLastTwo}{less than ten thousand}

    @Column(unique = true)
    private String email;

    @Enumerated(EnumType.STRING)
    private LiveDead liveDead;

}
