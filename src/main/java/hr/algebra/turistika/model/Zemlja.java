package hr.algebra.turistika.model;

import java.util.Objects;

public class Zemlja implements Comparable<Zemlja>{
    private Long id;
    private String naziv;
    private String kodDrzave;

    public Zemlja(Long id, String naziv) {
        this.id = id;
        this.naziv = naziv;
    }

    public Zemlja(Long id, String naziv, String kodDrzave) {
        this.id = id;
        this.naziv = naziv;
        this.kodDrzave = kodDrzave;
    }

    public Long getId() { return id; }

    public String getNaziv() { return naziv; }

    public String getKodDrzave() { return kodDrzave; }


    public void setId(Long id) { this.id = id; }

    public void setNaziv(String naziv) { this.naziv = naziv; }

    public void setKodDrzave(String kodDrzave) { this.kodDrzave = kodDrzave; }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Zemlja zemlja)) return false;
        return Objects.equals(id, zemlja.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return naziv + " (" + kodDrzave + ")";
    }

    @Override
    public int compareTo(Zemlja o) {
        return this.naziv.compareTo(o.naziv);
    }
}
