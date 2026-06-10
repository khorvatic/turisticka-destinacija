package hr.algebra.turistika.xml;

import hr.algebra.turistika.model.Destinacija;
import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlElementWrapper;

import java.util.List;

@XmlAccessorType(XmlAccessType.FIELD)
public class DestinacijaBackup {
    @XmlElement private Long id;
    @XmlElement private String naziv;
    @XmlElement private String opis;
    @XmlElement private Double latitude;
    @XmlElement private Double longitude;
    @XmlElement private String preprucenoGodisnjeDoba;
    @XmlElement private Long zemljaId;
    @XmlElement private String vrstaPutovanja;

    @XmlElementWrapper(name = "aktivnosti")
    @XmlElement(name = "aktivnost")
    private List<Long> aktivnostiIds;

    public DestinacijaBackup() {}

    public DestinacijaBackup(Destinacija d) {
        this.id = d.getId();
        this.naziv = d.getNaziv();
        this.opis = d.getOpis();
        this.latitude = d.getLatitude();
        this.longitude = d.getLongitude();
        this.preprucenoGodisnjeDoba = d.getPreporucenoGodisnjeDoba();
        this.zemljaId = d.getZemlja() != null ? d.getZemlja().getId() : null;
        this.vrstaPutovanja = d.getVrstaPutovanja().name();
        this.aktivnostiIds = d.getAktivnosti().stream().map(a -> a.getId()).toList();
    }

    public Long getId() { return id; }
    public String getNaziv() { return naziv; }
    public String getOpis() { return opis; }
    public Double getLatitude() { return latitude; }
    public Double getLongitude() { return longitude; }
    public String getPreprucenoGodisnjeDoba() { return preprucenoGodisnjeDoba; }
    public Long getZemljaId() { return zemljaId; }
    public String getVrstaPutovanja() { return vrstaPutovanja; }
    public List<Long> getAktivnostiIds() { return aktivnostiIds; }
}
