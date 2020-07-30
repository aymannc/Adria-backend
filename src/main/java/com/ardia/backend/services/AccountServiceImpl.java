package com.ardia.backend.services;

import com.ardia.backend.entities.Abonne;
import com.ardia.backend.repositories.AbonneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AccountServiceImpl implements AccountService{

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private AbonneRepository abonneRepository;

    @Override
    public Abonne saveUser(Abonne user) {
        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        return abonneRepository.save(user);
    }

    @Override
    public Abonne findUserbyUsername(String username) {
        return abonneRepository.findByUsername(username);
    }
}
