package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.User;

import java.util.Optional;

public interface UserRepository {
    Optional<User> findByUsername(String username);
    void save(User user);
}
