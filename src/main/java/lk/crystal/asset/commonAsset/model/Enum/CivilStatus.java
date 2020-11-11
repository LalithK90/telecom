package lk.crystal.asset.commonAsset.model.Enum;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CivilStatus {

    MARRIED("Married"),
    UNMARRIED("UnMarried");

    private final String civilStatus;


}
