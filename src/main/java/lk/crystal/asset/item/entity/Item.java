package lk.crystal.asset.item.entity;

import com.fasterxml.jackson.annotation.JsonFilter;
import lk.crystal.asset.item.category.entity.Category;
import lk.crystal.asset.purchaseOrder.entity.PurchaseOrderItem;
import lk.crystal.asset.supplier.entity.SupplierItem;
import lk.crystal.util.audit.AuditEntity;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonFilter("Item")
public class Item extends AuditEntity {

    @Size(min = 5, message = "Your name cannot be accepted")
    private String name;

    private Integer rop;

    @ManyToOne(fetch = FetchType.EAGER)
    private Category category;

    /*@ManyToOne(fetch = FetchType.EAGER)
    private Brand brand;*/

    @OneToMany(mappedBy = "item")
    private List<PurchaseOrderItem> purchaseOrderItems;

/*    @OneToMany(mappedBy = "item")
    private List<ItemBatch> itemBatches;*/

    @OneToMany(mappedBy = "item")
    private List<SupplierItem> supplierItems;
}
