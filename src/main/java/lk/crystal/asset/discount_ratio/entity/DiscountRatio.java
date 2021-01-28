package lk.crystal.asset.discount_ratio.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.invoice.entity.Invoice;
import lk.crystal.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;


@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "DiscountRatio" )
public class DiscountRatio extends AuditEntity {

    @Column( nullable = false, length = 45 )
    private String name;

    @Column( nullable = false, precision = 10, scale = 2 )
    private BigDecimal amount;

    @Enumerated( EnumType.STRING)
    private LiveDead liveDead;

    @OneToMany( mappedBy = "discountRatio" )
    private List< Invoice > invoices;

}

