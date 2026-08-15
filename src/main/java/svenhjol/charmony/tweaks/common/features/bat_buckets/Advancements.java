package svenhjol.charmony.tweaks.common.features.bat_buckets;

import net.minecraft.server.level.ServerPlayer;
import svenhjol.charmony.core.base.Setup;
import svenhjol.charmony.core.helpers.AdvancementHelper;

public class Advancements extends Setup<BatBuckets> {
    public Advancements(BatBuckets feature) { super(feature); }
    public void capturedBat(ServerPlayer player) { AdvancementHelper.trigger("captured_bat", player); }
    public void usedBatBucket(ServerPlayer player) { AdvancementHelper.trigger("used_bat_bucket", player); }
}
