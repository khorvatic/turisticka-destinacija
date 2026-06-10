package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.Aktivnost;
import hr.algebra.turistika.model.Zemlja;

import java.util.List;
import java.util.Optional;

public interface AktivnostRepository {
    List<Aktivnost> findAll();
    Optional<Aktivnost> findById(Long id);
    void save(Aktivnost aktivnost);
    void update(Aktivnost aktivnost);
    void delete(Long id);
}
