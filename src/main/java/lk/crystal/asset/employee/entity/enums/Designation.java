package lk.crystal.asset.employee.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Designation {
  ADMIN("Admin"),
  MANAGER("Manager"),
  PROCUREMENT_MANAGER("Procurement Manager"),
  HR_MANAGER("HR Manager"),
  ACCOUNT_MANAGER("Account Manager"),
  CASHIER("Cashier"),
  CLEANER("Cleaner");

  private final String designation;
}
