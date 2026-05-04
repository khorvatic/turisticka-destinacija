package hr.algebra.turistika.model;

public class Admin extends User{
    public Admin(String username, String password) {
        super(username, password);
    }

    public Admin(Long id, String username, String password) {
        super(id, username, password);
    }

    @Override
    public String getRole() {
        return "ADMIN";
    }
}
