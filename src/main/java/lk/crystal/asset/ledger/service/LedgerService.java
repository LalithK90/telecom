package lk.crystal.asset.ledger.service;


import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.item.entity.Item;
import lk.crystal.asset.item.entity.enums.MainCategory;
import lk.crystal.asset.ledger.dao.LedgerDao;
import lk.crystal.asset.ledger.entity.Ledger;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@CacheConfig(cacheNames = "ledger")
public class LedgerService implements AbstractService< Ledger, Integer> {
    private final LedgerDao ledgerDao;

    public LedgerService(LedgerDao ledgerDao) {
        this.ledgerDao = ledgerDao;
    }


    public List<Ledger> findAll() {
        return ledgerDao.findAll().stream()
            .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
            .collect(Collectors.toList());
    }


    public Ledger findById(Integer id) {
        return ledgerDao.getOne(id);
    }


    public Ledger persist(Ledger ledger) {
        if(ledger.getId()==null){
            ledger.setLiveDead(LiveDead.ACTIVE);}
        return ledgerDao.save(ledger);
    }

    public boolean delete(Integer id) {
        Ledger ledger =  ledgerDao.getOne(id);
        ledger.setLiveDead(LiveDead.STOP);
        ledgerDao.save(ledger);
        return false;
    }


    public List<Ledger> search(Ledger ledger) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Ledger> ledgerExample = Example.of(ledger, matcher);
        return ledgerDao.findAll(ledgerExample);
    }

    public List<Ledger> findByItem(Item item) {
        return ledgerDao.findByItem(item);
    }

    public Ledger findByItemAndSellPrice(Item item,  BigDecimal sellPrice) {
    return ledgerDao.findByItemAndSellPrice( item, sellPrice);
    }

    public List<Ledger> findByCreatedAtIsBetween(LocalDateTime startDate, LocalDateTime endDate) {
        return ledgerDao.findByCreatedAtBetween(startDate, endDate);
    }

    public Object findByCategory(MainCategory mainCategory) {
        return ledgerDao.findByMainCategory(mainCategory);
    }



   /* public Ledger findByItem(Item item) {
        return ledgerDao.findByItem(item);
    }*/
}
