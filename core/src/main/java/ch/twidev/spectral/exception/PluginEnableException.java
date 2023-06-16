package ch.twidev.spectral.exception;

import ch.twidev.spectral.SpectralDamage;

public class PluginEnableException extends RuntimeException{

    public PluginEnableException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        SpectralDamage.get().stop();

        super.printStackTrace();
    }
}
