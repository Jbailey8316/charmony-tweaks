package svenhjol.charmony.tweaks.client.features.crafting_from_inventory;

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.screen.v1.ScreenEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;
import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.tweaks.TweaksMod;
import svenhjol.charmony.tweaks.common.features.crafting_from_inventory.CraftingFromInventory;
import svenhjol.charmony.tweaks.common.features.crafting_from_inventory.Networking;

@FeatureDefinition(side = Side.Client, description = "Client controls for Crafting From Inventory.")
public final class CraftingFromInventoryClient extends SidedFeature {
    private final KeyMapping openPortableCrafting;

    public CraftingFromInventoryClient(Mod mod) {
        super(mod);
        openPortableCrafting = KeyBindingHelper.registerKeyBinding(new KeyMapping(
            "key.charmony.open_portable_crafting", GLFW.GLFW_KEY_V, KeyMapping.Category.INVENTORY));
    }

    @Override
    public java.util.function.BooleanSupplier check() {
        return () -> Mod.tryGetSidedFeature(CraftingFromInventory.class).map(SidedFeature::enabled).orElse(false);
    }

    @Override
    public void run() {
        ClientTickEvents.END_CLIENT_TICK.register(this::clientTick);
        ScreenEvents.AFTER_INIT.register((client, screen, width, height) -> {
            if (screen instanceof InventoryScreen inventory) {
                var left = (width - 176) / 2;
                var top = (height - 166) / 2;
                var button = Button.builder(Component.translatable("key.charmony.open_portable_crafting"),
                    ignored -> Networking.C2SOpenPortableCrafting.send())
                    .bounds(left + 127, top + 4, 20, 20).build();
                button.visible = hasTable(client);
                screen.addRenderableWidget(button);
            }
        });
    }

    private void clientTick(Minecraft minecraft) {
        while (openPortableCrafting.consumeClick()) {
            if (minecraft.level != null && hasTable(minecraft)) {
                Networking.C2SOpenPortableCrafting.send();
            }
        }
    }

    private boolean hasTable(Minecraft minecraft) {
        var player = minecraft.player;
        return player != null && CraftingFromInventory.feature().handlers.hasCraftingTable(player);
    }
}
