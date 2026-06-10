package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.TuristickiVodic;

import java.util.List;
import java.util.Optional;

public interface TuristickiVodicRepository {
    List<TuristickiVodic> findAll();
    Optional<TuristickiVodic> findById(Long id);
    void save(TuristickiVodic turistickiVodic);
    void update(TuristickiVodic turistickiVodic);
    void delete(Long id);
}
