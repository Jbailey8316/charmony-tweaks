package svenhjol.charmony.tweaks.common.features.lower_noteblock_pitch;

import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.NoteBlock;
import net.minecraft.world.phys.BlockHitResult;
import svenhjol.charmony.core.base.Setup;

public class Handlers extends Setup<LowerNoteblockPitch> {
    public Handlers(LowerNoteblockPitch feature) {
        super(feature);
    }

    public InteractionResult useBlock(Player player, Level level, InteractionHand hand, BlockHitResult hitResult) {
        if (hand != InteractionHand.MAIN_HAND
            || level.isClientSide()
            || !player.getMainHandItem().isEmpty()
            || !player.isSecondaryUseActive()) {
            return InteractionResult.PASS;
        }

        var pos = hitResult.getBlockPos();
        var state = level.getBlockState(pos);
        if (!state.is(Blocks.NOTE_BLOCK)) {
            return InteractionResult.PASS;
        }

        var note = state.getValue(NoteBlock.NOTE);
        var lowered = state.setValue(NoteBlock.NOTE, note == 0 ? 24 : note - 1);
        level.setBlock(pos, lowered, 3);
        level.blockEvent(pos, lowered.getBlock(), 0, 0);
        level.gameEvent(player, net.minecraft.world.level.gameevent.GameEvent.NOTE_BLOCK_PLAY, pos);
        return InteractionResult.SUCCESS;
    }
}
