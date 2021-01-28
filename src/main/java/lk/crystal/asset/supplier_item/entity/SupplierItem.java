package lk.crystal.asset.supplier_item.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.supplier.entity.Supplier;
import lk.crystal.asset.supplier_item.entity.enums.ItemSupplierStatus;
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
@JsonFilter( "SupplierItem" )
public class SupplierItem extends AuditEntity {

  @Column( precision = 10, scale = 2 )
  private BigDecimal price;

  @Enumerated( EnumType.STRING )
  private ItemSupplierStatus itemSupplierStatus;

  @Enumerated( EnumType.STRING )
  private LiveDead liveDead;

  @ManyToOne( cascade = CascadeType.MERGE )
  private Item item;

  @ManyToOne( cascade = CascadeType.ALL )
  private Supplier supplier;

}
