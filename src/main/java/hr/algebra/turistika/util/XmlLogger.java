package hr.algebra.turistika.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import java.io.File;
import java.time.format.DateTimeFormatter;

public class XmlLogger {
    private static final String LOG_PATH = "logs/log.xml";
    private static final DateTimeFormatter FORMATTER =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public static void log(String korisnik, String akcija, String opis){
        try {
            File logFile = new File(LOG_PATH);
            logFile.getParentFile().mkdirs();

            Document doc;
            Element root;

            if (logFile.exists()) {
                DocumentBuilder builder = DocumentBuilderFactory.
                        newInstance().newDocumentBuilder();
                doc = builder.parse(logFile);
                root = doc.getDocumentElement();
            } else {
                DocumentBuilder builder = DocumentBuilderFactory
                        .newInstance().newDocumentBuilder();
                doc = builder.newDocument();
                root = doc.createElement("log");
                doc.appendChild(root);
            }

            Element entry = doc.createElement("entry");

            Element timestamp = doc.createElement("timestamp");
            timestamp.setTextContent(new LogEntry(korisnik, akcija, opis)
                    .getTimestamp().format(FORMATTER));
            entry.appendChild(timestamp);

            Element korisnikE1 = doc.createElement("korisnik");
            korisnikE1.setTextContent(korisnik);
            entry.appendChild(korisnikE1);

            Element akcijaE1 = doc.createElement("akcija");
            akcijaE1.setTextContent(akcija);
            entry.appendChild(akcijaE1);

            Element opisE1 = doc.createElement("opis");
            opisE1.setTextContent(opis);
            entry.appendChild(opisE1);

            root.appendChild(entry);

            Transformer transformer = TransformerFactory.newInstance()
                    .newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(
                    "{http://xml.apache.org/xslt}indent-amount", "4");
            transformer.transform(new DOMSource(doc), new StreamResult(logFile));
        } catch (Exception e) {
            throw new RuntimeException("Greska pri zapisivanju loga", e);
        }
    }
}
