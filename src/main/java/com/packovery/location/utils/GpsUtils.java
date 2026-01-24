package com.packovery.location.utils;

public class GpsUtils {
    public static double calculateDistance(double latitude1, double longitude1, double latitude2, double longitude2) {
        if ((latitude1 == latitude2) && (longitude1 == longitude2)) {
            return 0;
        } else {
            double difference = longitude1 - longitude2;

            //Calcola l'angolo tra i due punti rispetto al centro della Terra
            double distance = Math.sin(Math.toRadians(latitude1)) * Math.sin(Math.toRadians(latitude2)) +
                    Math.cos(Math.toRadians(latitude1)) * Math.cos(Math.toRadians(latitude2)) * Math.cos(difference);

            //Trasforma il risultato matematico astratto in un angolo geometrico sulla sfera terrestre
            distance = Math.acos(distance);

            //Converte il risultato in gradi decimali
            distance = Math.toDegrees(distance);

            //Converte da Gradi a Miglia
            distance = distance * 60 * 1.1515;

            //Converte da Miglia a kilometri
            distance = distance * 1.609344;
            return distance;
        }
    }
}