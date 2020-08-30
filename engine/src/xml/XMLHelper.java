package xml;

import xml.generated.SuperDuperMarketDescriptor;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;

public class XMLHelper {

    private final static String JAXB_XML_GAME_PACKAGE_NAME = "xml.generated";



    //Blocks the  option of creating an object of XMLHelper
    private XMLHelper() {
    }

    public static SuperDuperMarketDescriptor FromXmlFileToObject(String filePath) throws FileNotFoundException, JAXBException {
       // try {
            InputStream inputStream = new FileInputStream(new File(filePath));
            return deserializeFrom(inputStream);
            //sdmSystem.loadSystem(superDuperMarketDescriptor);
//        } catch (JAXBException | FileNotFoundException e) {
//            e.printStackTrace();
//        }
    }


    private static SuperDuperMarketDescriptor deserializeFrom(InputStream in) throws JAXBException {
        JAXBContext jc = JAXBContext.newInstance(JAXB_XML_GAME_PACKAGE_NAME);
        Unmarshaller u = jc.createUnmarshaller();
        return (SuperDuperMarketDescriptor) u.unmarshal(in);
    }


}

