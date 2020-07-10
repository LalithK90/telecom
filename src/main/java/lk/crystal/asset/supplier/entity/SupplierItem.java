package lk.crystal.asset.supplier.entity;

import com.fasterxml.jackson.annotation.JsonFilter;


import lk.crystal.asset.item.entity.Item;
import lk.crystal.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("SupplierItem")
public class SupplierItem extends AuditEntity {

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal price;

    @ManyToOne
    private Item item;

    @ManyToOne
    private Supplier supplier;

}
