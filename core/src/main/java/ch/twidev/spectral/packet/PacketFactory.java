package ch.twidev.spectral.packet;

import ch.twidev.spectral.SpectralDamage;
import com.avaje.ebean.validation.NotNull;

public class PacketFactory {
    private static final IPackets instance;

    static {
        instance = new PacketsV1_8_R3();
        /*if (VersionUtil.isCompatible(VersionUtil.VersionEnum.V1_8)) {

        } else if (VersionUtil.isBetween(VersionUtil.VersionEnum.V1_9, VersionUtil.VersionEnum.V1_18)) {
            instance = null;
        } else {
            instance = null;
        }*/

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
