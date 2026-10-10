package com.lightclient.mixin;

import com.lightclient.xray.XRayState;
import net.minecraft.block.AbstractBlock;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.BlockView;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(AbstractBlock.AbstractBlockState.class)
public abstract class AbstractBlockStateMixin {
    @Inject(method = "isOpaqueFullCube", at = @At("HEAD"), cancellable = true)
    private void lightclient$noOcclusion(BlockView world, BlockPos pos, CallbackInfoReturnable<Boolean> cir) {
        if (!XRayState.isEnabled()) return;
        AbstractBlock.AbstractBlockState self = (AbstractBlock.AbstractBlockState) (Object) this;
        if (!XRayState.shouldRender(self)) {
            cir.setReturnValue(false);
        }
    }
}
