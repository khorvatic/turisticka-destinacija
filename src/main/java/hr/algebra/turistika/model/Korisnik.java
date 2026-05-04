package hr.algebra.turistika.model;

public class Korisnik extends User{
    public Korisnik(String username, String password) {
        super(username, password);
    }

    public Korisnik(Long id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getRole() {
        return "KORISNIK";
    }
}
