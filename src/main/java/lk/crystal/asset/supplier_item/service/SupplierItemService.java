package lk.crystal.asset.supplier_item.service;


import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.supplier.entity.Supplier;
import lk.crystal.asset.supplier_item.dao.SupplierItemDao;
import lk.crystal.asset.supplier_item.entity.SupplierItem;
import lk.crystal.asset.supplier_item.entity.enums.ItemSupplierStatus;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@CacheConfig( cacheNames = "supplierItem" )
public class SupplierItemService implements AbstractService< SupplierItem, Integer > {
  private final SupplierItemDao supplierItemDao;

  @Autowired
  public SupplierItemService(SupplierItemDao supplierItemDao) {
    this.supplierItemDao = supplierItemDao;
  }

  public List< SupplierItem > findAll() {
    return supplierItemDao.findAll();
  }

  public SupplierItem findById(Integer id) {
    return supplierItemDao.getOne(id);
  }
@Transactional
  public SupplierItem persist(SupplierItem supplierItem) {
    //if item is new supplier should be save as currently buying item
    if ( supplierItem.getId() == null ) {
      supplierItem.setLiveDead(LiveDead.ACTIVE);
      supplierItem.setItemSupplierStatus(ItemSupplierStatus.CURRENTLY_BUYING);
    }
    //if item buying price was changed (increase/decrease) by supplier,
    // need to change that item as supplier not currently buying and save as new supplier_item
    if ( supplierItem.getId() != null ) {
      SupplierItem supplierItemDB = supplierItemDao.getOne(supplierItem.getId());
      if ( !supplierItem.getPrice().equals(supplierItemDB.getPrice()) ) {
        //price change item save as new item
        supplierItem.setItemSupplierStatus(ItemSupplierStatus.CURRENTLY_BUYING);
        supplierItem.setId(null);
        //already saved item was change to not currently buying
        supplierItemDB.setItemSupplierStatus(ItemSupplierStatus.STOPPED);
        supplierItemDao.save(supplierItemDB);
      }
    }
    return supplierItemDao.save(supplierItem);
  }

  public boolean delete(Integer id) {
    SupplierItem supplierItem = supplierItemDao.getOne(id);
    supplierItem.setLiveDead(LiveDead.STOP);
    supplierItemDao.save(supplierItem);
    return false;
  }

  public List< SupplierItem > search(SupplierItem supplierItem) {
    ExampleMatcher matcher = ExampleMatcher
        .matching()
        .withIgnoreCase()
        .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
    Example< SupplierItem > supplierItemExample = Example.of(supplierItem, matcher);
    return supplierItemDao.findAll(supplierItemExample);
  }

  public SupplierItem findBySupplierAndItem(Supplier supplier, Item item) {
    return supplierItemDao.findBySupplierAndItem(supplier, item);
  }

  public List< SupplierItem > findBySupplier(Supplier supplier) {
    return supplierItemDao.findBySupplier(supplier);
  }

  public List< SupplierItem > findBySupplierAndItemSupplierStatus(Supplier supplier,
                                                                  ItemSupplierStatus itemSupplierStatus) {
    return supplierItemDao.findBySupplierAndItemSupplierStatus(supplier, itemSupplierStatus);
  }

  public List< Supplier > findByItem(Item item) {
    List< Supplier > suppliers = new ArrayList<>();
    supplierItemDao.findByItem(item).forEach(x -> {
      suppliers.add(x.getSupplier());
    });
    return suppliers;
  }

  public Item findByItemAndSupplier(Item item, Supplier supplier) {
    return supplierItemDao.findByItemAndSupplier(item, supplier);
  }

  public SupplierItem findBySupplierAndItemItemSupplierStatus(Supplier supplier, Item item,
                                                              ItemSupplierStatus itemSupplierStatus) {
    return supplierItemDao.findBySupplierAndItemAndItemSupplierStatus(supplier, item, itemSupplierStatus);
  }
}
