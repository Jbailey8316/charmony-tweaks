package svenhjol.charmony.tweaks.client.features.bat_buckets;

import net.minecraft.world.item.CreativeModeTabs;
import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;
import svenhjol.charmony.core.client.ClientRegistry;
import svenhjol.charmony.tweaks.common.features.bat_buckets.BatBuckets;

@FeatureDefinition(side = Side.Client, canBeDisabledInConfig = false)
public final class BatBucketsClient extends SidedFeature {
    public BatBucketsClient(Mod mod) {
        super(mod);
        var common = BatBuckets.feature();
        ClientRegistry.forFeature(this).itemTab(
            common.registers.batBucketItem.get(), CreativeModeTabs.TOOLS_AND_UTILITIES, null);
    }

    public static BatBucketsClient feature() {
        return Mod.getSidedFeature(BatBucketsClient.class);
    }
}
