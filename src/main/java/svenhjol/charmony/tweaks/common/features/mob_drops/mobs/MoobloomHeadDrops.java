package svenhjol.charmony.tweaks.common.features.mob_drops.mobs;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.ResolvableProfile;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.EnchantmentsHelper;
import svenhjol.charmony.tweaks.common.features.mob_drops.DropProvider;
import svenhjol.charmony.tweaks.common.features.mob_drops.MobDrops;

import java.nio.charset.StandardCharsets;
import java.util.Optional;
import java.util.UUID;

public class MoobloomHeadDrops extends Setup<MobDrops> implements DropProvider {
    static final double BASE_DROP_CHANCE = 0.01d;
    static final double LOOTING_BONUS = 0.001d;

    private static final ResourceLocation MOOBLOOM = ResourceLocation.fromNamespaceAndPath("charmony", "moobloom");
    private static final String TEXTURE = "eyJ0ZXh0dXJlcyI6eyJTS0lOIjp7InVybCI6Imh0dHA6Ly90ZXh0dXJlcy5taW5lY3JhZnQubmV0L3RleHR1cmUvYjc5ZDdmYzJhYTRjM2VmM2FjNGI4N2Q3MzUxYzM4ZTFjZWJmZGY3NWIwMDJiZDlmNTMwYzA2ODI3ZTUzYWVkNCJ9fX0=";
    private static final UUID PROFILE_ID = UUID.nameUUIDFromBytes("charmony:moobloom_head".getBytes(StandardCharsets.UTF_8));

    public MoobloomHeadDrops(MobDrops feature) {
        super(feature);
    }

    @Override
    public Optional<ItemStack> dropWhenKilled(LivingEntity entity, DamageSource source) {
        if (!BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).equals(MOOBLOOM)
            || !(source.getEntity() instanceof ServerPlayer)
        ) {
            return Optional.empty();
        }

        var chance = BASE_DROP_CHANCE + (EnchantmentsHelper.lootingLevel(source) * LOOTING_BONUS);
        if (entity.getRandom().nextDouble() >= chance) {
            return Optional.empty();
        }

        var profile = new GameProfile(PROFILE_ID, "Moobloom");
        profile.properties().put("textures", new Property("textures", TEXTURE));

        var head = new ItemStack(Items.PLAYER_HEAD);
        head.set(DataComponents.PROFILE, ResolvableProfile.createResolved(profile));
        head.set(DataComponents.CUSTOM_NAME, Component.literal("Moobloom Head"));
        return Optional.of(head);
    }
}
