package api.mentalotus.Controller;
import api.mentalotus.Service.WeatherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.ParseException;


@RestController
public class HomeController {
    @Autowired
    private WeatherService weatherService;

    @GetMapping(path = "/weather")
    public String get_Weather(@RequestParam("nx") Double nx, @RequestParam("ny") Double ny) throws ParseException
    {
        try{
            String res = weatherService.get_Weather(nx, ny);
            return res;
        } catch(Exception e) {
            e.printStackTrace();
        }

        return null;
    }

    @GetMapping(path = "/home")
    public String home()
    {
        return "Finally";
    }
}
