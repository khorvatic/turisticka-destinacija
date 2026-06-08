package hr.algebra.turistika.util;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.InputStream;

public class AppConfig {
    private static AppConfig instance;

    private int windowWidth;
    private int windowHeight;
    private String windowTitle;
    private String databaseUrl;
    private String databaseUsername;
    private String databasePassword;

    private AppConfig() {
        ucitajKonfiguraciju();
    }

    public static synchronized AppConfig getInstance() {
        if (instance == null) instance = new AppConfig();
        return instance;
    }

    private void ucitajKonfiguraciju() {
        try (InputStream is = getClass().getClassLoader()
                .getResourceAsStream("config.xml")) {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(is);
            doc.getDocumentElement().normalize();

            Element window = (Element) doc.getElementsByTagName("window").item(0);
            windowWidth = Integer.parseInt(getTagValue("width", window));
            windowHeight = Integer.parseInt(getTagValue("height", window));
            windowTitle = getTagValue("title", window);

            Element database = (Element) doc.getElementsByTagName("database").item(0);
            databaseUrl = getTagValue("url", database);
            databaseUsername = getTagValue("username", database);
            databasePassword = getTagValue("password", database);
        } catch(Exception e) {
            throw new RuntimeException("Greska pri ucitavanju konfiguracije", e);
        }
    }

    private String getTagValue(String tag, Element element) {
        return element.getElementsByTagName(tag).item(0).getTextContent();
    }

    public int getWindowWidth() {
        return windowWidth;
    }

    public int getWindowHeight() {
        return windowHeight;
    }

    public String getWindowTitle() {
        return windowTitle;
    }

    public String getDatabaseUrl() {
        return databaseUrl;
    }

    public String getDatabaseUsername() {
        return databaseUsername;
    }

    public String getDatabasePassword() {
        return databasePassword;
    }
}
