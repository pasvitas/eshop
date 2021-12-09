package ru.pasvitas.eshop.view.bindermodel;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class RegistrationBinderModel {

    private String username;

    private String email;

    private String password;

}
