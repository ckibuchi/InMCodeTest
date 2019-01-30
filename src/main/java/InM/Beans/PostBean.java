package InM.Beans;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.annotation.JsonAnySetter;
/**
 * @author: chris Kibuchi
 * This Pojo will receive json Data from api postings.
 * Example if you want to add a new character, you will be required to just pass the data and its not a must you match with the Class StarCharacter
 */
import java.util.HashMap;

public class PostBean {
    private HashMap<String, String> map;

    @JsonAnyGetter
    public HashMap<String, String> getMap() {
        return map;
    }

    @JsonAnySetter
    public void setMap(String name, String value) {
        if (this.map == null) map = new  HashMap<String, String>();;
        this.map.put(name, value);
    }
}