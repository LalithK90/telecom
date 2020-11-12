package lk.crystal.asset.discount_ratio.entity.Enum;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum DiscountRatioStatus {
    ACTIVE("Active"),
    STOP("Stop");

    private final String discountRatioStatus;
}
