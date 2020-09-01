package lk.crystal.asset.invoice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.util.stream.Collectors;

@Controller
@RequestMapping( "/invoice" )
public class InvoiceController {
    private final InvoiceService invoiceService;
    private final ItemService itemService;
    private final CustomerService customerService;
    private final LedgerService ledgerService;
    private final DateTimeAgeService dateTimeAgeService;
    private final DiscountRatioService discountRatioService;
    private final MakeAutoGenerateNumberService makeAutoGenerateNumberService;

    public InvoiceController(InvoiceService invoiceService, ItemService itemService, CustomerService customerService,
                             LedgerService ledgerService, DateTimeAgeService dateTimeAgeService,
                             DiscountRatioService discountRatioService,
                             MakeAutoGenerateNumberService makeAutoGenerateNumberService) {
        this.invoiceService = invoiceService;
        this.itemService = itemService;
        this.customerService = customerService;
        this.ledgerService = ledgerService;
        this.dateTimeAgeService = dateTimeAgeService;
        this.discountRatioService = discountRatioService;
        this.makeAutoGenerateNumberService = makeAutoGenerateNumberService;
    }

    @GetMapping
    public String invoice(Model model) {
        model.addAttribute("invoices",
                           invoiceService.findByCreatedAtIsBetween(dateTimeAgeService.dateTimeToLocalDateStartInDay(dateTimeAgeService.getPastDateByMonth(3)), dateTimeAgeService.dateTimeToLocalDateEndInDay(LocalDate.now())));
        model.addAttribute("firstInvoiceMessage", true);
        return "invoice/invoice";
    }

    @GetMapping( "/search" )
    public String invoiceSearch(@RequestAttribute( "startDate" ) LocalDate startDate,
                                @RequestAttribute( "endDate" ) LocalDate endDate, Model model) {
        model.addAttribute("invoices",
                           invoiceService.findByCreatedAtIsBetween(dateTimeAgeService.dateTimeToLocalDateStartInDay(startDate), dateTimeAgeService.dateTimeToLocalDateEndInDay(endDate)));
        model.addAttribute("firstInvoiceMessage", true);
        return "invoice/invoice";
    }

    private String common(Model model, Invoice invoice) {
        model.addAttribute("invoice", invoice);
        model.addAttribute("invoicePrintOrNots", InvoicePrintOrNot.values());
        model.addAttribute("paymentMethods", PaymentMethod.values());
        model.addAttribute("customers", customerService.findAll());
        model.addAttribute("discountRatios", discountRatioService.findAll());
        //send not expired and not zero quantity
        model.addAttribute("ledgers", ledgerService.findAll()
                .stream()
                .filter(x -> 0 < Integer.parseInt(x.getQuantity()) && x.getExpiredDate().isBefore( LocalDate.now()))
                .collect(Collectors.toList()));
        return "invoice/addInvoice";
    }

    @GetMapping( "/add" )
    public String getInvoiceForm(Model model) {
        return common(model, new Invoice());
    }

    @GetMapping( "/{id}" )
    public String viewDetails(@PathVariable Integer id, Model model) {
        model.addAttribute("invoiceDetail", invoiceService.findById(id));
        return "invoice/invoice-detail";
    }

    @PostMapping
    public String persistInvoice(@Valid @ModelAttribute Invoice invoice, BindingResult bindingResult, Model model) {
        if ( bindingResult.hasErrors() ) {
            return common(model, invoice);
        }
        if ( invoice.getId() == null ) {
            if ( invoiceService.findByLastInvoice() == null ) {
                //need to generate new one
                invoice.setCode("JNPI" + makeAutoGenerateNumberService.numberAutoGen(null).toString());
            } else {
                System.out.println("last customer not null");
                //if there is customer in db need to get that customer's code and increase its value
                String previousCode = invoiceService.findByLastInvoice().getCode().substring(4);
                invoice.setCode("JNPI" + makeAutoGenerateNumberService.numberAutoGen(previousCode).toString());
            }
        }
        invoice.setInvoiceValidOrNot(InvoiceValidOrNot.VALID);

        return "redirect:/invoice";
    }


    @GetMapping( "/remove/{id}" )
    public String removeInvoice(@PathVariable( "id" ) Integer id) {
        Invoice invoice = invoiceService.findById(id);
        invoice.setInvoiceValidOrNot(InvoiceValidOrNot.NOTVALID);
        invoiceService.persist(invoice);
        return "redirect:/invoice";
    }
} 
