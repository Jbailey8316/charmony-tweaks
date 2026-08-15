package svenhjol.charmony.tweaks.common.features.lower_noteblock_pitch;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import svenhjol.charmony.core.base.Setup;

public class Registers extends Setup<LowerNoteblockPitch> {
    public Registers(LowerNoteblockPitch feature) {
        super(feature);
    }

    @Override
    public Runnable boot() {
        return () -> UseBlockCallback.EVENT.register(feature().handlers::useBlock);
    }
}
