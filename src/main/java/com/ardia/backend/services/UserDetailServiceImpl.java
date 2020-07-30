package com.ardia.backend.services;

import com.ardia.backend.entities.Abonne;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Collection;

@Service
public class UserDetailServiceImpl implements UserDetailsService {

    @Autowired
    private AccountService accountService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Abonne abonne = accountService.findUserbyUsername(username);
        if(abonne == null) throw  new UsernameNotFoundException(username);
        Collection<GrantedAuthority> roles = new ArrayList<GrantedAuthority>();
        return new User(abonne.getUsername(),abonne.getPassword(),roles);
    }
}
