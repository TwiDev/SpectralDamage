package ch.twidev.spectral.packet;

import ch.twidev.spectral.SpectralDamage;
import ch.twidev.spectral.utils.VersionUtil;
import ch.twidev.spectraldamage.nms.common.IPackets;
import ch.twidev.spectraldamage.nms.common.UnsupportedVersionException;
import ch.twidev.spectraldamage.nms.v1_10_R1.PacketsV1_10_R1;
import ch.twidev.spectraldamage.nms.v1_11_R1.PacketsV1_11_R1;
import ch.twidev.spectraldamage.nms.v1_12_R1.PacketsV1_12_R1;
import ch.twidev.spectraldamage.nms.v1_13_R2.PacketsV1_13_R2;
import ch.twidev.spectraldamage.nms.v1_14_R1.PacketsV1_14_R1;
import ch.twidev.spectraldamage.nms.v1_15_R1.PacketsV1_15_R1;
import ch.twidev.spectraldamage.nms.v1_16_R3.PacketsV1_16_R3;
import ch.twidev.spectraldamage.nms.v1_17_R1.PacketsV1_17_R1;
import ch.twidev.spectraldamage.nms.v1_18_R2.PacketsV1_18_R2;
import ch.twidev.spectraldamage.nms.v1_19_R3.PacketsV1_19_R3;
import ch.twidev.spectraldamage.nms.v1_20_R1.PacketsV1_20_R1;
import ch.twidev.spectraldamage.nms.v1_8_R3.PacketsV1_8_R3;
import ch.twidev.spectraldamage.nms.v1_9_R2.PacketsV1_9_R2;
import com.avaje.ebean.validation.NotNull;

public class PacketFactory {
    private static IPackets instance = null;

    static {
        if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {
            instance = new PacketsV1_8_R3();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_9)) {
            instance = new PacketsV1_9_R2();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_10)) {
            instance = new PacketsV1_10_R1();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_11)) {
            instance = new PacketsV1_11_R1();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_12)) {
            instance = new PacketsV1_12_R1();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_13)) {
            instance = new PacketsV1_13_R2();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_14)) {
            instance = new PacketsV1_14_R1();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_15)) {
            instance = new PacketsV1_15_R1();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_16)) {
            instance = new PacketsV1_16_R3();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_17)) {
            instance = new PacketsV1_17_R1();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_18)) {
            instance = new PacketsV1_18_R2();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_19)) {
            instance = new PacketsV1_19_R3();
        } else if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_20)) {
            instance = new PacketsV1_20_R1();
        }

        if(instance == null) {
            throw new UnsupportedVersionException();
        }else{
            SpectralDamage.log("Load Packet Factory Class " + instance.getVersionName());
        }
    }

    @NotNull
    public static IPackets get() {
        return instance;
    }

}
