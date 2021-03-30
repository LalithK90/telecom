package lk.crystal.asset.invoice_ledger.service;

import lk.crystal.asset.invoice_ledger.dao.InvoiceLedgerDao;
import lk.crystal.asset.invoice_ledger.entity.InvoiceLedger;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class InvoiceLedgerService {
private final InvoiceLedgerDao invoiceLedgerDao;

  public InvoiceLedgerService(InvoiceLedgerDao invoiceLedgerDao) {
    this.invoiceLedgerDao = invoiceLedgerDao;
  }

  public List< InvoiceLedger> findByCreatedAtIsBetween(LocalDateTime from, LocalDateTime to) {
  return invoiceLedgerDao.findByCreatedAtIsBetween(from,to);
  }

    public InvoiceLedger findByWarrantyNumber(String warrantyNumber) {
    return invoiceLedgerDao.findByWarrantyNumber(warrantyNumber);
    }
}
