package ch.twidev.spectral.utils;

import org.bukkit.Bukkit;


public class VersionUtil {

    public static final String VERSION;

    public static final VersionUtil.VersionEnum CLEAN_VERSION;

    static {
        String bpName = Bukkit.getServer().getClass().getPackage().getName();
        VERSION = bpName.substring(bpName.lastIndexOf(".") + 1);
        String clean = VERSION.substring(0, VERSION.length() - 3);
        CLEAN_VERSION = VersionUtil.VersionEnum.valueOf(clean.toUpperCase());
    }

    public static boolean isCompatible(VersionEnum ve) {
        return VERSION.toLowerCase().contains(ve.toString().toLowerCase());
    }

    public static boolean isAbove(VersionEnum ve) {
        return CLEAN_VERSION.getOrder() >= ve.getOrder();
    }

    public static boolean isBelow(VersionEnum ve) {
        return CLEAN_VERSION.getOrder() <= ve.getOrder();
    }

    public static boolean isBetween(VersionEnum ve1, VersionEnum ve2) {
        return isAbove(ve1) && isBelow(ve2);
    }


    public enum VersionEnum {

        V1_8(1),
        V1_9(2),
        V1_10(3),
        V1_11(4),
        V1_12(5),
        V1_13(6),
        V1_14(7),
        V1_15(8),
        V1_16(9),
        V1_17(10),
        V1_18(11),
        V1_19(12);

        private final int order;

        VersionEnum(int order) {
            this.order = order;
        }

        public int getOrder() {
            return order;
        }

    }

}
