package lk.crystal.asset.supplier.dao;

import lk.crystal.asset.supplier.entity.Supplier;
import lk.crystal.asset.supplier_item.entity.enums.ItemSupplierStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
@Repository
public interface SupplierDao extends JpaRepository<Supplier, Integer> {
    Supplier findFirstByOrderByIdDesc();

    Supplier findByIdAndItemSupplierStatus(Integer supplierId, ItemSupplierStatus itemSupplierStatus);
}
