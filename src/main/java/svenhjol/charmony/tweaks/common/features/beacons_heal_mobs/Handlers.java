package svenhjol.charmony.tweaks.common.features.beacons_heal_mobs;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.AgeableMob;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import svenhjol.charmony.core.base.Setup;

import javax.annotation.Nullable;

public class Handlers extends Setup<BeaconsHealMobs> {
    private static final int EFFECT_DURATION = 80;
    private static final int EFFECT_AMPLIFIER = 1;

    public Handlers(BeaconsHealMobs feature) {
        super(feature);
    }

    public void applyBeaconEffects(Level level, BlockPos pos, int levels,
                                   @Nullable Holder<MobEffect> primaryEffect,
                                   @Nullable Holder<MobEffect> secondaryEffect) {
        if (!feature().enabled() || level.isClientSide()) return;
        if (!isRegeneration(primaryEffect) && !isRegeneration(secondaryEffect)) return;

        var range = levels * 10 + 10;
        var bounds = new AABB(pos).inflate(range).expandTowards(0.0D, level.getHeight(), 0.0D);
        var mobs = level.getEntitiesOfClass(AgeableMob.class, bounds);
        mobs.forEach(mob -> mob.addEffect(new MobEffectInstance(
            MobEffects.REGENERATION, EFFECT_DURATION, EFFECT_AMPLIFIER)));
    }

    private boolean isRegeneration(@Nullable Holder<MobEffect> effect) {
        return effect != null && effect.is(MobEffects.REGENERATION);
    }
}
