package lk.crystal.asset.purchase_order.common_model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseOrderItemLedger {
    private int itemId;
    private String itemName;
    private String rop;
    private BigDecimal price;
    private String availableQuantity;
}
