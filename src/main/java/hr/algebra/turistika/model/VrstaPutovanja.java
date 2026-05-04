package hr.algebra.turistika.model;

public enum VrstaPutovanja {
    AVANTURIZAM("Avanturizam"),
    KULTURNI("Kulturni turizam"),
    PLAZNI("Plažni odmor"),
    GASTRONOMSKI("Gastronomski turizam");

    private final String name;

    VrstaPutovanja(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return name;
    }
}
