package ch.twidev.spectral.exception;

import ch.twidev.spectral.SpectralDamagePlugin;

public class PluginEnableException extends RuntimeException{

    public PluginEnableException(String message) {
        super(message);
    }

    @Override
    public void printStackTrace() {
        SpectralDamagePlugin.get().stop();

        super.printStackTrace();
    }
}
