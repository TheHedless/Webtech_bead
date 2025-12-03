package csendes.david.ser.service.impl;

import csendes.david.ser.data.repo.UserRepo;
import csendes.david.ser.service.UserService;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepo repo;

    public UserServiceImpl(UserRepo repo) {
        this.repo = repo;
    }

    @Override
    public UserDetailsService getUserDetailsService() {
        return new UserDetailsService() {
            @Override
            public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
                return repo.findByUsername(username);
            }
        };
    }
}
