package lk.crystal.asset.invoice_ledger.entity;


import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.invoice.entity.Invoice;
import lk.crystal.asset.ledger.entity.Ledger;
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
@JsonFilter( "InvoiceLedger" )
public class InvoiceLedger extends AuditEntity {

  @Column( nullable = false )
  private String quantity;

  @Column( unique = true )
  private String warrantyNumber;

  @Column( nullable = false, precision = 10, scale = 2 )
  private BigDecimal sellPrice;

  @Column( nullable = false, precision = 10, scale = 2 )
  private BigDecimal lineTotal;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @ManyToOne
  private Ledger ledger;

  @ManyToOne
  private Invoice invoice;

}
