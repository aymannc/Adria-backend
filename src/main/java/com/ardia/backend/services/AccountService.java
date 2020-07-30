package com.ardia.backend.services;

import com.ardia.backend.entities.Abonne;

public interface AccountService {
    public Abonne saveUser(Abonne user);
    public Abonne findUserbyUsername(String username);
}
