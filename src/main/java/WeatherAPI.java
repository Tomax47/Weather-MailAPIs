import org.json.JSONArray;
import org.json.JSONObject;

import java.io.*;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Scanner;

public class WeatherAPI {
    private static final String API_KEY = "Key";

    public static void main(String[] args) {
        Scanner scan = new Scanner(System.in);
        System.out.print("Enter the city : ");
        String city = scan.nextLine();
        System.out.println(getWeatherInfo(city));

    }

    public static String getWeatherInfo(String city) {
        String weatherDescription = null;
        double temperature =0,humidity =0;
        try {

            String apiUrl = "http://api.openweathermap.org/data/2.5/weather?q=" + city + "&appid=" + API_KEY;


            URL url = new URL(apiUrl);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setRequestMethod("GET");

            int responseCode = connection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                StringBuilder response = new StringBuilder();
                String line;
                while ((line = reader.readLine()) != null) {
                    response.append(line);
                }
                reader.close();


                JSONObject jsonResponse = new JSONObject(response.toString());
                JSONArray weatherArray = jsonResponse.getJSONArray("weather");
                JSONObject weatherObject = weatherArray.getJSONObject(0);
                weatherDescription = weatherObject.getString("description");

                JSONObject mainObject = jsonResponse.getJSONObject("main");
                temperature = mainObject.getDouble("temp");
                humidity = mainObject.getDouble("humidity");

                retrievedWeatherData(city,weatherDescription,temperature,humidity);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return "Weather in : "+city+"\nDescription : "+weatherDescription+"\nTemp : "+(temperature - 273.15)+" C"+"\nHumidity : "+humidity+"%";
    }

    public static void retrievedWeatherData(String city, String weatherDescription, double temperature, double humidity) throws IOException {
        JSONObject weatherData = new JSONObject();
        weatherData.put("city", city);
        weatherData.put("description", weatherDescription);
        weatherData.put("temperature", temperature - 273.15);
        weatherData.put("humidity", humidity);

        String filename = city + "_weather.json";
        BufferedWriter writer = new BufferedWriter(new FileWriter(filename));
        writer.write(weatherData.toString());
        writer.close();

    }
}
