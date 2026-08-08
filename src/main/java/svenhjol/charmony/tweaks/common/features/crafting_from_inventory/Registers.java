package svenhjol.charmony.tweaks.common.features.crafting_from_inventory;

import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;

public final class Registers extends Setup<CraftingFromInventory> {
    public Registers(CraftingFromInventory feature) {
        super(feature);
    }

    @Override
    public Runnable boot() {
        return () -> {
            var registry = CommonRegistry.forFeature(feature());
            registry.packetSender(Side.Client, Networking.C2SOpenPortableCrafting.TYPE,
                Networking.C2SOpenPortableCrafting.CODEC);
            registry.packetReceiver(Networking.C2SOpenPortableCrafting.TYPE,
                () -> feature().handlers::openPortableCraftingReceived);
        };
    }
}
