package lk.crystal.asset.customer.dao;


import lk.crystal.asset.customer.entity.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CustomerDao extends JpaRepository< Customer, Integer> {
    Customer findFirstByOrderByIdDesc();
}
