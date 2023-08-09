package api.Service;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import api.Network.Server;
import api.Utils.Global;
import api.Utils.YMLReader;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Naver OCR API Service (네이버 ocr api 호출)
 */


public class clovaService {
    public String processOCR(String format, String data, String name)
    {
        try {
            URL url = new URL(Global.YMLReader.getClovaConfig().getURL());
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setUseCaches(false);
            con.setDoInput(true);
            con.setDoOutput(true);
            con.setRequestMethod("POST");
            con.setRequestProperty("Content-Type", "application/json; charset=utf-8");
            con.setRequestProperty("X-OCR-SECRET", Global.YMLReader.getClovaConfig().getKEY());

            JSONObject json = new JSONObject();
            json.put("version", "V2");
            json.put("requestId", UUID.randomUUID().toString());
            json.put("timestamp", System.currentTimeMillis());
            JSONObject image = new JSONObject();
            image.put("format", format); //TODO 확장자 가져와야 됨
            image.put("data", data); //TODO 변수 적어야 됨
            image.put("name", name); //TODO Key값 넣어야 됨

            JSONArray images = new JSONArray();
            images.put(image);
            json.put("images", images);
            String postParams = json.toString();

            DataOutputStream wr = new DataOutputStream(con.getOutputStream());
            wr.writeBytes(postParams);
            wr.flush();
            wr.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) {
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else {
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }
            String inputLine;

            StringBuffer response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            String result = response.toString();
            return extractTextFromResponse(result);
        } catch (Exception e) {
            System.out.println(e.getCause());
            return e.toString();
        }
    }
    public String extractTextFromResponse(String response) {
        JSONObject jsonResponse = new JSONObject(response);
        JSONArray images = jsonResponse.getJSONArray("images");
        JSONObject image = images.getJSONObject(0);
        JSONArray fields = image.getJSONArray("fields");

        List<Coordinate> coordinateList = new ArrayList<>(); //좌표+텍스트 클래스 arraylist
        for (int i = 0; i < fields.length(); i++) {
            JSONObject field = fields.getJSONObject(i);
            String inferText = field.getString("inferText");

            JSONArray vertices = field.getJSONObject("boundingPoly").getJSONArray("vertices");
            JSONObject topLeft = vertices.getJSONObject(0);
            JSONObject bottomRight = vertices.getJSONObject(2); // 이걸 클래스로만들기
            double topX = topLeft.getDouble("x");
            double topY =  topLeft.getDouble("y");
            double bottomX = bottomRight.getDouble("x");
            double bottomY = bottomRight.getDouble("y");

            coordinateList.add(new Coordinate(topX,bottomX,topY,bottomY,inferText));

        }
        System.out.println(coordinateList.toString());
        return coordinateList.toString();
    }

    public static class Coordinate {
        double x1;
        double x2;
        double y1;
        double y2;
        String text;

        public Coordinate(double x1, double x2, double y1, double y2, String text) {
            this.x1 = x1;
            this.x2 = x2;
            this.y1 = y1;
            this.y2 = y2;
            this.text = text;
        }

        @Override
        public String toString(){
            return "("+ this.x1 +","+ this.x2+","+ this.y1+","+ this.y2+"," + ")"+this.text + "\n";
        }
    }
}

