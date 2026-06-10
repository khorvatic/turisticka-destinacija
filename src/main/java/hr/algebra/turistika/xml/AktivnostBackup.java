package hr.algebra.turistika.xml;

import hr.algebra.turistika.model.Aktivnost;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;

@XmlAccessorType(XmlAccessType.FIELD)
public class AktivnostBackup {
    private Long id;
    private String naziv;
    private String opis;

    public AktivnostBackup() {}

    public AktivnostBackup(Aktivnost a) {
        this.id = a.getId();
        this.naziv = a.getNaziv();
        this.opis = a.getOpis();
    }

    public Long getId() { return id; }
    public String getNaziv() { return naziv; }
    public String getOpis() { return opis; }
}
