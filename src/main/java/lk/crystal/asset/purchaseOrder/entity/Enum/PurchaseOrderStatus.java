package lk.crystal.asset.purchaseOrder.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseOrderStatus {
    COMPLETED(" Completed "),
    NOT_COMPLETED(" Not Completed"),
    NOT_PROCEED(" Not Proceed");
    private final String purchaseOrderStatus;
}
