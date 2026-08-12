package svenhjol.charmony.tweaks.common.features.crafting_from_inventory;

import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.inventory.CraftingMenu;
import net.minecraft.world.Container;

public final class Menu extends CraftingMenu {
    public Menu(int syncId, Inventory inventory, ContainerLevelAccess access) {
        super(syncId, inventory, access);
    }

    @Override
    public boolean stillValid(Player player) {
        return CraftingFromInventory.feature().handlers.hasCraftingTable(player);
    }

    @Override
    public void slotsChanged(Container container) {
        if (owner().level() instanceof ServerLevel serverLevel) {
            slotChangedCraftingGrid(this, serverLevel, owner(), craftSlots, resultSlots, null);
        }
    }
}
