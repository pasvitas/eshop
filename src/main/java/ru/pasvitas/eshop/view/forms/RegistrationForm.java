package ru.pasvitas.eshop.view.forms;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.spring.annotation.SpringComponent;
import com.vaadin.flow.spring.annotation.UIScope;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import ru.pasvitas.eshop.exceptions.UserAlreadyCreatedException;
import ru.pasvitas.eshop.service.UserService;
import ru.pasvitas.eshop.view.bindermodel.RegistrationBinderModel;

public class RegistrationForm extends FormLayout {

    private final UserService userService;

    private final TextField name = new TextField("Логин");

    private final EmailField emailField = new EmailField("Электронная почта");

    private final PasswordField passwordField = new PasswordField("Пароль");

    private final Button button = new Button("Регистрация");

    private final PasswordField passwordAgainField = new PasswordField("Повторите пароль");

    private final Binder<RegistrationBinderModel> binder = new Binder<>(RegistrationBinderModel.class);

    public RegistrationForm(UserService userService) {
        this.userService = userService;

        binder.bind(name, RegistrationBinderModel::getUsername, RegistrationBinderModel::setUsername);
        binder.bind(emailField, RegistrationBinderModel::getEmail, RegistrationBinderModel::setEmail);
        binder.bind(passwordField, RegistrationBinderModel::getPassword, RegistrationBinderModel::setPassword);

        name.setRequired(true);
        emailField.setRequiredIndicatorVisible(true);
        passwordField.setRequired(true);
        passwordAgainField.setRequired(true);

        binder.setBean(new RegistrationBinderModel());

        button.addClickListener(event -> registration());
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        setResponsiveSteps(new ResponsiveStep("0", 1));

        add(name, emailField, passwordField, passwordAgainField, button);
    }

    private void registration() {
        String validationError = getValidationError();
        if (validationError != null) {
            Notification.show(validationError, 5000, Notification.Position.BOTTOM_END)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
        else {
            RegistrationBinderModel model = binder.getBean();
            try {
                userService.createUser(model.getUsername(), model.getPassword(), model.getEmail());
                Notification successNotification =  Notification.show("Успешно зарегистрировались!", 3000, Notification.Position.BOTTOM_END);
                successNotification.addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                successNotification.addDetachListener(event -> UI.getCurrent().navigate("/login"));
                button.setEnabled(false);
            } catch (UserAlreadyCreatedException e) {
                Notification.show("Пользователь с таким именем/email уже зарегистрирован!", 10000, Notification.Position.BOTTOM_END)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        }
    }

    private String getValidationError() {
        if (name.isEmpty()) {
            return "Введите имя пользователя";
        }
        if (emailField.isEmpty()) {
            return "Введите почту";
        }
        if (passwordField.isEmpty()) {
            return "Введите пароль";
        }
        if (passwordAgainField.isEmpty()) {
            return "Повторите пароль";
        }
        if (!passwordField.getValue().equals(passwordAgainField.getValue())) {
            return "Введенные пароли не совпадают!";
        }
        return null;
    }
}
