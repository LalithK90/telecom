package lk.crystal.asset.supplier_item.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemSupplierStatus {
    CURRENTLY_BUYING("Currently Buying"),
    STOPPED("Stopped");

    private final String itemSupplierStatus;

}
