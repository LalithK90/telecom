package lk.crystal.asset.purchaseOrder.dao;



import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.purchaseOrder.entity.PurchaseOrder;
import lk.crystal.asset.purchaseOrder.entity.PurchaseOrderItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PurchaseOrderItemDao extends JpaRepository< PurchaseOrderItem, Integer> {
    PurchaseOrderItem findByPurchaseOrderAndItem(PurchaseOrder purchaseOrder, Item item);
    List<PurchaseOrderItem> findByPurchaseOrder(PurchaseOrder purchaseOrder);
}
