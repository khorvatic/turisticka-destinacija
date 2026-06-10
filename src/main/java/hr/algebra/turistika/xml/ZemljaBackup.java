package hr.algebra.turistika.xml;

import hr.algebra.turistika.model.Zemlja;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class ZemljaBackup {
    @XmlElement private Long id;
    @XmlElement private String naziv;
    @XmlElement private String kodDrzave;

    public ZemljaBackup() {}

    public ZemljaBackup(Zemlja z) {
        this.id = z.getId();
        this.naziv = z.getNaziv();
        this.kodDrzave = z.getKodDrzave();
    }

    public Long getId() { return id; }
    public String getNaziv() { return naziv; }
    public String getKodDrzave() { return kodDrzave; }
}
