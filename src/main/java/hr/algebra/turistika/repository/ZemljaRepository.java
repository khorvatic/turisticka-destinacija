package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.Zemlja;

import java.util.List;
import java.util.Optional;

public interface ZemljaRepository {
    List<Zemlja> findAll();
    Optional<Zemlja> findById(Long id);
    void save(Zemlja zemlja);
    void update(Zemlja zemlja);
    void delete(Long id);
}
