package svenhjol.charmony.tweaks.common.features.crafting_from_inventory;

import net.minecraft.world.entity.player.Player;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.AdvancementHelper;

public final class Advancements extends Setup<CraftingFromInventory> {
    public Advancements(CraftingFromInventory feature) {
        super(feature);
    }

    public void usedCraftingTable(Player player) {
        AdvancementHelper.trigger("used_portable_crafting_table", player);
    }
}
