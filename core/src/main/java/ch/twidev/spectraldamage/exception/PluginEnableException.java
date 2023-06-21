package ch.twidev.spectraldamage.exception;

import ch.twidev.spectraldamage.SpectralDamagePlugin;

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
