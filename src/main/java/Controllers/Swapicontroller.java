package Controllers;
import InM.Pojos.StarCharacter;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.util.TextUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@RestController
public class Swapicontroller {

    OkHttpClient client = new OkHttpClient();
    List<StarCharacter> chars ;
    List<StarCharacter> allchars =new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();
    public static String next="";
    public static String previous="";
    public String swapurl="https://swapi.co/api/people/";

    private static final Logger LOGGER = Logger.getLogger(Swapicontroller.class.getName());
    @GetMapping("/characters")
    public String GetCharacters()
    {
        return  callApi(swapurl);
    }
    @GetMapping("/getnext")
    public String GetNext()
    {
        if(next==null)
        {
            return "{}";
        }
        return callApi(next);
    }

    @GetMapping("/getprevious")
    public String GetPrevious()
    {   if(previous==null)
    {
        next=swapurl;
        return "{}";
    }
        return callApi(previous);
    }


    public String callApi(String requrl)
    {
        String resp="{}";
        allchars =new ArrayList<>();

        LOGGER.info("Calling swapi API");
        try {

            HttpUrl.Builder urlBuilder = HttpUrl.parse(requrl).newBuilder();

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject tempJson= new JSONObject(response.body().string());
            try{next=tempJson.get("next")==null?null:tempJson.getString("next").toString();}
            catch(Exception e){}
            try {
                previous = tempJson.get("previous") == null ? null : tempJson.getString("previous").toString();
            }
            catch(Exception e)
            {}
            LOGGER.info("Temp "+tempJson.getJSONArray("results").toString());

            TypeToken<List<StarCharacter>> token = new TypeToken<List<StarCharacter>>(){};
            Gson gson = new Gson();
            chars = gson.fromJson(tempJson.getJSONArray("results").toString(), token.getType());

            //chars = mapper.readValue(tempJson.getJSONArray("results").getJSONObject(0).toString(), new TypeReference<Collection<StarCharacter>>(){});


            for(StarCharacter newchar : chars)
            {
                if(MainController.checkFavorites(newchar.getUrl())>0)
                {
                    newchar.setFavourite("1");
                }
                else
                    newchar.setFavourite("0");
                allchars.add(newchar);
            }

            LOGGER.info("Characters >> "+mapper.writeValueAsString(allchars));
            resp=mapper.writeValueAsString(allchars);
            // LOGGER.info(chars.toString());
        }
        catch(Exception e)
        {e.printStackTrace();
            LOGGER.info(e.getMessage());
        }

        return allchars==null?"{}":resp ;

    }

    @GetMapping("/checkfavorite")
    public String CheckFavoriteByUrl(@RequestParam(name = "url") String url) {
        return ""+MainController.checkFavorites(url);
    }

    @GetMapping("/characters/{id}")
    public String GetCharactersByID(@RequestParam String id)
    {
        StarCharacter chars= new StarCharacter();
        LOGGER.info("Calling swapi API");
        try {

            HttpUrl.Builder urlBuilder = HttpUrl.parse("https://swapi.co/api/people/"+id).newBuilder();

            String url = urlBuilder.build().toString();

            Request request = new Request.Builder()
                    .url(url)
                    .build();
            Response response = client.newCall(request).execute();
            JSONObject tempJson= new JSONObject(response.body().string());
            JSONObject tempchars=tempJson.getJSONArray("results").getJSONObject(0);
            Gson gson = new Gson();
              chars = mapper.readValue(tempchars.toString(), new TypeReference<List<StarCharacter>>(){});

            // chars = gson.fromJson(tempchars.toString(), StarCharacter.class);
            LOGGER.info(chars.toString());
        }
        catch(Exception e)
        {e.printStackTrace();
            LOGGER.info(e.getMessage());
        }

        return chars.toString();

    }
}
