package ch.twidev.spectraldamage.nms;

import ch.twidev.spectraldamage.nms.v1_20_R2.PacketsV1_20_R2;
import org.bukkit.Bukkit;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum NMSVersion {

    // Not using shorter method reference syntax here because it initializes the class, causing a ClassNotFoundException

    /* 1.8 - 1.8.2     */ v1_8_R1(NMSManagerFactory.outdatedVersion("1.8.4")),
    /* 1.8.3           */ v1_8_R2(NMSManagerFactory.outdatedVersion("1.8.4")),
    /* 1.8.4 - 1.8.9   */ v1_8_R3(() -> new ch.twidev.spectraldamage.nms.v1_8_R3.PacketsV1_8_R3()),
    /* 1.9 - 1.9.3     */ v1_9_R1(NMSManagerFactory.outdatedVersion("1.9.4")),
    /* 1.9.4           */ v1_9_R2(() -> new ch.twidev.spectraldamage.nms.v1_9_R2.PacketsV1_9_R2()),
    /* 1.10 - 1.10.2   */ v1_10_R1(() -> new ch.twidev.spectraldamage.nms.v1_10_R1.PacketsV1_10_R1()),
    /* 1.11 - 1.11.2   */ v1_11_R1(() -> new ch.twidev.spectraldamage.nms.v1_11_R1.PacketsV1_11_R1()),
    /* 1.12 - 1.12.2   */ v1_12_R1(() -> new ch.twidev.spectraldamage.nms.v1_12_R1.PacketsV1_12_R1()),
    /* 1.13            */ v1_13_R1(NMSManagerFactory.outdatedVersion("1.13.1")),
    /* 1.13.1 - 1.13.2 */ v1_13_R2(() -> new ch.twidev.spectraldamage.nms.v1_13_R2.PacketsV1_13_R2()),
    /* 1.14 - 1.14.4   */ v1_14_R1(() -> new ch.twidev.spectraldamage.nms.v1_14_R1.PacketsV1_14_R1()),
    /* 1.15 - 1.15.2   */ v1_15_R1(() -> new ch.twidev.spectraldamage.nms.v1_15_R1.PacketsV1_15_R1()),
    /* 1.16 - 1.16.1   */ v1_16_R1(NMSManagerFactory.outdatedVersion("1.16.4")),
    /* 1.16.2 - 1.16.3 */ v1_16_R2(NMSManagerFactory.outdatedVersion("1.16.4")),
    /* 1.16.4 - 1.16.5 */ v1_16_R3(() -> new ch.twidev.spectraldamage.nms.v1_16_R3.PacketsV1_16_R3()),
    /* 1.17            */ v1_17_R1(() -> new ch.twidev.spectraldamage.nms.v1_17_R1.PacketsV1_17_R1()),
    /* 1.18 - 1.18.1   */ v1_18_R1(() -> new ch.twidev.spectraldamage.nms.v1_18_R1.PacketsV1_18_R1()),
    /* 1.18.2          */ v1_18_R2(() -> new ch.twidev.spectraldamage.nms.v1_18_R2.PacketsV1_18_R2()),
    /* 1.19 - 1.19.2   */ v1_19_R1(() -> new ch.twidev.spectraldamage.nms.v1_19_R1.PacketsV1_19_R1()),
    /* 1.19.3          */ v1_19_R2(() -> new ch.twidev.spectraldamage.nms.v1_19_R2.PacketsV1_19_R2()),
    /* 1.19.4          */ v1_19_R3(() -> new ch.twidev.spectraldamage.nms.v1_19_R3.PacketsV1_19_R3()),
    /* 1.20 - 1.20.1   */ v1_20_R1(() -> new ch.twidev.spectraldamage.nms.v1_20_R1.PacketsV1_20_R1()),
    /* 1.20.2 - 1.20.3 */ v1_20_R2(() -> new ch.twidev.spectraldamage.nms.v1_20_R2.PacketsV1_20_R2()),
    /* 1.20.3 - ?      */ v1_20_R3(() -> new ch.twidev.spectraldamage.nms.v1_20_R3.PacketsV1_20_R3()),
    /* Other versions  */ UNKNOWN(NMSManagerFactory.unknownVersion());

    private static final NMSVersion CURRENT_VERSION;

    static {
        CURRENT_VERSION = detectCurrentVersion();
    }

    final NMSManagerFactory iPackets;

    NMSVersion(NMSManagerFactory iPackets) {
        this.iPackets = iPackets;
    }

    public static NMSVersion getCurrentVersion() {
        return CURRENT_VERSION;
    }

    public NMSManagerFactory getManagerFactory() {
        return iPackets;
    }

    private static NMSVersion detectCurrentVersion() {
        Matcher matcher = Pattern.compile("v\\d+_\\d+_R\\d+").matcher(Bukkit.getServer().getClass().getPackage().getName());
        if (!matcher.find()) {
            return UNKNOWN;
        }

        String nmsVersionName = matcher.group();
        try {
            return valueOf(nmsVersionName);
        } catch (IllegalArgumentException e) {
            return UNKNOWN;
        }
    }
}
