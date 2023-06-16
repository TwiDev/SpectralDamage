package ch.twidev.spectral.utils;

public class LocationUtils {

    public static byte getShortPoint(double coordinate){
        return getShortPoint(coordinate, 0);
    }

    public static byte getShortPoint(double coordinate, double adder){
        return new Double((coordinate + adder) * 32 - coordinate * 32).byteValue();
    }

}
