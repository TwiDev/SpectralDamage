package ch.twidev.spectraldamage.nms.common;

public class UnsupportedVersionException extends RuntimeException{

    public UnsupportedVersionException() {
        super("The version of your spigot instance is not supported by this plugin");
    }
}
