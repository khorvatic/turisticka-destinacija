package hr.algebra.turistika.xml;

import jakarta.xml.bind.annotation.*;

import java.util.List;

@XmlRootElement(name = "destinacija")
@XmlAccessorType(XmlAccessType.FIELD)
public class DestinacijaXml {
    @XmlElement(name = "naziv")
    private String naziv;

    @XmlElement(name = "opis")
    private String opis;

    @XmlElement(name = "zemlja")
    private String zemlja;

    @XmlElement(name = "vrstaPutovanja")
    private String vrstaPutovanja;

    @XmlElement(name = "preporucenoGodisnjeDoba")
    private String preporucenoGodisnjeDoba;

    @XmlElement(name = "latitude")
    private Double latitude;

    @XmlElement(name = "longitude")
    private Double longitude;

    @XmlElementWrapper(name = "aktivnosti")
    @XmlElement(name = "aktivnost")
    private List<String> aktivnosti;


    @XmlElementWrapper(name = "vodici")
    @XmlElement(name = "vodic")
    private List<String> vodici;

    public DestinacijaXml() {}

    public DestinacijaXml(hr.algebra.turistika.model.Destinacija d) {
        this.naziv = d.getNaziv();
        this.opis = d.getOpis();
        this.zemlja = d.getZemlja() != null ? d.getZemlja().getNaziv() : "";
        this.vrstaPutovanja = d.getVrstaPutovanja().toString();
        this.preporucenoGodisnjeDoba = d.getPreporucenoGodisnjeDoba();
        this.latitude = d.getLatitude();
        this.longitude = d.getLongitude();
        this.aktivnosti = d.getAktivnosti().stream()
                .map(a -> a.getNaziv())
                .toList();
        this.vodici = d.getTuristickiVodici().stream()
                .map(v -> v.getFullName())
                .toList();
    }
}
