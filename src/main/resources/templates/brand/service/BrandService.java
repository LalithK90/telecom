package lk.scubes.phonesAndAccessories.asset.brand.service;


import lk.scubes.phonesAndAccessories.asset.brand.dao.BrandDao;
import lk.scubes.phonesAndAccessories.asset.brand.entity.Brand;
import lk.scubes.phonesAndAccessories.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "brand")
public class BrandService implements AbstractService<Brand, Integer> {
    private final BrandDao brandDao;

    @Autowired
    public BrandService(BrandDao brandDao) {
        this.brandDao = brandDao;
    }

    @Override
    public List<Brand> findAll() {
        return brandDao.findAll();
    }

    @Override
    public Brand findById(Integer id) {
        return brandDao.getOne(id);
    }

    @Override
    public Brand persist(Brand brand) {
        return brandDao.save(brand);
    }

    @Override
    public boolean delete(Integer id) {
        brandDao.deleteById(id);
        //not applicable
        return false;
    }

    @Override
    public List<Brand> search(Brand brand) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<Brand> brandExample = Example.of(brand, matcher);
        return brandDao.findAll(brandExample);
    }
}
