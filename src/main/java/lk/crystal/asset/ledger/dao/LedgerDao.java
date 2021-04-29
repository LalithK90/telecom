package lk.crystal.asset.ledger.dao;


import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.item.entity.enums.MainCategory;
import lk.crystal.asset.ledger.entity.Ledger;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface LedgerDao extends JpaRepository< Ledger, Integer> {
    List<Ledger> findByItem(Item item);

    Ledger findByItemAndSellPrice(Item item, BigDecimal sellPrice);



    List< Ledger > findByCreatedAtBetween(LocalDateTime form, LocalDateTime to);

    List< Ledger > findByMainCategory(MainCategory mainCategory);
}
