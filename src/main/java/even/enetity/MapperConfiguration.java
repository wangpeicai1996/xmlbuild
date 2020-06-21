package even.enetity;

import java.util.HashMap;
import java.util.Map;

public class MapperConfiguration {

    public String namespace;
    public Map<String,Select> selectmap = new HashMap<>();

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

    public Map<String, Select> getSelectmap() {
        return selectmap;
    }

    public void setSelectmap(Map<String, Select> selectmap) {
        this.selectmap = selectmap;
    }
}
