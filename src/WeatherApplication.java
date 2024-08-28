import java.util.*;

class WeatherDispatcher{
    private float prevPresure;

    public void setMeasurements(float parseFloat, float parseFloat1, float parseFloat2) {

    }

    public void remove(Display Display) {

    }

    public void register(Display Display) {

    }

}
abstract class Display{

}
class CurrentConditionsDisplay extends Display{

    public CurrentConditionsDisplay(WeatherDispatcher weatherDispatcher) {

    }
}
class ForecastDisplay extends Display{
    private String forcast;

    public ForecastDisplay(WeatherDispatcher weatherDispatcher) {

    }
}
public class WeatherApplication {

    public static void main(String[] args) {
        WeatherDispatcher weatherDispatcher = new WeatherDispatcher();

        CurrentConditionsDisplay currentConditions = new CurrentConditionsDisplay(weatherDispatcher);
        ForecastDisplay forecastDisplay = new ForecastDisplay(weatherDispatcher);

        Scanner scanner = new Scanner(System.in);
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[] parts = line.split("\\s+");
            weatherDispatcher.setMeasurements(Float.parseFloat(parts[0]), Float.parseFloat(parts[1]), Float.parseFloat(parts[2]));
            if(parts.length > 3) {
                int operation = Integer.parseInt(parts[3]);
                if(operation==1) {
                    weatherDispatcher.remove(forecastDisplay);
                }
                if(operation==2) {
                    weatherDispatcher.remove(currentConditions);
                }
                if(operation==3) {
                    weatherDispatcher.register(forecastDisplay);
                }
                if(operation==4) {
                    weatherDispatcher.register(currentConditions);
                }

            }
        }
    }
}