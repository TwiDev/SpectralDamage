package ch.twidev.spectral.packet;

public class UnsupportedVersionException extends RuntimeException{

    public UnsupportedVersionException() {
        super("The version of your spigot instance is not supported by this plugin");
    }
}
