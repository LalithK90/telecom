package lk.crystal.asset.invoice.service;

import lk.crystal.asset.invoice.dao.InvoiceDao;
import lk.crystal.asset.invoice.entity.Invoice;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "invoice")
public class InvoiceService implements AbstractService<Invoice,Integer> {
    private final InvoiceDao invoiceDao;

    public InvoiceService(InvoiceDao invoiceDao) {
        this.invoiceDao = invoiceDao;
    }

    public List<Invoice> findAll() {
        return null;
    }

    public Invoice findById(Integer id) {
        return null;
    }

    public Invoice persist(Invoice invoice) {
        return null;
    }

    public boolean delete(Integer id) {
        return false;
    }

    public List<Invoice> search(Invoice invoice) {
        return null;
    }
}
