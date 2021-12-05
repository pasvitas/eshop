package ru.pasvitas.eshop.exceptions;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.atmosphere.config.service.Get;

@RequiredArgsConstructor
@Getter
public class UserAlreadyCreatedException extends Exception {
    private final String userName;
}
