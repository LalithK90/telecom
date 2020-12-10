package lk.crystal.asset.good_received_note.entity;



import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.good_received_note.entity.enums.GoodReceivedNoteState;
import lk.crystal.asset.ledger.entity.Ledger;
import lk.crystal.asset.purchaseOrder.entity.PurchaseOrder;
import lk.crystal.util.audit.AuditEntity;
import lombok.*;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "GoodReceivedNote" )
@ToString
public class GoodReceivedNote extends AuditEntity {

    private String remarks;

    @Column( precision = 10, scale = 2 )
    private BigDecimal totalAmount;

    @Enumerated( EnumType.STRING )
    private GoodReceivedNoteState goodReceivedNoteState;

    @ManyToOne
    private PurchaseOrder purchaseOrder;

    @OneToMany( mappedBy = "goodReceivedNote", cascade = CascadeType.PERSIST)
    private List< Ledger > ledgers;


}
