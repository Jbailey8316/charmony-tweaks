package svenhjol.charmony.tweaks.common.features.crafting_from_inventory;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import svenhjol.charmony.core.Charmony;

public final class Tags {
    public static final TagKey<Item> CRAFTING_TABLES = TagKey.create(Registries.ITEM,
        ResourceLocation.fromNamespaceAndPath(Charmony.ID, "crafting_tables"));

    private Tags() {}
}
