package hr.algebra.turistika.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class VodicBackup {
    @XmlElement private Long id;
    @XmlElement private String ime;
    @XmlElement private String prezime;
    @XmlElement private String kontakt;

    public VodicBackup() {}

    public VodicBackup(hr.algebra.turistika.model.TuristickiVodic v) {
        this.id = v.getId();
        this.ime = v.getIme();
        this.prezime = v.getPrezime();
        this.kontakt = v.getKontakt();
    }

    public Long getId() { return id; }
    public String getIme() { return ime; }
    public String getPrezime() { return prezime; }
    public String getKontakt() { return kontakt; }
}
