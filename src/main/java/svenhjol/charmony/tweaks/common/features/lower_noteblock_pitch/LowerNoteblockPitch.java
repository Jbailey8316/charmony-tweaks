package svenhjol.charmony.tweaks.common.features.lower_noteblock_pitch;

import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;

@FeatureDefinition(side = Side.Common, description = "Sneak-use note blocks to lower their note by one step.")
public final class LowerNoteblockPitch extends SidedFeature {
    public final Registers registers;
    public final Handlers handlers;

    public LowerNoteblockPitch(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
    }

    public static LowerNoteblockPitch feature() {
        return Mod.getSidedFeature(LowerNoteblockPitch.class);
    }
}
