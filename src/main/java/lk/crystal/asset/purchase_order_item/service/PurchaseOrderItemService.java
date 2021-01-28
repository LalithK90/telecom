package lk.crystal.asset.purchase_order_item.service;


import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.purchase_order.entity.PurchaseOrder;
import lk.crystal.asset.purchase_order_item.dao.PurchaseOrderItemDao;
import lk.crystal.asset.purchase_order_item.entity.PurchaseOrderItem;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "purchaseOrderItem")
public class PurchaseOrderItemService implements AbstractService< PurchaseOrderItem, Integer> {
    private final PurchaseOrderItemDao purchaseOrderItemDao;

    @Autowired
    public PurchaseOrderItemService(PurchaseOrderItemDao purchaseOrderItemDao) {
        this.purchaseOrderItemDao = purchaseOrderItemDao;
    }

    public List<PurchaseOrderItem> findAll() {
        return purchaseOrderItemDao.findAll();
    }

    public PurchaseOrderItem findById(Integer id) {
        return purchaseOrderItemDao.getOne(id);
    }

    public PurchaseOrderItem persist(PurchaseOrderItem purchaseOrderItem) {
        if(purchaseOrderItem.getId()==null){
            purchaseOrderItem.setLiveDead(LiveDead.ACTIVE);}
        return purchaseOrderItemDao.save(purchaseOrderItem);
    }

    public boolean delete(Integer id) {
        PurchaseOrderItem purchaseOrderItem =  purchaseOrderItemDao.getOne(id);
        purchaseOrderItem.setLiveDead(LiveDead.STOP);
        purchaseOrderItemDao.save(purchaseOrderItem);
        return false;
    }

    public List<PurchaseOrderItem> search(PurchaseOrderItem purchaseOrderItem) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<PurchaseOrderItem> purchaseRequestExample = Example.of(purchaseOrderItem, matcher);
        return purchaseOrderItemDao.findAll(purchaseRequestExample);
    }

    public PurchaseOrderItem findByPurchaseOrderAndItem(PurchaseOrder purchaseOrder, Item item) {
        return purchaseOrderItemDao.findByPurchaseOrderAndItem(purchaseOrder, item);
    }

    public List<PurchaseOrderItem> findByPurchaseOrder(PurchaseOrder purchaseOrder) {
        return purchaseOrderItemDao.findByPurchaseOrder(purchaseOrder);
    }



}
