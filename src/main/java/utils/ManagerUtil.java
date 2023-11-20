package utils;

import java.util.Random;

public class ManagerUtil {
    public static String generateRandomToken (int length) {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789";
        StringBuilder token = new StringBuilder();

        Random random = new Random();
        for (int i = 0; i < length; i++) {
            int index = random.nextInt(characters.length());
            token.append(characters.charAt(index));
        }

        return token.toString();
    }

    public static String getSensorNameById (String sensorId) {
        String sensorName = "";

        // Use a switch case to match the sensorId and assign the corresponding sensorName
        switch (sensorId) {
            case "V0":
                sensorName = "Gas Sensor";
                break;
            case "V1":
                sensorName = "Voltage Sensor";
                break;
            case "V2":
                sensorName = "Ampere Sensor";
                break;
            case "V3":
                sensorName = "Temperature Sensor";
                break;
            case "V4":
                sensorName = "Humidity Sensor";
                break;
            case "V5":
                sensorName = "Water Sensor";
                break;
            case "V6":
                sensorName = "Fire Sensor 1";
                break;
            case "V7":
                sensorName = "Fire Sensor 2";
                break;
            case "V8":
                sensorName = "Fire Sensor 3";
                break;
            case "V9":
                sensorName = "Fire Sensor 4";
                break;
            case "V10":
                sensorName = "Fire Sensor 5";
                break;
            case "V11":
                sensorName = "AC Switch 1";
                break;
            case "V12":
                sensorName = "AC Switch 2";
                break;
            case "V13":
                sensorName = "Total Power Consumption";
                break;
            case "V14":
                sensorName = "Total Water Consumption";
                break;
            default:
                sensorName = "Invalid Sensor ID";
        }

        return sensorName;
    }
}
