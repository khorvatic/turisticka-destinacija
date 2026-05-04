package hr.algebra.turistika.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Destinacija implements Comparable<Destinacija>{
    private Long id;
    private String naziv;
    private String opis;
    private Double latitude;
    private Double longitude;
    private String preporucenoGodisnjeDoba;
    private String putanjaFotografije;
    private Zemlja zemlja;
    private VrstaPutovanja vrstaPutovanja;
    private List<Aktivnost> aktivnosti;
    private List<TuristickiVodic> turistickiVodici;

    public Destinacija(String naziv, String opis, Double latitude, Double longitude, String preporucenoGodisnjeDoba, String putanjaFotografije, Zemlja zemlja, VrstaPutovanja vrstaPutovanja) {
        this.naziv = naziv;
        this.opis = opis;
        this.latitude = latitude;
        this.longitude = longitude;
        this.preporucenoGodisnjeDoba = preporucenoGodisnjeDoba;
        this.putanjaFotografije = putanjaFotografije;
        this.zemlja = zemlja;
        this.vrstaPutovanja = vrstaPutovanja;
        this.aktivnosti = new ArrayList<>();
        this.turistickiVodici = new ArrayList<>();
    }

    public Destinacija(Long id, String naziv, String opis, Double latitude, Double longitude, String preporucenoGodisnjeDoba, String putanjaFotografije, Zemlja zemlja, VrstaPutovanja vrstaPutovanja) {
        this.id = id;
        this.naziv = naziv;
        this.opis = opis;
        this.latitude = latitude;
        this.longitude = longitude;
        this.preporucenoGodisnjeDoba = preporucenoGodisnjeDoba;
        this.putanjaFotografije = putanjaFotografije;
        this.zemlja = zemlja;
        this.vrstaPutovanja = vrstaPutovanja;
        this.aktivnosti = new ArrayList<>();
        this.turistickiVodici = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNaziv() {
        return naziv;
    }

    public void setNaziv(String naziv) {
        this.naziv = naziv;
    }

    public String getOpis() {
        return opis;
    }

    public void setOpis(String opis) {
        this.opis = opis;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getPreporucenoGodisnjeDoba() {
        return preporucenoGodisnjeDoba;
    }

    public void setPreporucenoGodisnjeDoba(String preporucenoGodisnjeDoba) {
        this.preporucenoGodisnjeDoba = preporucenoGodisnjeDoba;
    }

    public String getPutanjaFotografije() {
        return putanjaFotografije;
    }

    public void setPutanjaFotografije(String putanjaFotografije) {
        this.putanjaFotografije = putanjaFotografije;
    }

    public Zemlja getZemlja() {
        return zemlja;
    }

    public void setZemlja(Zemlja zemlja) {
        this.zemlja = zemlja;
    }

    public VrstaPutovanja getVrstaPutovanja() {
        return vrstaPutovanja;
    }

    public void setVrstaPutovanja(VrstaPutovanja vrstaPutovanja) {
        this.vrstaPutovanja = vrstaPutovanja;
    }

    public List<Aktivnost> getAktivnosti() {
        return aktivnosti;
    }
    public void setAktivnosti(List<Aktivnost> aktivnosti) {
        this.aktivnosti = aktivnosti;
    }
    public void addAktivnost(Aktivnost aktivnost) {
        this.aktivnosti.add(aktivnost);
    }
    public void removeAktivnost(Aktivnost aktivnost) {
        this.aktivnosti.remove(aktivnost);
    }


    public List<TuristickiVodic> getTuristickiVodici() {
        return turistickiVodici;
    }
    public void setTuristickiVodici(List<TuristickiVodic> turistickiVodici) {
        this.turistickiVodici = turistickiVodici;
    }
    public void addTuristickiVodic(TuristickiVodic turistickiVodic) {
        this.turistickiVodici.add(turistickiVodic);
    }
    public void removeTuristickiVodic(TuristickiVodic turistickiVodic) {
        this.turistickiVodici.remove(turistickiVodic);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Destinacija destinacija)) return false;
        return Objects.equals(id, destinacija.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return naziv;
    }

    @Override
    public int compareTo(Destinacija o) {
        return this.naziv.compareTo(o.naziv);
    }
}
