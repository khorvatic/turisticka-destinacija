package hr.algebra.turistika.repository;

import hr.algebra.turistika.model.Destinacija;
import hr.algebra.turistika.model.VrstaPutovanja;

import java.util.List;
import java.util.Optional;

public interface DestinacijaRepository {
    List<Destinacija> findAll();
    Optional<Destinacija> findById(Long id);
    void save(Destinacija destinacija);
    void update(Destinacija destinacija);
    void delete(Long id);

    List<Destinacija> findByZemlja(Long zemljaId);
    List<Destinacija> findByVrstaPutovanja(VrstaPutovanja vrsta);

    default boolean exists(Long id){
        return findById(id).isPresent();
    }
}
