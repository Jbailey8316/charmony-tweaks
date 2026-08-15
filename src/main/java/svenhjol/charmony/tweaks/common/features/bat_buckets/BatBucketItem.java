package svenhjol.charmony.tweaks.common.features.bat_buckets;

import net.minecraft.resources.ResourceKey;
import net.minecraft.sounds.SoundSource;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public final class BatBucketItem extends Item {
    private final BatBuckets feature;

    public BatBucketItem(BatBuckets feature, ResourceKey<Item> key) {
        super(new Item.Properties().stacksTo(1).setId(key));
        this.feature = feature;
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        var stack = player.getItemInHand(hand);
        var stored = Handlers.storedBat(stack);
        if (stored == null || stored.isEmpty()) return InteractionResult.PASS;
        // A released bat is a new entity.  Do not reuse the captured entity's
        // world position, motion, or UUID from its serialized state.
        stored.remove("UUID");
        stored.remove("Pos");
        stored.remove("Motion");
        stored.remove("Rotation");

        var direction = player.getDirection();
        var pos = player.blockPosition();
        var spawn = new Vec3(
            pos.getX() + 0.5d + direction.getStepX(),
            pos.getY() + 0.65d + player.getRandom().nextFloat() / 2.0d + direction.getStepY(),
            pos.getZ() + 0.5d + direction.getStepZ());

        if (!level.isClientSide()) {
            var entity = net.minecraft.world.entity.EntityType.loadEntityRecursive(
                stored, level, EntitySpawnReason.SPAWN_ITEM_USE, loaded -> {
                loaded.setPos(spawn);
                loaded.setDeltaMovement(Vec3.ZERO);
                return loaded;
            });
            if (!(entity instanceof Bat bat)) return InteractionResult.PASS;

            level.addFreshEntity(bat);
            level.playSound(null, pos, feature.registers.releaseSound.get(), SoundSource.PLAYERS, 1.0f, 1.0f);
            if (feature.damageBat()) {
                bat.hurt(level.damageSources().generic(), 1.0f);
                if (!bat.isRemoved()) level.playSound(null, pos, SoundEvents.BAT_TAKEOFF, SoundSource.NEUTRAL, 1.0f, 1.0f);
            }
            bat.setPos(spawn);
            player.addEffect(new net.minecraft.world.effect.MobEffectInstance(
                feature.registers.echolocation.get(), feature.glowingTime() * 20));
            if (player instanceof net.minecraft.server.level.ServerPlayer serverPlayer) {
                feature.advancements.usedBatBucket(serverPlayer);
            }
            if (!player.getAbilities().instabuild) player.setItemInHand(hand, new ItemStack(Items.BUCKET));
        }
        player.swing(hand);
        return InteractionResult.SUCCESS;
    }
}
