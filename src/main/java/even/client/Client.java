package even.client;

import even.document.XmlParser;
import even.enetity.MapperConfiguration;
import even.enetity.Select;
import even.mapper.BlogMapper;
import even.resource.Resources;
import even.xmlresolver.XmlEntityResolver;
import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;


import java.io.InputStream;
import java.util.Map;
import java.util.jar.Attributes;

public class Client {
    public static void main(String[] args) throws Exception {

        InputStream inputStream = new Resources().getInputStream("even/mapper/blogMapper.xml",new ClassLoader[]{BlogMapper.class.getClassLoader()});
        if (inputStream != null) {
            MapperConfiguration mapperConfiguration = new MapperConfiguration();
            XmlParser parser = new XmlParser(mapperConfiguration,true,null,new XmlEntityResolver(),inputStream);
            parser.parse();
            Map<String, Select> selectMap = mapperConfiguration.getSelectmap();
            Select select = selectMap.get("selectById");
            Select select2 = selectMap.get("selectByName");
            System.out.println(select.getSql());
            System.out.println(select2.getSql());
        } else {
            throw new Exception("输入流为空");
        }
    }
}
