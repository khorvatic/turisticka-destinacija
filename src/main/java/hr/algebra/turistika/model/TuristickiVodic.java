package hr.algebra.turistika.model;

import java.util.Objects;

public class TuristickiVodic implements Comparable<TuristickiVodic>{
    private Long id;
    private String ime;
    private String prezime;
    private String kontakt;

    public TuristickiVodic(String ime, String prezime, String kontakt) {
        this.ime = ime;
        this.prezime = prezime;
        this.kontakt = kontakt;
    }

    public TuristickiVodic(Long id, String ime, String prezime, String kontakt) {
        this.id = id;
        this.ime = ime;
        this.prezime = prezime;
        this.kontakt = kontakt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIme() {
        return ime;
    }

    public void setIme(String ime) {
        this.ime = ime;
    }

    public String getPrezime() {
        return prezime;
    }

    public void setPrezime(String prezime) {
        this.prezime = prezime;
    }

    public String getKontakt() {
        return kontakt;
    }

    public void setKontakt(String kontakt) {
        this.kontakt = kontakt;
    }

    public String getFullName() {
        return ime + " " + prezime;
    }

    @Override
    public String toString() {
        return ime + " " + prezime;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TuristickiVodic tv)) return false;
        return Objects.equals(id, tv.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public int compareTo(TuristickiVodic o) {
        return this.getFullName().compareTo(o.getFullName());
    }
}
