package lk.crystal.asset.payment.dao;



import lk.crystal.asset.payment.entity.Payment;
import lk.crystal.asset.purchaseOrder.entity.PurchaseOrder;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentDao extends JpaRepository< Payment,Integer> {
    List< Payment> findByPurchaseOrder(PurchaseOrder purchaseOrder);

    Payment findFirstByOrderByIdDesc();
}
