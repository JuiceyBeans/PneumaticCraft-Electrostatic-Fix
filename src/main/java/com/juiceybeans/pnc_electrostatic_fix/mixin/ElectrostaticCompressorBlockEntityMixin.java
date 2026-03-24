package com.juiceybeans.pnc_electrostatic_fix.mixin;

import me.desht.pneumaticcraft.common.block.entity.ElectrostaticCompressorBlockEntity;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.RandomSource;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.util.Set;

@Mixin(value = ElectrostaticCompressorBlockEntity.class, remap = false)
public class ElectrostaticCompressorBlockEntityMixin {

    @Inject(
            method = "maybeLightningStrike",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;addFreshEntity(Lnet/minecraft/world/entity/Entity;)Z"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void pnc_electrostatic_fix$mark_summoned(CallbackInfo ci, Level level, RandomSource rnd, int dist, float angle,
                                                     int x, int z, int y,
                                                     BlockPos hitPos, BlockState state,
                                                     Set<BlockPos> gridSet, Set<ElectrostaticCompressorBlockEntity> compressorSet,
                                                     LightningBolt bolt) {
        CompoundTag tag = new CompoundTag();
        tag.putBoolean("compressor_summoned", true);
        bolt.save(tag);
    }
}
