package ru.pasvitas.eshop.service;

import ru.pasvitas.eshop.exceptions.UserAlreadyCreatedException;

public interface UserService {

    void createUser(String username, String password, String email) throws UserAlreadyCreatedException;
}
