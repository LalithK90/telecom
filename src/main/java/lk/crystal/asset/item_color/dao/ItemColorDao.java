package lk.crystal.asset.item_color.dao;

import lk.crystal.asset.item_color.entity.ItemColor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemColorDao extends JpaRepository<ItemColor, Integer> {

}
