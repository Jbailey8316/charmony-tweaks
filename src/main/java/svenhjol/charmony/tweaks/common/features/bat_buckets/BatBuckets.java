package svenhjol.charmony.tweaks.common.features.bat_buckets;

import svenhjol.charmony.api.core.Configurable;
import svenhjol.charmony.api.core.FeatureDefinition;
import svenhjol.charmony.api.core.Side;
import svenhjol.charmony.core.base.Mod;
import svenhjol.charmony.core.base.SidedFeature;

@FeatureDefinition(side = Side.Common, description = "Capture bats in buckets and release them to reveal nearby creatures.")
public final class BatBuckets extends SidedFeature {
    public final Registers registers;
    public final Handlers handlers;
    public final Advancements advancements;

    @Configurable(name = "Glowing time", description = "Number of seconds that entities receive the glowing effect.")
    private static int glowingTime = 10;
    @Configurable(name = "Glowing range", description = "Range in blocks in which entities glow.")
    private static int glowingRange = 24;
    @Configurable(name = "Damage bat", description = "If true, the bat takes half a heart of damage when released.")
    private static boolean damageBat = true;

    public BatBuckets(Mod mod) {
        super(mod);
        registers = new Registers(this);
        handlers = new Handlers(this);
        advancements = new Advancements(this);
    }
    public static BatBuckets feature() { return Mod.getSidedFeature(BatBuckets.class); }
    public int glowingTime() { return glowingTime; }
    public int glowingRange() { return glowingRange; }
    public boolean damageBat() { return damageBat; }
}
