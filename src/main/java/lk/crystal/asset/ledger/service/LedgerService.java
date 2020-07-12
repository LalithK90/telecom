package lk.crystal.asset.ledger.service;

import lk.crystal.asset.ledger.dao.LedgerDao;
import lk.crystal.asset.ledger.entity.Ledger;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "ledger")
public class LedgerService implements AbstractService<Ledger, Integer> {
    private final LedgerDao ledgerDao;

    @Autowired
    public LedgerService(LedgerDao ledgerDao) {
        this.ledgerDao = ledgerDao;
    }

    @Override
    public List<Ledger> findAll() {
        return ledgerDao.findAll();
    }

    @Override
    public Ledger findById(Integer id) {
        return ledgerDao.getOne(id);
    }

    @Override
    public Ledger persist(Ledger ledger) {
        return ledgerDao.save(ledger);
    }

    @Override
    public boolean delete(Integer id) {
        //not applicable
        return false;
    }

    @Override
    public List<Ledger> search(Ledger ledger) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Ledger> ledgerExample = Example.of(ledger, matcher);
        return ledgerDao.findAll(ledgerExample);
    }
}
