package Controllers;

import InM.Beans.PostBean;
import InM.Pojos.CustomErrorType;
import InM.Pojos.StarCharacter;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.gson.Gson;
import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import org.json.JSONObject;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.logging.Logger;
import java.util.stream.Collectors;

@Controller
public class MainController {
    private static final Logger LOGGER = Logger.getLogger(MainController.class.getName());
    OkHttpClient client = new OkHttpClient();
   public static List<String> favorites =new ArrayList<>();
    ObjectMapper mapper = new ObjectMapper();


    @RequestMapping(value="/details",method = RequestMethod.GET)
    public String details( Model model){

            return "CharacterDetails";
    }






    @RequestMapping(value = "/updatefavorite", method = RequestMethod.POST)
    public ResponseEntity<?> updateFavorite(@RequestBody PostBean bean, UriComponentsBuilder ucBuilder) {
        HashMap<String, String> map = bean.getMap();
        String url = map.get("url").toString();
        String action = map.get("action").toString();
        LOGGER.info("" + url);

        if(action.equalsIgnoreCase("add")) {
            if (getFavorites().size() >= 5)
            {
                return new ResponseEntity(new CustomErrorType("You can only have a maximum of 5 Favourites."), HttpStatus.CONFLICT);
            }
         else if (checkFavorites(url) != 0) {

              return new ResponseEntity(new CustomErrorType("Unable to add , already exist."), HttpStatus.CONFLICT);
          } else
              addFavorites(url);
      }
      else
      {
          if (checkFavorites(url) == 0) {

              return new ResponseEntity(new CustomErrorType("Unable Remove as the Character is not in the favorite list "
                      ), HttpStatus.CONFLICT);
          } else
              removeFavorites(url);
      }



        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(ucBuilder.path("/details?url={url}").buildAndExpand(url).toUri());
        return new ResponseEntity<String>(headers, HttpStatus.OK);
    }

    public static List<String> getFavorites() {
        return favorites;
    }


    public static void addFavorites(String url) {
        favorites.add(url);
    }
    public static void removeFavorites(String url) {
        favorites.remove(url);
    }
    public static int checkFavorites(String url) {
        List<String> result = favorites.stream()                // convert list to stream
                .filter(line -> url.equals(line))
                .collect(Collectors.toList());
        return result.size();
    }
}
