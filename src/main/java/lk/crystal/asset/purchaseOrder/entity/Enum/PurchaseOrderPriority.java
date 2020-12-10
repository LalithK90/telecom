package lk.crystal.asset.purchaseOrder.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseOrderPriority {
    HIGH("Immediate"),
    MEDIUM("Medium"),
    NORMAL("Normal");
    private final String purchaseOrderPriority;
}
