package lk.crystal.asset.item_color.service;

import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.employee.entity.Employee;
import lk.crystal.asset.item_color.dao.ItemColorDao;
import lk.crystal.asset.item_color.entity.ItemColor;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
        return itemColorDao.findAll().stream()
                .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
                .collect(Collectors.toList());
    }
//read one
    @Override
    public ItemColor findById(Integer id) {
        return itemColorDao.getOne(id);
    }

//create , update
    @Override
    public ItemColor persist(ItemColor itemColor) {
        if(itemColor.getId()==null){
            itemColor.setLiveDead(LiveDead.ACTIVE);}
        return itemColorDao.save(itemColor);

    }
//delete
    @Override
    public boolean delete(Integer id) {
        ItemColor itemColor =  itemColorDao.getOne(id);
        itemColor.setLiveDead(LiveDead.STOP);
        itemColorDao.save(itemColor);
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
