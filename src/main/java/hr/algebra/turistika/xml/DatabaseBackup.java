package hr.algebra.turistika.xml;

import hr.algebra.turistika.model.Zemlja;
import jakarta.xml.bind.annotation.*;

import javax.naming.Name;
import java.util.List;

@XmlRootElement(name = "backup")
@XmlAccessorType(XmlAccessType.FIELD)
public class DatabaseBackup {
    @XmlElementWrapper(name = "zemlje")
    @XmlElement(name = "zemlja")
    private List<ZemljaBackup> zemlje;

    @XmlElementWrapper(name = "aktivnosti")
    @XmlElement(name = "aktivnost")
    private List<AktivnostBackup> aktivnosti;

    @XmlElementWrapper(name = "vodici")
    @XmlElement(name = "vodic")
    private List<VodicBackup> vodici;

    @XmlElementWrapper(name = "destinacije")
    @XmlElement(name = "destinacija")
    private List<DestinacijaBackup> destinacije;

    public DatabaseBackup() {}

    public DatabaseBackup(List<ZemljaBackup> zemlje,
                          List<AktivnostBackup> aktivnosti,
                          List<VodicBackup> vodici,
                          List<DestinacijaBackup> destinacije) {
        this.zemlje = zemlje;
        this.aktivnosti = aktivnosti;
        this.vodici = vodici;
        this.destinacije = destinacije;
    }

    public List<ZemljaBackup> getZemlje() { return zemlje; }
    public List<AktivnostBackup> getAktivnosti() { return aktivnosti; }
    public List<VodicBackup> getVodici() { return vodici; }
    public List<DestinacijaBackup> getDestinacije() { return destinacije; }
}
