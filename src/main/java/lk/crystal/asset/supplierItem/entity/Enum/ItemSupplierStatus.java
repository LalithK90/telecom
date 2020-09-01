package lk.crystal.asset.supplierItem.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ItemSupplierStatus {
    CURRENTLY_BUYING("Currently Buying"),
    STOPPED("Stopped");

    private final String itemSupplierStatus;

}
