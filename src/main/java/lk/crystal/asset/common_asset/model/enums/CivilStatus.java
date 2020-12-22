package lk.crystal.asset.common_asset.model.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CivilStatus {
    MARRIED("Married"),
    UNMARRIED("Unmarried"),
    WIDOW("Widow");

    private final String civilStatus;
}
