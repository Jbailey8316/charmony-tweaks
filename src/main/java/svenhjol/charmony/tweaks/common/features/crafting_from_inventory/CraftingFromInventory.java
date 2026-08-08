package svenhjol.charmony.tweaks.common.features.crafting_from_inventory;

import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;

@FeatureDefinition(side = Side.Common, description = "Allows crafting if the player has a crafting table in their inventory.")
public final class CraftingFromInventory extends SidedFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    public CraftingFromInventory(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }

    public static CraftingFromInventory feature() {
        return Mod.getSidedFeature(CraftingFromInventory.class);
    }
}
