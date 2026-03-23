package com.juiceybeans.pnc_electrostatic_fix.mixin;

import me.desht.pneumaticcraft.common.block.entity.compressor.ElectrostaticCompressorBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ElectrostaticCompressorBlockEntity.class)
public interface ElectrostaticCompressorBlockEntityAccessor {
    @Accessor("struckByLightningCooldown")
    void pnc_electrostatic_fix$setStruckByLightningCooldown(int struckByLightningCooldown);
}
