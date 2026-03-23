package com.juiceybeans.pnc_electrostatic_fix;

import com.juiceybeans.pnc_electrostatic_fix.mixin.AbstractAirHandlingBlockEntityAccessor;
import com.juiceybeans.pnc_electrostatic_fix.mixin.ElectrostaticCompressorBlockEntityAccessor;
import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.objects.ObjectOpenHashSet;
import me.desht.pneumaticcraft.common.block.entity.compressor.ElectrostaticCompressorBlockEntity;
import me.desht.pneumaticcraft.common.registry.ModBlockEntityTypes;
import me.desht.pneumaticcraft.lib.PneumaticValues;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.LightningBolt;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.entity.EntityJoinLevelEvent;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.ModLoadingContext;
import org.slf4j.Logger;

import java.util.Optional;
import java.util.Set;

import static me.desht.pneumaticcraft.common.block.entity.compressor.ElectrostaticCompressorBlockEntity.MAX_ELECTROSTATIC_GRID_SIZE;

@Mod(PNCElectrostaticFix.MOD_ID)
public class PNCElectrostaticFix {
    public static final String MOD_ID = "pnc_electrostatic_fix";
    private static final Logger LOGGER = LogUtils.getLogger();

    public PNCElectrostaticFix() {
        IEventBus modEventBus = ModLoadingContext.get().getActiveContainer().getEventBus();

        NeoForge.EVENT_BUS.register(this);
    }

    @SubscribeEvent
    public void onLightningStruck(EntityJoinLevelEvent event) {
        if (event.getEntity() instanceof LightningBolt lightning) {
            var centerPos = lightning.blockPosition();
            Level level = lightning.level();

            for (int x = -1; x <= 1; x++) {
                for (int z = -1; z <= 1; z++) {
                    BlockPos hitPos = centerPos.offset(x, 0, z);
                    Optional<ElectrostaticCompressorBlockEntity> blockEntity = level.getBlockEntity(hitPos, ModBlockEntityTypes.ELECTROSTATIC_COMPRESSOR.get());

                    blockEntity.ifPresent(compressor -> {
                        Set<BlockPos> gridSet = new ObjectOpenHashSet<>(MAX_ELECTROSTATIC_GRID_SIZE);
                        Set<ElectrostaticCompressorBlockEntity> compressorSet = new ObjectOpenHashSet<>(20);

                        compressor.getElectrostaticGrid(gridSet, compressorSet, hitPos);

                        for (ElectrostaticCompressorBlockEntity c : compressorSet) {
                            onStruckByLightning(c, compressorSet.size());
                        }
                    });
                }
            }
        }
    }

    private void onStruckByLightning(ElectrostaticCompressorBlockEntity compressor, int divisor) {
        compressor.addAir(PneumaticValues.PRODUCTION_ELECTROSTATIC_COMPRESSOR / divisor);
        ((ElectrostaticCompressorBlockEntityAccessor) compressor).pnc_electrostatic_fix$setStruckByLightningCooldown(10);
        float excessPressure = compressor.getPressure() - compressor.getDangerPressure();
        if (excessPressure > 0f) {
            int maxRedirection = PneumaticValues.MAX_REDIRECTION_PER_IRON_BAR * compressor.ironBarsBeneath;
            int excessAir = (int) (excessPressure * ((AbstractAirHandlingBlockEntityAccessor) compressor).pnc_electrostatic_fix$airHandler().getVolume());
            compressor.addAir(-Math.min(maxRedirection, excessAir + 10));
        }
    }
}
