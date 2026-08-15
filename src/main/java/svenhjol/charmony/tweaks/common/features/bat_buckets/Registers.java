package svenhjol.charmony.tweaks.common.features.bat_buckets;

import net.fabricmc.fabric.api.event.player.UseEntityCallback;
import net.minecraft.core.Holder;
import net.minecraft.core.Registry;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.world.effect.MobEffect;
import svenhjol.charmony.core.base.Registerable;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.common.CommonRegistry;
import svenhjol.charmony.core.common.GenericTrades;

public class Registers extends Setup<BatBuckets> {
    public final Registerable<BatBucketItem> batBucketItem;
    public final Registerable<SoundEvent> grabSound;
    public final Registerable<SoundEvent> releaseSound;
    public final Registerable<Holder<MobEffect>> echolocation;

    public Registers(BatBuckets feature) {
        super(feature);
        var registry = CommonRegistry.forFeature(feature);
        batBucketItem = registry.item("bat_bucket", key -> new BatBucketItem(feature, key));
        grabSound = registry.sound("bat_bucket_grab");
        releaseSound = registry.sound("bat_bucket_release");
        echolocation = new Registerable<>(feature, () -> Registry.registerForHolder(
            BuiltInRegistries.MOB_EFFECT, feature.registryId("echolocation"), new EcholocationEffect()));
    }

    @Override
    public Runnable boot() {
        return () -> {
            UseEntityCallback.EVENT.register(feature().handlers::captureBat);
            svenhjol.charmony.api.events.PlayerTickCallback.EVENT.register(feature().handlers::playerTick);
            CommonRegistry.forFeature(feature()).wandererTrade(
                () -> new GenericTrades.ItemsForEmeralds(batBucketItem.get(), 8, 1, 1, 1), true);
        };
    }
}
