package lk.crystal.asset.report;

import lk.crystal.asset.common_asset.model.NameCount;

import lk.crystal.asset.common_asset.model.ParameterCount;
import lk.crystal.asset.common_asset.model.TwoDate;
import lk.crystal.asset.employee.entity.Employee;
import lk.crystal.asset.employee.service.EmployeeService;
import lk.crystal.asset.invoice.entity.Invoice;
import lk.crystal.asset.invoice.entity.enums.PaymentMethod;
import lk.crystal.asset.invoice.service.InvoiceService;
import lk.crystal.asset.invoice_ledger.entity.InvoiceLedger;
import lk.crystal.asset.invoice_ledger.service.InvoiceLedgerService;
import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.item.entity.enums.MainCategory;
import lk.crystal.asset.ledger.entity.Ledger;
import lk.crystal.asset.ledger.service.LedgerService;
import lk.crystal.asset.payment.entity.Payment;
import lk.crystal.asset.payment.service.PaymentService;
import lk.crystal.asset.user_management.user.service.UserService;
import lk.crystal.util.service.DateTimeAgeService;
import lk.crystal.util.service.OperatorService;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

import static lk.crystal.asset.item.entity.enums.MainCategory.ACCESSORIES;
import static lk.crystal.asset.item.entity.enums.MainCategory.MOBILE_PHONE;

@Controller
@RequestMapping( "/report" )
public class ReportController {
  private final PaymentService paymentService;
  private final InvoiceService invoiceService;
  private final OperatorService operatorService;
  private final DateTimeAgeService dateTimeAgeService;
  private final UserService userService;
  private final InvoiceLedgerService invoiceLedgerService;
  private final EmployeeService employeeService;
  private final LedgerService ledgerService;

  public ReportController(PaymentService paymentService, InvoiceService invoiceService,
                          OperatorService operatorService, DateTimeAgeService dateTimeAgeService,
                          UserService userService, InvoiceLedgerService invoiceLedgerService,
                          EmployeeService employeeService, LedgerService ledgerService) {
    this.paymentService = paymentService;
    this.invoiceService = invoiceService;
    this.operatorService = operatorService;
    this.dateTimeAgeService = dateTimeAgeService;
    this.userService = userService;
    this.invoiceLedgerService = invoiceLedgerService;
    this.employeeService = employeeService;
    this.ledgerService = ledgerService;
  }

  private String commonAll(List< Payment > payments, List< Invoice > invoices, Model model, String message,
                           LocalDateTime startDateTime, LocalDateTime endDateTime) {
    //according to payment type -> invoice
    commonInvoices(invoices, model);
    //according to payment type -> payment
    commonPayment(payments, model);
    // invoice count by cashier
    commonPerCashier(invoices, model);
    // payment count by account department
    commonPerAccountUser(payments, model);
    // item count according to item
    commonPerItem(startDateTime, endDateTime, model);

    model.addAttribute("message", message);
    return "report/paymentAndIncomeReport";
  }

  @GetMapping( "/manager" )
  public String getAllInvoiceAndPayment(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);

