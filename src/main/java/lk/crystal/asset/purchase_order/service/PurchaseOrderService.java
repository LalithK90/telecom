package lk.crystal.asset.purchase_order.service;


import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.purchase_order.dao.PurchaseOrderDao;
import lk.crystal.asset.purchase_order.entity.PurchaseOrder;
import lk.crystal.asset.purchase_order.entity.enums.PurchaseOrderStatus;
import lk.crystal.asset.supplier.entity.Supplier;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig( cacheNames = "purchaseOrder" )
public class PurchaseOrderService implements AbstractService< PurchaseOrder, Integer > {
    private final PurchaseOrderDao purchaseOrderDao;

    @Autowired
    public PurchaseOrderService(PurchaseOrderDao purchaseOrderDao) {
        this.purchaseOrderDao = purchaseOrderDao;
    }

    public List< PurchaseOrder > findAll() {
        return purchaseOrderDao.findAll().stream()
            .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
            .collect(Collectors.toList());
    }

    public PurchaseOrder findById(Integer id) {
        return purchaseOrderDao.getOne(id);
    }

    public PurchaseOrder persist(PurchaseOrder purchaseOrder) {
        if(purchaseOrder.getId()==null){
            purchaseOrder.setLiveDead(LiveDead.ACTIVE);}
        return purchaseOrderDao.save(purchaseOrder);
    }

    public boolean delete(Integer id) {
        PurchaseOrder purchaseOrder =  purchaseOrderDao.getOne(id);
        purchaseOrder.setLiveDead(LiveDead.STOP);
        purchaseOrderDao.save(purchaseOrder);
        return false;
    }

    public List< PurchaseOrder > search(PurchaseOrder purchaseOrder) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< PurchaseOrder > purchaseRequestExample = Example.of(purchaseOrder, matcher);
        return purchaseOrderDao.findAll(purchaseRequestExample);
    }

    public List< PurchaseOrder > findByPurchaseOrderStatus(PurchaseOrderStatus purchaseOrderStatus) {
        return purchaseOrderDao.findByPurchaseOrderStatus(purchaseOrderStatus);
    }


    public PurchaseOrder lastPurchaseOrder() {
        return purchaseOrderDao.findFirstByOrderByIdDesc();
    }

    public List< PurchaseOrder > findByPurchaseOrderStatusAndSupplier(PurchaseOrderStatus purchaseOrderStatus,
                                                                      Supplier supplier) {
        return purchaseOrderDao.findByPurchaseOrderStatusAndSupplier(purchaseOrderStatus, supplier);
    }

    public List< PurchaseOrder> findByCreatedAtIsBetween(LocalDateTime form, LocalDateTime to) {
    return purchaseOrderDao.findByCreatedAtIsBetween(form, to);
    }
}
