package ru.pasvitas.eshop.repository;

import java.util.Optional;
import org.springframework.data.mongodb.repository.MongoRepository;
import ru.pasvitas.eshop.model.User;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> getUserByUsername(String username);
}
