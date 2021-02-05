package lk.crystal.asset.item.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum WarrantyPeriod {
  NOW("No Warrenty"),
  ONEM("One Month"),
  THREEM("Three Month"),
  SIXM("Six Month"),
  ONEY("One Year"),
  TWOY("Two Year"),
  FIVEY("Five Year");
  private final String warrantyPeriod;
}
