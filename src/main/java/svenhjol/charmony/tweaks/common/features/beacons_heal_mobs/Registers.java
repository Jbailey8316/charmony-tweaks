package svenhjol.charmony.tweaks.common.features.beacons_heal_mobs;

import svenhjol.charmony.api.events.ApplyBeaconEffectsCallback;
import svenhjol.charmony.core.base.Setup;

public class Registers extends Setup<BeaconsHealMobs> {
    public Registers(BeaconsHealMobs feature) {
        super(feature);
    }

    @Override
    public Runnable boot() {
        return () -> ApplyBeaconEffectsCallback.EVENT.register(feature().handlers::applyBeaconEffects);
    }
}
