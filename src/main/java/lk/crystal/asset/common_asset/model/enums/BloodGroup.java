package lk.crystal.asset.common_asset.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum BloodGroup {
    AP("A RhD positive"),
    AN("A RhD negative"),
    BP("B RhD positive"),
    BN("B RhD negative"),
    OP("O RhD positive"),
    ON("O RhD negative"),
    ABP("AB RhD positive"),
    ABN("AB RhD negative");

    private final String bloodGroup;
}
