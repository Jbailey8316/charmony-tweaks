package svenhjol.charmony.tweaks.common.features.crafting_from_inventory;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleMenuProvider;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerLevelAccess;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.tweaks.common.features.crafting_from_inventory.Networking.C2SOpenPortableCrafting;

public final class Handlers extends Setup<CraftingFromInventory> {
    public static final Component LABEL = Component.translatable("container.charmony.portable_crafting_table");

    public Handlers(CraftingFromInventory feature) {
        super(feature);
    }

    public boolean hasCraftingTable(Player player) {
        if (player == null) return false;
        if (player.getMainHandItem().is(Tags.CRAFTING_TABLES)
            || player.getOffhandItem().is(Tags.CRAFTING_TABLES)) {
            return true;
        }
        for (ItemStack stack : player.getInventory().items) {
            if (stack.is(Tags.CRAFTING_TABLES)) return true;
        }
        return false;
    }

    public void openPortableCraftingReceived(Player player, C2SOpenPortableCrafting ignored) {
        if (!(player instanceof ServerPlayer serverPlayer) || !hasCraftingTable(serverPlayer)) return;
        feature().advancements.usedCraftingTable(serverPlayer);
        serverPlayer.closeContainer();
        serverPlayer.openMenu(new SimpleMenuProvider((id, inventory, owner) ->
            new Menu(id, inventory, ContainerLevelAccess.NULL), LABEL));
    }
}
