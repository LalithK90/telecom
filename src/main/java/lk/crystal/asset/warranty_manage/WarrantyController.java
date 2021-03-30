package lk.crystal.asset.warranty_manage;

import lk.crystal.asset.employee.entity.Employee;
import lk.crystal.asset.invoice.entity.Invoice;
import lk.crystal.asset.invoice.service.InvoiceService;
import lk.crystal.asset.invoice_ledger.entity.InvoiceLedger;
import lk.crystal.asset.invoice_ledger.service.InvoiceLedgerService;
import lk.crystal.asset.user_management.user.entity.User;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/warranty" )
public class WarrantyController {

  private final InvoiceLedgerService invoiceLedgerService;
  private final InvoiceService invoiceService;

  public WarrantyController(InvoiceLedgerService invoiceLedgerService, InvoiceService invoiceService) {
    this.invoiceLedgerService = invoiceLedgerService;
    this.invoiceService = invoiceService;
  }

  @GetMapping( "/form" )
  public String form() {

    return "warranty/warranty";
  }

  /*Test code*/
  @PostMapping( value = "/search" )
  public String addInvoiceDetails(@ModelAttribute Invoice invoice, Model model) {

    if ( invoice.getCode() != null ) {

      List< Invoice > invoices = invoiceService.search(invoice);

      if ( invoices.size() == 1 ) {
        Invoice invoiceDb = invoiceService.findById(invoices.get(0).getId());
        model.addAttribute("invoiceDetail", invoiceDb);
        model.addAttribute("customerDetail", invoiceDb.getCustomer());
        return "invoice/invoice-detail";
      }

    }
    if ( invoice.getInvoiceLedgers().get(0).getWarrantyNumber() != null ) {
      InvoiceLedger invoiceLedger =
          invoiceLedgerService.findByWarrantyNumber(invoice.getInvoiceLedgers().get(0).getWarrantyNumber());
      Invoice invoiceDb = invoiceService.findById(invoiceLedger.getInvoice().getId());
      model.addAttribute("invoiceDetail", invoiceDb);
      model.addAttribute("customerDetail", invoiceDb.getCustomer());
      return "invoice/invoice-detail";
    }

    model.addAttribute("messageShow", true);
    model.addAttribute("message", "There is not invoice in the system according to the provided details \n Could " +
        "you please search again !!");
    return "warranty/warranty";
  }
}
