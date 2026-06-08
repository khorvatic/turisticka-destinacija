package hr.algebra.turistika.util;

import hr.algebra.turistika.model.Destinacija;
import hr.algebra.turistika.xml.DestinacijaXml;
import hr.algebra.turistika.xml.DestinacijeXml;
import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.Marshaller;

import java.io.File;
import java.util.List;

public class XmlExporter {
    public static void export(List<DestinacijaXml> destinacije, String path){
        try {
            File file = new File(path);
            file.getParentFile().mkdirs();

            JAXBContext context = JAXBContext.newInstance(DestinacijeXml.class, DestinacijaXml.class);
            Marshaller marshaller = context.createMarshaller();
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");

            marshaller.marshal(new DestinacijeXml(destinacije), file);
        } catch (Exception e) {
            throw new RuntimeException("Greska pri XML exportu", e);
        }
    }
}
