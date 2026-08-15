package svenhjol.charmony.tweaks.client.features.mob_textures.custom_renderers;

import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.WanderingTraderRenderer;
import net.minecraft.client.renderer.entity.state.VillagerRenderState;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.npc.WanderingTrader;

import javax.annotation.Nullable;
public class CustomWanderingTraderRenderer extends WanderingTraderRenderer implements CustomRenderer {
    public CustomWanderingTraderRenderer(EntityRendererProvider.Context context) {
        super(context);
        handlers.fillLayersFromOld(context, this, EntityType.WANDERING_TRADER);
    }

    @Override
    @Nullable
    public ResourceLocation getTextureLocation(VillagerRenderState villagerRenderState) {
        var state = (CustomVillagerRenderState) villagerRenderState;
        return handlers.texture(state.uuid, registers.wanderingTraders);
    }

    @Override
    public VillagerRenderState createRenderState() {
        return new CustomVillagerRenderState();
    }

    @Override
    public void extractRenderState(WanderingTrader wanderingTrader, VillagerRenderState villagerRenderState, float f) {
        super.extractRenderState(wanderingTrader, villagerRenderState, f);
        ((CustomVillagerRenderState) villagerRenderState).uuid = wanderingTrader.getUUID();
    }
}
