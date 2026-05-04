package hr.algebra.turistika.model;

import java.util.Objects;

public class Aktivnost implements Comparable<Aktivnost>{
    private Long id;
    private String naziv;
    private String opis;

    public Aktivnost(String naziv, String opis) {
        this.naziv = naziv;
        this.opis = opis;
    }

    public Aktivnost(Long id, String naziv, String opis) {
        this.id = id;
        this.naziv = naziv;
        this.opis = opis;
    }

    public Long getId() {
        return id;
    }

    public String getNaziv() {
        return naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Aktivnost aktivnost)) return false;
        return Objects.equals(id, aktivnost.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Aktivnost: " + naziv;
    }

    @Override
    public int compareTo(Aktivnost o) {
        return this.naziv.compareTo(o.naziv);
    }
}
