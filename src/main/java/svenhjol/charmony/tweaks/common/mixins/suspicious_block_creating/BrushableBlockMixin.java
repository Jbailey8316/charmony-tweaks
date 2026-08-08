package svenhjol.charmony.tweaks.common.mixins.suspicious_block_creating;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.item.FallingBlockEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.BrushableBlock;
import net.minecraft.world.level.block.entity.BrushableBlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

/**
 * Vanilla creates a FallingBlockEntity for brushable blocks without copying
 * the source BrushableBlockEntity payload. FallingBlockEntity already knows
 * how to persist and restore blockData, so carry the normal vanilla payload
 * through that existing path.
 */
@Mixin(BrushableBlock.class)
public class BrushableBlockMixin {
    @Redirect(
        method = "tick",
        at = @At(
            value = "INVOKE",
            target = "Lnet/minecraft/world/entity/item/FallingBlockEntity;fall(Lnet/minecraft/world/level/Level;Lnet/minecraft/core/BlockPos;Lnet/minecraft/world/level/block/state/BlockState;)Lnet/minecraft/world/entity/item/FallingBlockEntity;"
        )
    )
    private FallingBlockEntity carryBlockEntityData(Level level, BlockPos pos, BlockState state) {
        var payload = level.getBlockEntity(pos) instanceof BrushableBlockEntity brushable
            ? brushable.saveWithoutMetadata(level.registryAccess())
            : null;
        var falling = FallingBlockEntity.fall(level, pos, state);
        falling.blockData = payload;
        return falling;
    }
}
