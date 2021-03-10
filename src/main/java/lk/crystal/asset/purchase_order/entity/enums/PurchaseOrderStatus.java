package lk.crystal.asset.purchase_order.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum PurchaseOrderStatus {
    NOT_COMPLETED(" Not Completed"),
    NOT_PROCEED(" Not Proceed"),
    COMPLETED(" Completed ");

    private final String purchaseOrderStatus;

}
