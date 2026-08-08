package svenhjol.charmony.tweaks.common.features.mob_drops.mobs;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.EnchantmentsHelper;
import svenhjol.charmony.mooblooms.common.features.mooblooms.Moobloom;
import svenhjol.charmony.tweaks.common.features.mob_drops.DropProvider;
import svenhjol.charmony.tweaks.common.features.mob_drops.MobDrops;

import java.util.Optional;

public class MoobloomHeadDrops extends Setup<MobDrops> implements DropProvider {
    static final double BASE_DROP_CHANCE = 0.01d;
    static final double LOOTING_BONUS = 0.001d;

    public MoobloomHeadDrops(MobDrops feature) {
        super(feature);
    }

    @Override
    public Optional<ItemStack> dropWhenKilled(LivingEntity entity, DamageSource source) {
        if (!(entity instanceof Moobloom moobloom)
            || !(source.getEntity() instanceof ServerPlayer)
        ) {
            return Optional.empty();
        }

        var chance = BASE_DROP_CHANCE + (EnchantmentsHelper.lootingLevel(source) * LOOTING_BONUS);
        if (entity.getRandom().nextDouble() >= chance) {
            return Optional.empty();
        }

        var headId = ResourceLocation.fromNamespaceAndPath(
            "charmony", moobloom.getMoobloomType().getName() + "_moobloom_head");
        return Optional.of(new ItemStack(BuiltInRegistries.ITEM.getValue(headId)));
    }
}
