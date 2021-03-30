package lk.crystal.asset.warranty_manage;

import lk.crystal.asset.invoice_ledger.service.InvoiceLedgerService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/warranty")
public class WarrantyController {

  private final InvoiceLedgerService invoiceLedgerService;

  public WarrantyController(InvoiceLedgerService invoiceLedgerService) {
    this.invoiceLedgerService = invoiceLedgerService;
  }

  @GetMapping("/form")
  public String form(){
    return "warranty/warranty";
  }
}
