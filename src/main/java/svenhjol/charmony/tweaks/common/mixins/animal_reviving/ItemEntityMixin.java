package svenhjol.charmony.tweaks.common.mixins.animal_reviving;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Items;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import svenhjol.charmony.tweaks.common.features.animal_reviving.AnimalReviving;

/** Protects only Name Tags carrying serialized Animal Reviving data. */
@Mixin(ItemEntity.class)
public abstract class ItemEntityMixin {
    @Inject(method = "hurtServer(Lnet/minecraft/server/level/ServerLevel;Lnet/minecraft/world/damagesource/DamageSource;F)Z",
        at = @At("HEAD"), cancellable = true)
    private void charm$protectRevivalTag(ServerLevel level, DamageSource source, float amount,
                                         CallbackInfoReturnable<Boolean> cir) {
        var itemEntity = (ItemEntity) (Object) this;
        if (isRevivalTag(itemEntity)) {
            cir.setReturnValue(true);
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void charm$preventRevivalTagVoidLoss(CallbackInfo ci) {
        var itemEntity = (ItemEntity) (Object) this;
        if (!itemEntity.level().isClientSide()
            && isRevivalTag(itemEntity)
            && itemEntity.getY() < itemEntity.level().getMinY()) {
            itemEntity.setPos(itemEntity.getX(), itemEntity.level().getMinY() + 1.0, itemEntity.getZ());
        }
    }

    private static boolean isRevivalTag(ItemEntity itemEntity) {
        var stack = itemEntity.getItem();
        return stack.is(Items.NAME_TAG) && stack.has(AnimalReviving.feature().registers.data);
    }
}
