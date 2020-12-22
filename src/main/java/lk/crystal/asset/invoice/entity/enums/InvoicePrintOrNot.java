package lk.crystal.asset.invoice.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoicePrintOrNot {
  PRINTED("No Want"),
  NOT_PRINTED("Want");
  private final String invoicePrintOrNot;
}
