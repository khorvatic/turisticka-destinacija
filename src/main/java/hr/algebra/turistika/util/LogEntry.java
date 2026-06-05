package hr.algebra.turistika.util;

import java.time.LocalDateTime;

public class LogEntry {
    private LocalDateTime timestamp;
    private String korisnik;
    private String akcija;
    private String opis;

    public LogEntry(String korisnik, String akcija, String opis) {
        this.timestamp = LocalDateTime.now();
        this.korisnik = korisnik;
        this.akcija = akcija;
        this.opis = opis;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public String getKorisnik() {
        return korisnik;
    }

    public String getAkcija() {
        return akcija;
    }

    public String getOpis() {
        return opis;
    }
}
