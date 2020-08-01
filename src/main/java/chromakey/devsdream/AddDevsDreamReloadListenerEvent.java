package chromakey.devsdream;

import chromakey.devsdream.util.DevsDreamDPR;
import net.minecraftforge.event.AddReloadListenerEvent;

public class AddDevsDreamReloadListenerEvent extends AddReloadListenerEvent {

    public AddDevsDreamReloadListenerEvent(DevsDreamDPR dataPackRegistries) {
        super(dataPackRegistries);
    }
}