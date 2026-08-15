package svenhjol.charmony.tweaks.common.features.beacons_heal_mobs;

import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;

@FeatureDefinition(side = Side.Common, description = "Passive and friendly mobs will heal themselves within range of a beacon with the regeneration effect.")
public final class BeaconsHealMobs extends SidedFeature {
    public final Handlers handlers;
    public final Registers registers;

    public BeaconsHealMobs(Mod mod) {
        super(mod);
        handlers = new Handlers(this);
        registers = new Registers(this);
    }

    public static BeaconsHealMobs feature() {
        return Mod.getSidedFeature(BeaconsHealMobs.class);
    }
}
