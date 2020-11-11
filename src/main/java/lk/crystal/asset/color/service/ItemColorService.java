package lk.crystal.asset.color.service;

import lk.crystal.asset.color.dao.ItemColorDao;
import lk.crystal.asset.color.entity.ItemColor;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig(cacheNames = "itemColor")
public class ItemColorService implements AbstractService<ItemColor, Integer> {
    private final ItemColorDao itemColorDao;

    @Autowired
    public ItemColorService(ItemColorDao itemColorDao) {
        this.itemColorDao = itemColorDao;
    }
//read all
    @Override
    public List<ItemColor> findAll() {
        return itemColorDao.findAll();
    }
//read one
    @Override
    public ItemColor findById(Integer id) {
        return itemColorDao.getOne(id);
    }
    //create , update
    @Override
    public ItemColor persist(ItemColor itemColor) {
        return itemColorDao.save(itemColor);
    }
//delete
    @Override
    public boolean delete(Integer id) {
        itemColorDao.deleteById(id);
        //not applicable
        return false;
    }
//search return-> list

    @Override
    public List<ItemColor> search(ItemColor itemColor) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example<ItemColor> brandExample = Example.of(itemColor, matcher);
        return itemColorDao.findAll(brandExample);
    }
}
