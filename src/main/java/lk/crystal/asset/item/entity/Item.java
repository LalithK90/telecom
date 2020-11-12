package lk.crystal.asset.item.entity;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.purchase_order.entity.PurchaseOrderItem;
import lk.crystal.asset.brand.entity.Brand;
import lk.crystal.asset.category.entity.Category;
import lk.crystal.asset.color.entity.ItemColor;
import lk.crystal.asset.item.entity.enums.ItemStatus;
import lk.crystal.asset.ledger.entity.Ledger;
import lk.crystal.asset.supplier_item.entity.SupplierItem;
import lk.crystal.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter( "Item" )
public class Item extends AuditEntity {

    @Size( min = 2, message = "Your name can not be accepted" )
    private String name;

    private String rop;

    @Column( unique = true )
    private String code;

    @Column( nullable = false, precision = 10, scale = 2 )
    private BigDecimal sellPrice;

    @Enumerated( EnumType.STRING )
    private ItemStatus itemStatus;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    @ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;

    @ManyToOne(fetch = FetchType.EAGER)
    private ItemColor itemColor;

    @OneToMany( mappedBy = "item" )
    private List< SupplierItem > supplierItems;

    @OneToMany( mappedBy = "item" )
    @JsonBackReference
    private List< Ledger > ledgers;

    @OneToMany( mappedBy = "item" )
    private List< PurchaseOrderItem > purchaseOrderItems;
}
