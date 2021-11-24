package net.coderbot.iris.mixin;

import com.mojang.blaze3d.platform.GlUtil;
import com.mojang.blaze3d.vertex.PoseStack;
import net.coderbot.iris.Iris;
import net.coderbot.iris.gui.screen.HudHideable;
import net.coderbot.iris.pipeline.FixedFunctionWorldRenderingPipeline;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.renderer.ItemInHandRenderer;
import net.minecraft.client.renderer.MultiBufferSource.BufferSource;
import net.minecraft.client.renderer.RenderBuffers;
import net.minecraft.server.packs.resources.ResourceManager;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(GameRenderer.class)
@Environment(EnvType.CLIENT)
public class MixinGameRenderer {
	@Shadow @Final
	private Minecraft minecraft;

	@Shadow
	private boolean renderHand;

	@Inject(method = "<init>", at = @At("TAIL"))
	private void iris$logSystem(Minecraft client, ResourceManager resourceManager, RenderBuffers bufferBuilderStorage,
								CallbackInfo ci) {
		Iris.logger.info("Hardware information:");
		Iris.logger.info("CPU: " + GlUtil.getCpuInfo());
		Iris.logger.info("GPU: " + GlUtil.getRenderer() + " (Supports OpenGL " + GlUtil.getOpenGLVersion() + ")");
		Iris.logger.info("OS: " + System.getProperty("os.name"));
	}

	@Inject(method = "shouldRenderBlockOutline", at = @At("HEAD"), cancellable = true)
	public void iris$handleHudHidingScreens(CallbackInfoReturnable<Boolean> cir) {
		Screen screen = this.minecraft.screen;

		if (screen instanceof HudHideable) {
			cir.setReturnValue(false);
		}
	}

	@Redirect(method = "renderItemInHand", at = @At(value = "INVOKE", target = "Lnet/minecraft/client/renderer/ItemInHandRenderer;renderHandsWithItems(FLcom/mojang/blaze3d/vertex/PoseStack;Lnet/minecraft/client/renderer/MultiBufferSource$BufferSource;Lnet/minecraft/client/player/LocalPlayer;I)V"))
	private void disableVanillaHandRendering(ItemInHandRenderer itemInHandRenderer, float tickDelta, PoseStack poseStack, BufferSource bufferSource, LocalPlayer localPlayer, int light) {
		if (!(Iris.getPipelineManager().getPipeline() instanceof FixedFunctionWorldRenderingPipeline)) {
			return;
		}

		itemInHandRenderer.renderHandsWithItems(tickDelta, poseStack, bufferSource, localPlayer, light);
	}
}
