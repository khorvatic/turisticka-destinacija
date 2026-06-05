package hr.algebra.turistika.util;

import hr.algebra.turistika.model.User;

public class SessionManager {
    private static SessionManager instance;
    private User trenutniKorisnik;

    private SessionManager() {}

    public static synchronized SessionManager getInstance() {
        if (instance == null) instance = new SessionManager();
        return instance;
    }

    public void postavi(User korisnik){
        trenutniKorisnik = korisnik;
    }

    public User getTrenutniKorisnik(){
        return trenutniKorisnik;
    }

    public String getKorisnickoIme(){
        return trenutniKorisnik != null ? trenutniKorisnik.getUsername() : "nepoznato";
    }
}
