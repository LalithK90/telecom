package lk.crystal.asset.user_management.service;

import lk.crystal.asset.user_management.dao.RoleDao;
import lk.crystal.asset.user_management.entity.Role;
import lk.crystal.util.interfaces.AbstractService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.*;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@CacheConfig( cacheNames = {"role"} ) // tells Spring where to store cache for this class
public class RoleService implements AbstractService< Role, Integer > {
    private final RoleDao roleDao;

    @Autowired
    public RoleService(RoleDao roleDao) {
        this.roleDao = roleDao;
    }

    @Cacheable
    public List<Role> findAll() {
        return roleDao.findAll();
    }

    @Cacheable
    public Role findById(Integer id) {
        return roleDao.getOne(id);
    }


    @Caching( evict = {@CacheEvict( value = "role", allEntries = true )},
            put = {@CachePut( value = "role", key = "#role.id" )} )
    public Role persist(Role role) {
        role.setRoleName(role.getRoleName().toUpperCase());
        return roleDao.save(role);
    }

    @CacheEvict( allEntries = true )
    public boolean delete(Integer id) {
        roleDao.deleteById(id);
        return true;
    }

    @Cacheable
    public List<Role> search(Role role) {
        ExampleMatcher matcher = ExampleMatcher
                .matching()
                .withIgnoreCase()
                .withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING);
        Example< Role > roleExample = Example.of(role, matcher);
        return roleDao.findAll(roleExample);
    }

    @Cacheable
    public Role findByRoleName(String roleName) {
        return roleDao.findByRoleName(roleName);
    }
}
