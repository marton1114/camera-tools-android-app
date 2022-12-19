package com.example.phototools.exposureapp;


import java.util.Arrays;
import java.util.List;

public class CameraUtils {
    private static List<String> shutters = Arrays.asList("1/8000", "1/4000", "1/2000",
            "1/1000", "1/500", "1/250", "1/125", "1/60", "1/30", "1/15",
            "1/8", "1/4", "1/2", "1", "2", "4", "8", "15", "30", "60");
    private static List<String> isos = Arrays.asList("100", "200", "400", "800", "1600",
            "3200", "6400", "12800");
    private static List<String> apertures = Arrays.asList("1", "1.4", "2", "2.8", "4",
            "5.6", "8", "11", "16", "22");

    public static float shutterStringToFloat(String shutterValue) {
        float result = 0;
        if (shutterValue.contains("/")) {
            result = 1 / Float.parseFloat(shutterValue.substring(2, shutterValue.length()));
        } else {
            result = Float.parseFloat(shutterValue);
        }

        return result;
    }

    public static String closestShutter(float shutter) {
        int mini = shutters.size() - 1;
        for (int i = 0; i < shutters.size(); i++) {
            float difference = Math.abs(shutterStringToFloat(shutters.get(i)) - shutter);
            if (difference < Math.abs(shutterStringToFloat(shutters.get(mini)) - shutter)) {
                mini = i;
            }
        }

        return shutters.get(mini);
    }

    public static String closestIso(int iso) {
        int mini = isos.size() - 1;
        for (int i = 0; i < isos.size(); i++) {
            int difference = Math.abs(Integer.parseInt(isos.get(i)) - iso);
            if (difference < Math.abs(Integer.parseInt(isos.get(mini)) - iso)) {
                mini = i;
            }
        }

        return isos.get(mini);
    }

    public static String closestAperture(float aperture) {
        int mini = apertures.size() - 1;
        for (int i = 0; i < apertures.size(); i++) {
            float difference = Math.abs(Float.parseFloat(apertures.get(i)) - aperture);
            if (difference < Math.abs(Float.parseFloat(apertures.get(mini)) - aperture)) {
                mini = i;
            }
        }

        return apertures.get(mini);
    }

    public static int luxToEv(float lux) {
        // return (int) ((lux / 1428.57) - 6);  // 0-20000, -6-22
        // return (int) ((lux / 3.57) - 6);     // 0-100, -6-22
        return (int) ((lux / (activity_exposure.getmLight().getMaximumRange() / 28)) - 6);
    }

    public static float calculateShutter(float lux, float aperture, int iso) {
        int ev = luxToEv(lux);

        return (float) ((100 * Math.pow(aperture, 2)) / (Math.pow(2, ev) * iso));
    }

    public static int calculateIso(float lux, float shutter, float aperture) {
        int ev = luxToEv(lux);

        return (int) ((100 * Math.pow(aperture, 2)) / (Math.pow(2, ev) * shutter));
    }

    public static float calculateAperture(float lux, float shutter, float iso) {
        int ev = luxToEv(lux);

        return (float) Math.sqrt((Math.pow(2, ev) * iso * shutter) / 100);
    }

    public static List<String> getShutters() {
        return shutters;
    }

    public static List<String> getIsos() {
        return isos;
    }

    public static List<String> getApertures() {
        return apertures;
    }
}