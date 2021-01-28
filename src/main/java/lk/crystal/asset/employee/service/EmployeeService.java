package lk.crystal.asset.employee.service;


import lk.crystal.asset.common_asset.model.enums.LiveDead;
import lk.crystal.asset.employee.dao.EmployeeDao;
import lk.crystal.asset.employee.entity.Employee;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
// spring transactional annotation need to tell spring to this method work through the project
@CacheConfig( cacheNames = "employee" )
public class EmployeeService implements AbstractService< Employee, Integer > {

    private final EmployeeDao employeeDao;

    @Autowired
    public EmployeeService(EmployeeDao employeeDao) {
        this.employeeDao = employeeDao;
    }

    @Cacheable
    public List< Employee > findAll() {
        return employeeDao.findAll().stream()
            .filter(x -> LiveDead.ACTIVE.equals(x.getLiveDead()))
            .collect(Collectors.toList());
    }

    @Cacheable
    public Employee findById(Integer id) {
        return employeeDao.getOne(id);
    }

    @Caching( evict = {@CacheEvict( value = "employee", allEntries = true )},
            put = {@CachePut( value = "employee", key = "#employee.id" )} )
    @Transactional
    public Employee persist(Employee employee) {
        if(employee.getId()==null){
            employee.setLiveDead(LiveDead.ACTIVE);}
        return employeeDao.save(employee);
    }

    public boolean delete(Integer id) {
        Employee employee =  employeeDao.getOne(id);
        employee.setLiveDead(LiveDead.STOP);
        employeeDao.save(employee);
        return false;
    }
   /* @CacheEvict( allEntries = true )
    public boolean delete(Integer id) {
        employeeDao.deleteById(id);
        return false;
    }
*/
    @Cacheable
    public List< Employee > search(Employee employee) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< Employee > employeeExample = Example.of(employee, matcher);
        return employeeDao.findAll(employeeExample);
    }

    public boolean isEmployeePresent(Employee employee) {
        return employeeDao.equals(employee);
    }


    public Employee lastEmployee() {
        return employeeDao.findFirstByOrderByIdDesc();
    }

    @Cacheable
    public Employee findByNic(String nic) {
        return employeeDao.findByNic(nic);
    }


}
