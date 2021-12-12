package ru.pasvitas.eshop.service;

import ru.pasvitas.eshop.exceptions.UserAlreadyCreatedException;
import ru.pasvitas.eshop.model.User;

public interface UserService {

    void createUser(String username, String password, String email) throws UserAlreadyCreatedException;
}
