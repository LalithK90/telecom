package lk.crystal.asset.supplier.service;

import lk.crystal.asset.supplier.dao.SupplierItemDao;
import lk.crystal.asset.supplier.entity.SupplierItem;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "supplierItem")
public class SupplierItemService implements AbstractService<SupplierItem, Integer> {
    private final SupplierItemDao supplierItemDao;

    @Autowired
    public SupplierItemService(SupplierItemDao supplierItemDao) {
        this.supplierItemDao = supplierItemDao;
    }

    public List<SupplierItem> findAll() {
        return supplierItemDao.findAll();
    }

    public SupplierItem findById(Integer id) {
        return supplierItemDao.getOne(id);
    }

    public SupplierItem persist(SupplierItem supplierItem) {
        return supplierItemDao.save(supplierItem);
    }

    public boolean delete(Integer id) {
        supplierItemDao.deleteById(id);
        return false;
    }

    public List<SupplierItem> search(SupplierItem supplierItem) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<SupplierItem> supplierItemExample = Example.of(supplierItem, matcher);
        return supplierItemDao.findAll(supplierItemExample);
    }

}
