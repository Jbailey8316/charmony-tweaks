package svenhjol.charmony.tweaks.common.features.bat_buckets;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueOutput;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.EntityHitResult;
import svenhjol.charmony.core.base.Setup;

import javax.annotation.Nullable;

public class Handlers extends Setup<BatBuckets> {
    private static final String STORED_BAT = "StoredBat";

    public Handlers(BatBuckets feature) { super(feature); }

    public InteractionResult captureBat(Player player, Level level, InteractionHand hand, Entity entity,
                                        @Nullable EntityHitResult hitResult) {
        if (level.isClientSide() || !(entity instanceof Bat bat) || bat.getHealth() <= 0.0f) {
            return InteractionResult.PASS;
        }
        var held = player.getItemInHand(hand);
        if (!held.is(Items.BUCKET)) return InteractionResult.PASS;

        var bucket = new ItemStack(feature().registers.batBucketItem.get());
        var tag = new CompoundTag();
        try (var collector = new net.minecraft.util.ProblemReporter.ScopedCollector(
            bat.problemPath(), feature().log().getLogger())) {
            var output = TagValueOutput.createWithContext(collector, level.registryAccess());
            bat.save(output);
            tag.put(STORED_BAT, output.buildResult());
        }
        CustomData.set(DataComponents.CUSTOM_DATA, bucket, tag);

        if (held.getCount() == 1) {
            player.setItemInHand(hand, bucket);
        } else {
            held.shrink(1);
            player.getInventory().placeItemBackInInventory(bucket);
        }
        bat.discard();
        level.playSound(null, bat.blockPosition(), feature().registers.grabSound.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
        player.getCooldowns().addCooldown(bucket, 30);
        player.swing(hand);
        if (player instanceof ServerPlayer serverPlayer) feature().advancements.capturedBat(serverPlayer);
        return InteractionResult.SUCCESS;
    }

    public void playerTick(Player player) {
        if (player.level().isClientSide() || player.tickCount % 10 != 0
            || !player.hasEffect(feature().registers.echolocation.get())) return;

        var range = feature().glowingRange();
        var center = player.position();
        var box = new AABB(center.x, center.y, center.z, center.x, center.y, center.z)
            .inflate(range, range / 2.0d, range);
        var glowing = new MobEffectInstance(MobEffects.GLOWING, 15, 0, false, false, true);
        player.level().getEntitiesOfClass(LivingEntity.class, box, entity -> entity != player)
            .forEach(entity -> {
                if (entity.canBeAffected(glowing)) entity.addEffect(new MobEffectInstance(glowing));
            });
    }

    static CompoundTag storedBat(ItemStack stack) {
        var data = stack.get(DataComponents.CUSTOM_DATA);
        return data == null ? null : data.copyTag().getCompoundOrEmpty(STORED_BAT);
    }
}
