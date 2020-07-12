package lk.crystal.asset.payment.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.payment.entity.Enum.PaymentMethod;
import lk.crystal.asset.purchaseOrder.entity.PurchaseOrder;
import lk.crystal.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Payment")
public class Payment extends AuditEntity {
    private String bankName;

    private String remarks;

    @Column(nullable = false, unique = true)
    private String code;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal amount;

    @ManyToOne
    private PurchaseOrder purchaseOrder;
}
