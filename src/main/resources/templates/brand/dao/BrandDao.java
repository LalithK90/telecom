package lk.scubes.phonesAndAccessories.asset.brand.dao;


import lk.scubes.phonesAndAccessories.asset.brand.entity.Brand;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BrandDao extends JpaRepository<Brand, Integer> {
}
