package hr.algebra.turistika.xml;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;
import jakarta.xml.bind.annotation.XmlRootElement;

import java.util.List;

@XmlRootElement(name = "destinacije")
@XmlAccessorType(XmlAccessType.FIELD)
public class DestinacijeXml {
    @XmlElement(name = "destinacija")
    private List<DestinacijaXml> destinacije;

    public DestinacijeXml() {}

    public DestinacijeXml(List<DestinacijaXml> destinacije) {
        this.destinacije = destinacije;
    }

}
