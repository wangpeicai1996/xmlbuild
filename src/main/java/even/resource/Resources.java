package even.resource;

import java.io.InputStream;

public class Resources {

    public InputStream getInputStream(String path,ClassLoader[] classLoaders){
        for (ClassLoader classLoader : classLoaders) {
            if (null != classLoader) {
                InputStream returnValue = classLoader.getResourceAsStream(path);
                if (null == returnValue) {
                    returnValue = classLoader.getResourceAsStream("/" + path);
                }
                if (null != returnValue) {
                    return returnValue;
                }
            }
        }
        return null;
    }


}
