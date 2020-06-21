package even.xmlresolver;

import org.apache.ibatis.io.Resources;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;

public class XmlEntityResolver implements EntityResolver {


    private static final String MY_XML_DEFI = "mybatis-3-mapper.dtd";
    private static final String MY_XML_DTD = "even/xmlresolver/mybatis-3-mapper.dtd";

    /**
     *
     * @param publicId -//mybatis.org//DTD Mapper 3.0//EN
     * @param systemId file:///G:/ideaspace/xmlbuild/src/main/java/even/xmlresolver/mybatis-3-mapper.dtd
     * @return
     * @throws SAXException
     * @throws IOException
     */
    @Override
    public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
        try {
            if (systemId != null) {
                String lowerCaseSystemId = systemId.toLowerCase(Locale.ENGLISH);
                if (lowerCaseSystemId.contains(MY_XML_DEFI)) {
                    return getInputSource(MY_XML_DTD, publicId, systemId);
                }
            }
            return null;
        } catch (Exception e) {
            throw new SAXException(e.toString());
        }
    }

    public InputSource getInputSource(String path, String publicId, String systemId) {
        InputSource source = null;
        if (path != null) {
            try {
                ClassLoader[] classLoaders = new ClassLoader[]{
                        Thread.currentThread().getContextClassLoader(),
                        getClass().getClassLoader(),
                };
                InputStream in = this.getResourceAsStream(path, classLoaders);
                source = new InputSource(in);
                source.setPublicId(publicId);
                source.setSystemId(systemId);
            } catch (Exception e) {
            }
        }
        return source;
    }

    public InputStream getResourceAsStream(String resource, ClassLoader[] classLoader) {
        for (ClassLoader cl : classLoader) {
            if (null != cl) {
                InputStream returnValue = cl.getResourceAsStream(resource);
                if (null == returnValue) {
                    returnValue = cl.getResourceAsStream("/" + resource);
                }
                if (null != returnValue) {
                    return returnValue;
                }
            }
        }
        return null;
    }
}
