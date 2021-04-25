package lk.crystal.asset.invoice.service;


import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.invoice.dao.InvoiceDao;
import lk.crystal.asset.invoice.entity.Invoice;
import lk.crystal.asset.invoice.entity.enums.PaymentMethod;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class InvoiceService implements AbstractService<Invoice, Integer > {
  private final InvoiceDao invoiceDao;

  public InvoiceService(InvoiceDao invoiceDao) {
    this.invoiceDao = invoiceDao;
  }


  public List< Invoice > findAll() {
    return invoiceDao.findAll().stream()
            .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
            .collect(Collectors.toList());
  }

  public Invoice findById(Integer id) {
    return invoiceDao.getOne(id);
  }

  public Invoice persist(Invoice invoice) {
    if ( invoice.getId() == null ) {
      invoice.setLiveDead(LiveDead.ACTIVE);
    }
    return invoiceDao.save(invoice);
  }

  public boolean delete(Integer id) {
    Invoice invoice = invoiceDao.getOne(id);
    invoice.setLiveDead(LiveDead.STOP);
    invoiceDao.save(invoice);
    return false;
  }

  public List< Invoice > search(Invoice invoice) {
    ExampleMatcher matcher = ExampleMatcher
            .matching()
            .withIgnoreCase()
            .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< Invoice > invoiceExample = Example.of(invoice, matcher);
    return invoiceDao.findAll(invoiceExample);

  }

  public List< Invoice > findByCreatedAtIsBetween(LocalDateTime from, LocalDateTime to) {
    return invoiceDao.findByCreatedAtIsBetween(from, to);
  }

  public Invoice findByLastInvoice() {
    return invoiceDao.findFirstByOrderByIdDesc();
  }

  public List< Invoice > findByCreatedAtIsBetweenAndCreatedBy(LocalDateTime from, LocalDateTime to, String userName) {
    return invoiceDao.findByCreatedAtIsBetweenAndCreatedBy(from, to, userName);
  }

  public ByteArrayInputStream createPDF(Integer id) throws DocumentException {

    Invoice invoice = invoiceDao.getOne(id);

    ByteArrayOutputStream out = new ByteArrayOutputStream();
    Document document = new Document(PageSize.A4.rotate(), 15, 15, 45, 30);
    PdfWriter.getInstance(document, out);
    document.open();

    Font mainFont = FontFactory.getFont("Arial", 10, BaseColor.BLACK);
    Font secondaryFont = FontFactory.getFont("Arial", 8, BaseColor.BLACK);
    Font highLiltedFont = FontFactory.getFont("Arial", 8, BaseColor.BLACK);


    Paragraph paragraph = new Paragraph("Scubes Phones & Accessories \n \t\t Raddolugama\n ", mainFont);
    paragraph.setAlignment(Element.ALIGN_CENTER);
    paragraph.setIndentationLeft(50);
    paragraph.setIndentationRight(50);
    paragraph.setSpacingAfter(10);
    document.add(paragraph);

//customer details and invoice main details
    float[] columnWidths = {200f, 200f};//column amount{column 1 , column 2 }
    PdfPTable mainTable = new PdfPTable(columnWidths);
    // add cell to table
    PdfPCell cell = new PdfPCell(new Phrase("Date : \t" + invoice.getCreatedAt().format(DateTimeFormatter.ofPattern(
            "yyyy-MM-dd HH:mm:ss")), secondaryFont));
    pdfCellBodyCommonStyle(cell);
    mainTable.addCell(cell);

    PdfPCell cell1 = new PdfPCell(new Phrase("Invoice Number : " + invoice.getCode(), secondaryFont));
    pdfCellBodyCommonStyle(cell1);
    mainTable.addCell(cell1);

    PdfPCell cell2;
    if ( invoice.getCustomer() != null ) {
      cell2 =
              new PdfPCell(new Phrase("Name : " + invoice.getCustomer().getTitle().getTitle() + " " + invoice.getCustomer().getName(), secondaryFont));
    } else {
      cell2 = new PdfPCell(new Phrase("Name : Anonymous Customer ", secondaryFont));
    }
    pdfCellBodyCommonStyle(cell2);
    mainTable.addCell(cell2);

    document.add(mainTable);

    Rectangle page = document.getPageSize();

    PdfPTable ledgerItemDisplay = new PdfPTable(7);//column amount
    ledgerItemDisplay.setWidthPercentage(100);
    ledgerItemDisplay.setSpacingBefore(10f);
    ledgerItemDisplay.setSpacingAfter(10);
    ledgerItemDisplay.setTotalWidth(page.getWidth() - document.leftMargin() - document.rightMargin());

    Font tableHeader = FontFactory.getFont("Arial", 10, BaseColor.BLACK);
    Font tableHeaderOnly = FontFactory.getFont("Arial", 12, BaseColor.BLACK);


    PdfPCell indexHeader = new PdfPCell(new Paragraph("Index", tableHeaderOnly));
    pdfCellHeaderCommonStyle(indexHeader);
    ledgerItemDisplay.addCell(indexHeader);

    PdfPCell itemNameHeader = new PdfPCell(new Paragraph("Item Name", tableHeaderOnly));
    pdfCellHeaderCommonStyle(itemNameHeader);
    ledgerItemDisplay.addCell(itemNameHeader);

    PdfPCell unitPriceHeader = new PdfPCell(new Paragraph("Unit Price", tableHeaderOnly));
    pdfCellHeaderCommonStyle(unitPriceHeader);
    ledgerItemDisplay.addCell(unitPriceHeader);

    PdfPCell quantityHeader = new PdfPCell(new Paragraph("Quantity", tableHeaderOnly));
    pdfCellHeaderCommonStyle(quantityHeader);
    ledgerItemDisplay.addCell(quantityHeader);

    PdfPCell warrantyNumberHeader = new PdfPCell(new Paragraph("Warranty Number", tableHeaderOnly));
    pdfCellHeaderCommonStyle(warrantyNumberHeader);
    ledgerItemDisplay.addCell(warrantyNumberHeader);

    PdfPCell warrantyPeriodHeader = new PdfPCell(new Paragraph("Warranty Period", tableHeaderOnly));
    pdfCellHeaderCommonStyle(warrantyPeriodHeader);
    ledgerItemDisplay.addCell(warrantyPeriodHeader);

    PdfPCell lineTotalHeader = new PdfPCell(new Paragraph("Line Total", tableHeaderOnly));
    pdfCellHeaderCommonStyle(lineTotalHeader);
    ledgerItemDisplay.addCell(lineTotalHeader);

    for ( int i = 0; i < invoice.getInvoiceLedgers().size(); i++ ) {
      PdfPCell index = new PdfPCell(new Paragraph(Integer.toString(i+1), tableHeader));
      pdfCellBodyCommonStyle(index);
      ledgerItemDisplay.addCell(index);

      PdfPCell itemName =
              new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getLedger().getItem().getName(), tableHeader));
      pdfCellBodyCommonStyle(itemName);
      ledgerItemDisplay.addCell(itemName);

      PdfPCell unitPrice =
              new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getLedger().getSellPrice().toString(),
                      tableHeader));
      pdfCellBodyCommonStyle(unitPrice);
      ledgerItemDisplay.addCell(unitPrice);

      PdfPCell quantity = new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getQuantity(), tableHeader));
      pdfCellBodyCommonStyle(quantity);
      ledgerItemDisplay.addCell(quantity);

      PdfPCell warrantyNumber = new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getWarrantyNumber(), tableHeader));
      pdfCellBodyCommonStyle(warrantyNumber);
      ledgerItemDisplay.addCell(warrantyNumber);

      PdfPCell warrantyPeriod = new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getLedger().getItem().getWarrantyPeriod().getWarrantyPeriod(), tableHeader));
      pdfCellBodyCommonStyle(warrantyPeriod);
      ledgerItemDisplay.addCell(warrantyPeriod);

      PdfPCell lineTotal = new PdfPCell(new Paragraph(invoice.getInvoiceLedgers().get(i).getLineTotal().toString(),
              tableHeader));
      pdfCellBodyCommonStyle(lineTotal);
      ledgerItemDisplay.addCell(lineTotal);
    }

    document.add(ledgerItemDisplay);

    PdfPTable invoiceTable = new PdfPTable(new float[]{3f, 1f});

    PdfPCell totalAmount = new PdfPCell(new Phrase("\nTotal Amount(Rs.) : ", secondaryFont));
    commonStyleForPdfPCellLastOne(totalAmount);
    invoiceTable.addCell(totalAmount);

    PdfPCell totalAmountRs = new PdfPCell(new Phrase("---------------\n" + invoice.getTotalPrice().setScale(2,
            BigDecimal.ROUND_CEILING).toString(), secondaryFont));
    commonStyleForPdfPCellLastOne(totalAmountRs);
    invoiceTable.addCell(totalAmountRs);

    PdfPCell paymentMethodOnBill = new PdfPCell(new Phrase("\nPayment Method : ", secondaryFont));
    commonStyleForPdfPCellLastOne(paymentMethodOnBill);
    invoiceTable.addCell(paymentMethodOnBill);

    PdfPCell paymentMethodOnBillState =
            new PdfPCell(new Phrase("========\n" + invoice.getPaymentMethod().getPaymentMethod(), secondaryFont));
    commonStyleForPdfPCellLastOne(paymentMethodOnBillState);
    invoiceTable.addCell(paymentMethodOnBillState);

    PdfPCell discountRadioAndAmount =
            new PdfPCell(new Phrase("Discount ( " + invoice.getDiscountRatio().getAmount() + "% ) (Rs.) : ",
                    secondaryFont));
    commonStyleForPdfPCellLastOne(discountRadioAndAmount);
    invoiceTable.addCell(discountRadioAndAmount);

    PdfPCell discountRadioAndAmountRs = new PdfPCell(new Phrase(invoice.getDiscountAmount().toString(), secondaryFont));
    commonStyleForPdfPCellLastOne(discountRadioAndAmountRs);
    invoiceTable.addCell(discountRadioAndAmountRs);

    PdfPCell amount = new PdfPCell(new Phrase("Amount (Rs.) : ", highLiltedFont));
    commonStyleForPdfPCellLastOne(amount);
    invoiceTable.addCell(amount);

    PdfPCell amountRs = new PdfPCell(new Phrase(invoice.getTotalAmount().toString(), highLiltedFont));
    commonStyleForPdfPCellLastOne(amountRs);
    invoiceTable.addCell(amountRs);


    if ( invoice.getPaymentMethod().equals(PaymentMethod.CASH) ) {
      PdfPCell amountTendered = new PdfPCell(new Phrase("Tender Amount (Rs.) : ", secondaryFont));
      commonStyleForPdfPCellLastOne(amountTendered);
      invoiceTable.addCell(amountTendered);

      PdfPCell amountTenderedRs = new PdfPCell(new Phrase(invoice.getAmountTendered().toString(), secondaryFont));
      commonStyleForPdfPCellLastOne(amountTenderedRs);
      invoiceTable.addCell(amountTenderedRs);

      PdfPCell balance = new PdfPCell(new Phrase("Balance (Rs.) : ", highLiltedFont));
      commonStyleForPdfPCellLastOne(balance);
      invoiceTable.addCell(balance);

      PdfPCell balanceRs = new PdfPCell(new Phrase(invoice.getBalance().toString(), highLiltedFont));
      commonStyleForPdfPCellLastOne(balanceRs);
      invoiceTable.addCell(balanceRs);

    } else {
      PdfPCell bank = new PdfPCell(new Phrase("Bank Name : ", secondaryFont));
      commonStyleForPdfPCellLastOne(bank);
      invoiceTable.addCell(bank);

      PdfPCell bankName = new PdfPCell(new Phrase(invoice.getBankName(), secondaryFont));
      commonStyleForPdfPCellLastOne(bankName);
      invoiceTable.addCell(bankName);
    }

    document.add(invoiceTable);

    Paragraph remarks = new Paragraph("Remarks : " + invoice.getRemarks(), secondaryFont);
    commonStyleForParagraphTwo(remarks);
    document.add(remarks);

    Paragraph message = new Paragraph("\nWe will not accept return without invoiced. \n\n " +
            "------------------------------------\n            ( " + invoice.getCreatedBy() + " )", secondaryFont);
    commonStyleForParagraphTwo(message);
    document.add(message);

    document.close();
    return new ByteArrayInputStream(out.toByteArray());
  }


  private void pdfCellHeaderCommonStyle(PdfPCell pdfPCell) {
    pdfPCell.setBorderColor(BaseColor.BLACK);
    pdfPCell.setPaddingLeft(10);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setBackgroundColor(BaseColor.DARK_GRAY);
    pdfPCell.setExtraParagraphSpace(5f);
  }

  private void pdfCellBodyCommonStyle(PdfPCell pdfPCell) {
    pdfPCell.setBorderColor(BaseColor.BLACK);
    pdfPCell.setPaddingLeft(10);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setVerticalAlignment(Element.ALIGN_CENTER);
    pdfPCell.setBackgroundColor(BaseColor.WHITE);
    pdfPCell.setExtraParagraphSpace(5f);
  }

  private void commonStyleForPdfPCellLastOne(PdfPCell pdfPCell) {
    pdfPCell.setBorder(0);
    pdfPCell.setHorizontalAlignment(Element.ALIGN_RIGHT);
    pdfPCell.setBorderColor(BaseColor.WHITE);
  }

  private void commonStyleForParagraphTwo(Paragraph paragraph) {
    paragraph.setAlignment(Element.ALIGN_LEFT);
    paragraph.setIndentationLeft(50);
    paragraph.setIndentationRight(50);
  }
}