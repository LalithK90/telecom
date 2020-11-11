package lk.crystal.asset.item.entity.Enum;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum ItemStatus {
    AVAILABLE("Available"),
    NOT_AVAILABLE("Not Available"),
    ROP("Need to order"),
    ORDERED("Ordered"),
    STOP("Not For Further");

    private final String itemStatus;
}

