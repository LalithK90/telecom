package lk.crystal.asset.invoice.entity.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoiceValidOrNot {
    VALID("Valid"),
    NOTVALID("No Valid");
    private final String invoiceValidOrNot;
}
