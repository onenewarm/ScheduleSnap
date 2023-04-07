package api.mentalotus.Service;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import java.net.URI;
import java.net.URISyntaxException;
import java.time.LocalDateTime;



@Service
public class WeatherService {

    @Value("${weather.API_KEY}")
    private String API_KEY;
    private static final String BASE_URL = "http://apis.data.go.kr/1360000/VilageFcstInfoService_2.0/getUltraSrtFcst?serviceKey=%s&numOfRows=1000&pageNo=1&base_date=%s&base_time=%s&nx=%s&ny=%s&dataType=json";

    @Autowired
    private RestTemplate restTemplate;

    private LocalDateTime now = LocalDateTime.now();

    public String get_Weather(double x, double y) throws ParseException{
        String basedate;
        String basetime;

        String year = Integer.toString(now.getYear());  // 연도
        String month = Integer.toString(now.getMonthValue());  // 월(숫자)
        if(month.length() == 1) month = "0"+month;
        String dayofmonth = Integer.toString(now.getDayOfMonth());  // 일(월 기준)
        String hour = Integer.toString(now.getHour()-1);

        basedate = year+month+dayofmonth;
        if(hour.length()==1) basetime = "0" + hour + "00";
        else basetime = hour+"00";

        GpsTransfer trans = new GpsTransfer();
        LatXLngY pos  = new LatXLngY();
        pos = trans.convertGRID_GPS(0, x, y);

        String nx = Integer.toString((int)pos.x);
        String ny = Integer.toString((int)pos.y);

        String url = String.format(BASE_URL, API_KEY, basedate, basetime, nx, ny);

        System.out.println(url);

        URI uri = null;
        try{
            uri = new URI(url);
        }
        catch(URISyntaxException e){
            e.printStackTrace();
        }
        String jsonString = restTemplate.getForObject(uri, String.class);

        // Json parser를 만들어 만들어진 문자열 데이터를 객체화
        JSONParser parser = new JSONParser();
        JSONObject obj = (JSONObject) parser.parse(jsonString);
        // response 키를 가지고 데이터를 파싱
        JSONObject parse_response = (JSONObject) obj.get("response");
        // response 로 부터 body 찾기
        JSONObject parse_body = (JSONObject) parse_response.get("body");
        // body 로 부터 items 찾기
        JSONObject parse_items = (JSONObject) parse_body.get("items");

        // items로 부터 itemlist 를 받기
        JSONArray parse_item = (JSONArray) parse_items.get("item");
        String category;
        JSONObject weather; // parse_item은 배열형태이기 때문에 하나씩 데이터를 하나씩 가져올때 사


        // 카테고리와 값만 받아오기
        String day="";
        String time="";
        boolean PTY_flag = true;
        boolean SKY_flag = true;
        String PTY_value = "0";
        String SKY_value = "0";
        for(int i = 0 ; i < parse_item.size(); i++) {
            weather = (JSONObject) parse_item.get(i);
            String fcstValue = (weather.get("fcstValue").toString());

            category = (String)weather.get("category");
            // 출력
            if(category.equals("PTY")&& PTY_flag)
            {

                PTY_value = fcstValue;
                PTY_flag = false;
            }
            else if(category.equals("SKY")&& SKY_flag)
            {
                SKY_value = fcstValue;
                SKY_flag = false;
            }

            if(!PTY_flag && !SKY_flag) break;

        }

        if(PTY_value.equals("0"))
        {
            if(SKY_value.equals("1"))
            {
                jsonString = "맑음";
            } else if (SKY_value.equals("3") || SKY_value.equals("4")) {
                jsonString = "흐림";
            }
        } else if (PTY_value.equals("1") || PTY_value.equals("2")|| PTY_value.equals("5") || PTY_value.equals("6")) {
            jsonString = "비";
        } else{
            jsonString = "눈";
        }


        return jsonString;
    }


}

class GpsTransfer
{
    public static int TO_GRID = 0;
    public static int TO_GPS = 1;


    public LatXLngY convertGRID_GPS(int mode, double lat_X, double lng_Y )
    {
        double RE = 6371.00877; // 지구 반경(km)
        double GRID = 5.0; // 격자 간격(km)
        double SLAT1 = 30.0; // 투영 위도1(degree)
        double SLAT2 = 60.0; // 투영 위도2(degree)
        double OLON = 126.0; // 기준점 경도(degree)
        double OLAT = 38.0; // 기준점 위도(degree)
        double XO = 210/GRID; // 기준점 X좌표(GRID)
        double YO = 675/GRID; // 기1준점 Y좌표(GRID)

        //
        // LCC DFS 좌표변환 ( code : "TO_GRID"(위경도->좌표, lat_X:위도,  lng_Y:경도), "TO_GPS"(좌표->위경도,  lat_X:x, lng_Y:y) )
        //


        double DEGRAD = Math.PI / 180.0;
        double RADDEG = 180.0 / Math.PI;

        double re = RE / GRID;
        double slat1 = SLAT1 * DEGRAD;
        double slat2 = SLAT2 * DEGRAD;
        double olon = OLON * DEGRAD;
        double olat = OLAT * DEGRAD;

        double sn = Math.tan(Math.PI * 0.25 + slat2 * 0.5) / Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sn = Math.log(Math.cos(slat1) / Math.cos(slat2)) / Math.log(sn);
        double sf = Math.tan(Math.PI * 0.25 + slat1 * 0.5);
        sf = Math.pow(sf, sn) * Math.cos(slat1) / sn;
        double ro = Math.tan(Math.PI * 0.25 + olat * 0.5);
        ro = re * sf / Math.pow(ro, sn);
        LatXLngY rs = new LatXLngY();

        if (mode == TO_GRID) {
            rs.lat = lat_X;
            rs.lng = lng_Y;
            double ra = Math.tan(Math.PI * 0.25 + (lat_X) * DEGRAD * 0.5);
            ra = re * sf / Math.pow(ra, sn);
            double theta = lng_Y * DEGRAD - olon;
            if (theta > Math.PI) theta -= 2.0 * Math.PI;
            if (theta < -Math.PI) theta += 2.0 * Math.PI;
            theta *= sn;
            rs.x = Math.floor(ra * Math.sin(theta) + XO + 0.5);
            rs.y = Math.floor(ro - ra * Math.cos(theta) + YO + 0.5);
        }
        else {
            rs.x = lat_X;
            rs.y = lng_Y;
            double xn = lat_X - XO;
            double yn = ro - lng_Y + YO;
            double ra = Math.sqrt(xn * xn + yn * yn);
            if (sn < 0.0) {
                ra = -ra;
            }
            double alat = Math.pow((re * sf / ra), (1.0 / sn));
            alat = 2.0 * Math.atan(alat) - Math.PI * 0.5;

            double theta = 0.0;
            if (Math.abs(xn) <= 0.0) {
                theta = 0.0;
            }
            else {
                if (Math.abs(yn) <= 0.0) {
                    theta = Math.PI * 0.5;
                    if (xn < 0.0) {
                        theta = -theta;
                    }
                }
                else theta = Math.atan2(xn, yn);
            }
            double alon = theta / sn + olon;
            rs.lat = alat * RADDEG;
            rs.lng = alon * RADDEG;
        }
        return rs;
    }

}

class LatXLngY
{
    public double lat;
    public double lng;

    public double x;
    public double y;

}