package hr.algebra.turistika.service;

import hr.algebra.turistika.model.Destinacija;
import hr.algebra.turistika.model.VrstaPutovanja;
import hr.algebra.turistika.model.Zemlja;
import hr.algebra.turistika.repository.DestinacijaRepository;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class DestinacijaService {
    private final DestinacijaRepository destinacijaRepository;

    public DestinacijaService(DestinacijaRepository destinacijaRepository) {
        this.destinacijaRepository = destinacijaRepository;
    }

    // Dohvaca sve destinacije soritrane po nazivu
    public List<Destinacija> findAllSorted() {
        return destinacijaRepository.findAll().stream().
                sorted(Comparator.comparing(Destinacija::getNaziv))
                .collect(Collectors.toList());
    }

    // Filtrira destinacije kombiniranim predikatom
    public List<Destinacija> filter(String pojam, Zemlja zemlja, VrstaPutovanja vrsta) {
        Predicate<Destinacija> filter = d -> true;
        if(pojam != null && !pojam.isBlank()) {
            filter = filter.and(d -> d.getNaziv().toLowerCase().contains(pojam.toLowerCase()));
        }
        if(zemlja != null) {
            filter = filter.and(d -> d.getZemlja() != null && d.getZemlja().equals(zemlja));
        }
        if(vrsta != null) {
            filter = filter.and(d -> d.getVrstaPutovanja().equals(vrsta));
        }

        return destinacijaRepository.findAll().stream()
                .filter(filter)
                .sorted(Comparator.comparing(Destinacija::getNaziv))
                .collect(Collectors.toList());
    }

    public Optional<Destinacija> findById(Long id) {
        return destinacijaRepository.findAll().stream()
                .filter(d -> d.getId().equals(id))
                .findFirst();
    }

    public List<String> findDistinctZemlje() {
        return destinacijaRepository.findAll().stream()
                .map(Destinacija::getZemlja)
                .map(Zemlja::getNaziv)
                .distinct()
                .sorted()
                .collect(Collectors.toList());
    }

    public void forEach(Consumer<Destinacija> action) {
        destinacijaRepository.findAll().forEach(action);
    }

    public <T> List<T> map(Function<Destinacija, T> mapper) {
        return destinacijaRepository.findAll().stream()
                .map(mapper)
                .collect(Collectors.toList());
    }
}
