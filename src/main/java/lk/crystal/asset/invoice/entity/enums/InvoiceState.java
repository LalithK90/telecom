package lk.crystal.asset.invoice.entity.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InvoiceState {
    PAID("Paid"),
    CANCELLED("Cancelled");

    private final String invoiceState;
}
