package ru.pasvitas.eshop.service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import ru.pasvitas.eshop.exceptions.UserAlreadyCreatedException;
import ru.pasvitas.eshop.model.User;
import ru.pasvitas.eshop.model.UserRole;
import ru.pasvitas.eshop.repository.UserRepository;

@RequiredArgsConstructor
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Optional<User> userOptional = userRepository.getUserByUsername(username);
        if (userOptional.isPresent()) {
            return userOptional.get();
        }
        else {
            throw new UsernameNotFoundException(username);
        }
    }

    @Override
    public void createUser(String username, String password, String email) throws UserAlreadyCreatedException {
        Optional<User> userOptional = userRepository.getUserByUsername(username);
        if (userOptional.isEmpty()) {
            User user = User.builder()
                    .username(username)
                    .password(password)
                    .email(email)
                    .roles(List.of(UserRole.USER.getDbName()))
                    .cartProductIds(Map.of())
                    .orders(List.of())
                    .build();
            userRepository.save(user);
        }
        else {
            throw new UserAlreadyCreatedException(username);
        }
    }
}
