package lk.crystal.asset.invoice.dao;

import lk.crystal.asset.invoice.entity.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;

public interface InvoiceDao extends JpaRepository<Invoice,Integer> {
}