    return commonAll(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime),
                     invoiceService.findByCreatedAtIsBetween(startDateTime, endDateTime), model, message,
                     startDateTime, endDateTime);

  }

  @PostMapping( "/manager/search" )
  public String getAllInvoiceAndPaymentBetweenTwoDate(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message =
        "This report is between from " + twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    return commonAll(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime),
                     invoiceService.findByCreatedAtIsBetween(startDateTime, endDateTime), model, message,
                     startDateTime, endDateTime);
  }

  private void commonInvoices(List< Invoice > invoices, Model model) {
    // invoice count
    int invoiceTotalCount = invoices.size();
    model.addAttribute("invoiceTotalCount", invoiceTotalCount);
    //|-> card
    List< Invoice > invoiceCards =
        invoices.stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CREDIT)).collect(Collectors.toList());
    int invoiceCardCount = invoiceCards.size();
    AtomicReference< BigDecimal > invoiceCardAmount = new AtomicReference<>(BigDecimal.ZERO);
    invoiceCards.forEach(x -> {
      BigDecimal addAmount = operatorService.addition(invoiceCardAmount.get(), x.getTotalAmount());
      invoiceCardAmount.set(addAmount);
    });
    model.addAttribute("invoiceCardCount", invoiceCardCount);
    model.addAttribute("invoiceCardAmount", invoiceCardAmount.get());
    //|-> cash
    List< Invoice > invoiceCash =
        invoices.stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CASH)).collect(Collectors.toList());
    int invoiceCashCount = invoiceCash.size();
    AtomicReference< BigDecimal > invoiceCashAmount = new AtomicReference<>(BigDecimal.ZERO);
    invoiceCash.forEach(x -> {
      BigDecimal addAmount = operatorService.addition(invoiceCashAmount.get(), x.getTotalAmount());
      invoiceCashAmount.set(addAmount);
    });
    model.addAttribute("invoiceCashCount", invoiceCashCount);
    model.addAttribute("invoiceCashAmount", invoiceCashAmount.get());

  }

  @GetMapping( "/cashier" )
  public String getCashierToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate.toString() + " and \n congratulation all are done by " +
        "you.";
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonInvoices(invoiceService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                       SecurityContextHolder.getContext().getAuthentication().getName()), model);
    model.addAttribute("message", message);
    return "report/cashierReport";
  }

  @PostMapping( "/cashier/search" )
  public String getCashierSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message =
        "This report is between from " + twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString() + " and \n congratulation all are done by you.";
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonInvoices(invoiceService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                       SecurityContextHolder.getContext().getAuthentication().getName()), model);
    model.addAttribute("message", message);
    return "report/cashierReport";
  }

  private void commonPayment(List< Payment > payments, Model model) {
    // payment count
    int paymentTotalCount = payments.size();
    model.addAttribute("paymentTotalCount", paymentTotalCount);
    //|-> card
    List< Payment > paymentCards =
        payments.stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CREDIT)).collect(Collectors.toList());
    int paymentCardCount = paymentCards.size();
    AtomicReference< BigDecimal > paymentCardAmount = new AtomicReference<>(BigDecimal.ZERO);
    paymentCards.forEach(x -> {
      BigDecimal addAmount = operatorService.addition(paymentCardAmount.get(), x.getAmount());
      paymentCardAmount.set(addAmount);
    });
    model.addAttribute("paymentCardCount", paymentCardCount);
    model.addAttribute("paymentCardAmount", paymentCardAmount.get());
    //|-> cash
    List< Payment > paymentCash =
        payments.stream().filter(x -> x.getPaymentMethod().equals(PaymentMethod.CASH)).collect(Collectors.toList());
    int paymentCashCount = paymentCash.size();
    AtomicReference< BigDecimal > paymentCashAmount = new AtomicReference<>(BigDecimal.ZERO);
    paymentCash.forEach(x -> {
      BigDecimal addAmount = operatorService.addition(paymentCashAmount.get(), x.getAmount());
      paymentCashAmount.set(addAmount);
    });
    model.addAttribute("paymentCashCount", paymentCashCount);
    model.addAttribute("paymentCardAmount", paymentCashAmount.get());

  }

  @GetMapping( "/payment" )
  public String getPaymentToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate.toString() + " and \n congratulation all are done by " +
        "you.";
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonPayment(paymentService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                      SecurityContextHolder.getContext().getAuthentication().getName()), model);
    model.addAttribute("message", message);
    return "report/paymentReport";
  }

  @PostMapping( "/payment/search" )
  public String getPaymentSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message =
        "This report is between from " + twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString() + " and \n congratulation all are done by you.";
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonPayment(paymentService.findByCreatedAtIsBetweenAndCreatedBy(startDateTime, endDateTime,
                                                                      SecurityContextHolder.getContext().getAuthentication().getName()), model);
    model.addAttribute("message", message);
    return "report/paymentReport";
  }

  private void commonPerCashier(List< Invoice > invoices, Model model) {
    List< NameCount > invoiceByCashierAndTotalAmount = new ArrayList<>();
//name, count, total
    HashSet< String > createdByAll = new HashSet<>();
    invoices.forEach(x -> createdByAll.add(x.getCreatedBy()));
    System.out.println(" size "+ createdByAll.size());
    createdByAll.forEach(x -> {
      NameCount nameCount = new NameCount();
      Employee employee = employeeService.findById(userService.findByUserName(x).getEmployee().getId());
      nameCount.setName(employee.getTitle().getTitle() + " " + employee.getName());
      AtomicReference< BigDecimal > cashierTotalCount = new AtomicReference<>(BigDecimal.ZERO);
      List< Invoice > cashierInvoice =
          invoices.stream().filter(a -> a.getCreatedBy().equals(x)).collect(Collectors.toList());
      nameCount.setCount(cashierInvoice.size());
      cashierInvoice.forEach(a -> {
        BigDecimal addAmount = operatorService.addition(cashierTotalCount.get(), a.getTotalAmount());
        cashierTotalCount.set(addAmount);
      });
      nameCount.setTotal(cashierTotalCount.get());
      invoiceByCashierAndTotalAmount.add(nameCount);
    });
    model.addAttribute("invoiceByCashierAndTotalAmount", invoiceByCashierAndTotalAmount);
  }

  @GetMapping( "/perCashier" )
  public String perCashierToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonPerCashier(invoiceService.findByCreatedAtIsBetween(startDateTime, endDateTime), model);
    model.addAttribute("message", message);
    return "report/perCashierReport";
  }

  @PostMapping( "/perCashier/search" )
  public String getPerCashierSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message =
        "This report is between from " + twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString() + " and \n congratulation all are done by you.";
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonPerCashier(invoiceService.findByCreatedAtIsBetween(startDateTime, endDateTime), model);
    model.addAttribute("message", message);
    return "report/perCashierReport";
  }

  private void commonPerAccountUser(List< Payment > payments, Model model) {
    List< NameCount > paymentByUserAndTotalAmount = new ArrayList<>();
//name, count, total
    HashSet< String > createdByAllPayment = new HashSet<>();
    payments.forEach(x -> createdByAllPayment.add(x.getCreatedBy()));

    createdByAllPayment.forEach(x -> {
      NameCount nameCount = new NameCount();
      Employee employee = employeeService.findById(userService.findByUserName(x).getEmployee().getId());
      nameCount.setName(employee.getTitle().getTitle() + " " + employee.getName());
      AtomicReference< BigDecimal > userTotalCount = new AtomicReference<>(BigDecimal.ZERO);
      List< Payment > paymentUser =
          payments.stream().filter(a -> a.getCreatedBy().equals(x)).collect(Collectors.toList());
      nameCount.setCount(paymentUser.size());
      paymentUser.forEach(a -> {
        BigDecimal addAmount = operatorService.addition(userTotalCount.get(), a.getAmount());
        userTotalCount.set(addAmount);
      });
      nameCount.setTotal(userTotalCount.get());
      paymentByUserAndTotalAmount.add(nameCount);
    });

    model.addAttribute("paymentByUserAndTotalAmount", paymentByUserAndTotalAmount);

  }

  @GetMapping( "/perAccount" )
  public String perAccountToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonPerAccountUser(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime), model);
    model.addAttribute("message", message);
    return "report/perAccountReport";
  }

  @PostMapping( "/perAccount/search" )
  public String getPerAccountSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message =
        "This report is between from " + twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString() + " and \n congratulation all are done by you.";
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonPerAccountUser(paymentService.findByCreatedAtIsBetween(startDateTime, endDateTime), model);
    model.addAttribute("message", message);
    return "report/perAccountReport";
  }

  private void commonPerItem(LocalDateTime startDateTime, LocalDateTime endDateTime, Model model) {
    HashSet< Item > invoiceItems = new HashSet<>();

    List< ParameterCount > itemNameAndItemCount = new ArrayList<>();

    List< InvoiceLedger > invoiceLedgers = invoiceLedgerService.findByCreatedAtIsBetween(startDateTime, endDateTime);
    invoiceLedgers.forEach(x -> invoiceItems.add(x.getLedger().getItem()));

    invoiceItems.forEach(x -> {
      ParameterCount parameterCount = new ParameterCount();
      parameterCount.setName(x.getName());
      parameterCount.setCount((int) invoiceLedgers
          .stream()
          .filter(a -> a.getLedger().getItem().equals(x))
          .count());
      itemNameAndItemCount.add(parameterCount);
    });
    model.addAttribute("itemNameAndItemCount", itemNameAndItemCount);

  }

  @GetMapping( "/perItem" )
  public String perItemToday(Model model) {
    LocalDate localDate = LocalDate.now();
    String message = "This report is belongs to " + localDate.toString();
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(localDate);
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(localDate);
    commonPerItem(startDateTime, endDateTime, model);
    model.addAttribute("message", message);
    return "report/perItemReport";
  }

  @PostMapping( "/perItem/search" )
  public String getPerItemSearch(@ModelAttribute( "twoDate" ) TwoDate twoDate, Model model) {
    String message =
        "This report is between from " + twoDate.getStartDate().toString() + " to " + twoDate.getEndDate().toString() + " and \n congratulation all are done by you.";
    LocalDateTime startDateTime = dateTimeAgeService.dateTimeToLocalDateStartInDay(twoDate.getStartDate());
    LocalDateTime endDateTime = dateTimeAgeService.dateTimeToLocalDateEndInDay(twoDate.getEndDate());
    commonPerItem(startDateTime, endDateTime, model);
    model.addAttribute("message", message);
    return "report/perItemReport";
  }

  @GetMapping("/searchByCategory")
  public String searchItemsByCategory(Model model) {
    model.addAttribute("ledger", new Ledger());

    return "report/searchByCategory";
  }


  @PostMapping("/itemsByCategory")
  public String getItemsByCategory(@ModelAttribute Ledger ledger, Model model) {
   /* MainCategory mainCategory = ACCESSORIES;*/
    model.addAttribute("ledgers", ledgerService.findByCategory(ledger.getMainCategory()));


    return "report/itemsByCategoryReport";
  }

}
