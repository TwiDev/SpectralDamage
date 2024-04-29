package ch.twidev.spectraldamage.config;

public class ConfigValue {

    private final Object value;

    public ConfigValue(Object value) {
        this.value = value;
    }

    public int asInt() {
        try {
            return (int) Math.ceil(Double.parseDouble(value.toString()));
        } catch (ClassCastException e) {
            e.printStackTrace();
            return -1;
        }
    }

    public double asDouble() {
        try {
            return (double) value;
        } catch (ClassCastException e) {
            return asInt();
        }
    }

    public String asString() {
        try {
            return (String) value;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return null;
        }
    }

    public boolean asBoolean() {
        try {
            return (Boolean) value;
        } catch (ClassCastException e) {
            e.printStackTrace();
            return false;
        }
    }

}
