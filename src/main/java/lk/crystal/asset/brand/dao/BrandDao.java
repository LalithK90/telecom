package lk.crystal.asset.brand.dao;

import lk.crystal.asset.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandDao extends JpaRepository<Brand, Integer> {
    Brand findByName(String nic);
}
