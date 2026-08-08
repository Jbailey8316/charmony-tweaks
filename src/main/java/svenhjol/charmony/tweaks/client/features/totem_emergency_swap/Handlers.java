package svenhjol.charmony.tweaks.client.features.totem_emergency_swap;

import net.minecraft.client.Minecraft;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.inventory.ClickType;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.Items;
import svenhjol.charmony.core.base.Setup;

public class Handlers extends Setup<TotemEmergencySwap> {
    private int swapTicks;

    public Handlers(TotemEmergencySwap feature) {
        super(feature);
    }

    /**
     * Callback from mixin to prevent inventory screen from being rendered.
     * @see svenhjol.charmony.tweaks.client.mixins.totem_emergency_swap.InventoryScreenMixin
     */
    public boolean preventInventoryRendering() {
        return swapTicks > 0;
    }

    /**
     * Callback from mixin to prevent interruption of current keybinds.
     * @see svenhjol.charmony.tweaks.client.mixins.totem_emergency_swap.KeyMappingMixin
     */
    public boolean preventKeybindReleasing() {
        return swapTicks > 0;
    }

    public void clientTick(Minecraft minecraft) {
        if (swapTicks > 0) {
            if (swapTicks++ < 2) {
                findAndSwap(minecraft);
            } else {
                swapTicks = 0;
                minecraft.setScreen(null);
            }
        }

        while (feature().registers.swapTotemKey.consumeClick()) {
            requestSwapTotem(minecraft);
        }
    }

    public void requestSwapTotem(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null) return;

        if (player.getItemInHand(InteractionHand.MAIN_HAND).is(Items.TOTEM_OF_UNDYING)
            || player.getItemInHand(InteractionHand.OFF_HAND).is(Items.TOTEM_OF_UNDYING)) {
            // Already a totem in mainhand or offhand, ignore.
            return;
        }

        findAndSwap(minecraft);
    }

    public void findAndSwap(Minecraft minecraft) {
        var player = minecraft.player;
        if (player == null) return;

        var menu = player.inventoryMenu;

        var startIndex = 0;
        var endIndex = 44;
        var slots = menu.slots;
        if (slots.size() <= 45) return;
        var offhandSlot = slots.get(45);
        var itemInOffhand = offhandSlot.getItem();

        if (itemInOffhand.is(Items.TOTEM_OF_UNDYING)) {
            // Already a totem present in offhand.
            return;
        }

        for (var slot : slots) {
            if (canUseSlotIndex(slot, startIndex, endIndex)) {
                var stack = slot.getItem();
                if (stack.is(Items.TOTEM_OF_UNDYING)) {
                    var gameMode = minecraft.gameMode;
                    if (gameMode == null) return;

                    gameMode.handleInventoryMouseClick(menu.containerId, slot.index, 0, ClickType.PICKUP, player);
                    gameMode.handleInventoryMouseClick(menu.containerId, offhandSlot.index, 0, ClickType.PICKUP, player);
                    gameMode.handleInventoryMouseClick(menu.containerId, slot.index, 0, ClickType.PICKUP, player);
                    return;
                }
            }
        }
    }

    // TODO: move to helper for this and item tidying handler.
    private boolean canUseSlotIndex(Slot slot, int start, int end) {
        return (start == -1 || (start > -1 && slot.getContainerSlot() >= start))
            && (end == -1 || (end > -1 && slot.getContainerSlot() <= end));
    }
}
