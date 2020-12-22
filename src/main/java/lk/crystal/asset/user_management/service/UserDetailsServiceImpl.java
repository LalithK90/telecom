package lk.crystal.asset.user_management.service;

import lk.crystal.asset.user_management.CustomerUserDetails;
import lk.crystal.asset.user_management.user.dao.UserDao;
import lk.crystal.asset.user_management.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {
    @Autowired
    private UserDao userDao;


    public UserDetailsServiceImpl() {
    }

    @Transactional( readOnly = true )
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        User user = userDao.findByUsername(username.toLowerCase());
        CustomerUserDetails userDetails;
        if ( user != null ) {
            userDetails = new CustomerUserDetails();
            userDetails.setUser(user);
        } else {
            throw new UsernameNotFoundException("User not exist with name : " + username);
        }
        return userDetails;
    }
}

