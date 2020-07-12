package lk.crystal.asset.goodReceivedNote.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.goodReceivedNote.entity.Enum.GoodReceivedNoteState;
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
@JsonFilter("GoodReceivedNote")
public class GoodReceivedNote extends AuditEntity {


/*    @Column(unique = true)
    private String code;*/

    private String remarks;
/*
    @Transient
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate mDate;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate eDate;*/


    @ManyToOne
    private PurchaseOrder purchaseOrder;

    @Column(precision = 10, scale = 2)
    private BigDecimal totalAmount;

    @Enumerated(EnumType.STRING)
    private GoodReceivedNoteState goodReceivedNoteState;




}
