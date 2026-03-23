package com.juiceybeans.pnc_electrostatic_fix.mixin;

import me.desht.pneumaticcraft.api.tileentity.IAirHandlerMachine;
import me.desht.pneumaticcraft.common.block.entity.AbstractAirHandlingBlockEntity;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(AbstractAirHandlingBlockEntity.class)
public interface AbstractAirHandlingBlockEntityAccessor {
    @Accessor("airHandler")
    IAirHandlerMachine pnc_electrostatic_fix$airHandler();
}
