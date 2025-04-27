package net.adam.config;

import org.w3c.dom.Node;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import java.io.File;
import java.util.Map;

// Exemple de parser XML pour lire la configuration
public class XMLParser {
    public static Map<String, String> parseXML(String filePath) throws JAXBException {
        JAXBContext context = JAXBContext.newInstance(BeansConfig.class);
        return context.createUnmarshaller().unmarshal((Node) new File(filePath), BeansConfig.class).getValue().getBeans();
    }
}
