package net.coderbot.iris.gui;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.coderbot.iris.Iris;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiComponent;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.renderer.GameRenderer;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.client.resources.sounds.SimpleSoundInstance;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.util.Mth;

public final class GuiUtil {
	private static final ResourceLocation IRIS_WIDGETS_TEX = new ResourceLocation("iris", "textures/gui/widgets.png");

	private GuiUtil() {}

	private static Minecraft client() {
		return Minecraft.getInstance();
	}

    public static void drawDirtTexture(Minecraft client, int x, int y, int z, int width, int height) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        client.getTextureManager().bindForSetup(GuiComponent.BACKGROUND_LOCATION);
        RenderSystem.enableDepthTest();
        RenderSystem.depthFunc(519);
        RenderSystem.enableTexture();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(x, y + height, z).uv(x, y + height / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferBuilder.vertex(x + width, y + height, 0.0D).uv(x + width / 32.0F, y + height / 32.0F).color(64, 64, 64, 255).endVertex();
        bufferBuilder.vertex(x + width, y, z).uv(x + width / 32.0F, y).color(64, 64, 64, 255).endVertex();
        bufferBuilder.vertex(x, y, z).uv(x, y).color(64, 64, 64, 255).endVertex();
        tessellator.end();
    }

    public static void drawCompactScrollBar(int x, int top, int bottom, int maxScroll, double scrollAmount, int maxPosition, float alpha) {
        if (maxScroll > 0) {
            int barHeight = (int)((float)((bottom - top) * (bottom - top)) / (float) maxPosition);
            barHeight = Mth.clamp(barHeight, 32, bottom - top - 8);
            int barTop = (int)scrollAmount * (bottom - top - barHeight) / maxScroll + top;
            if (barTop < top) barTop = top;
            int backgroundColor = (((byte)(0x6E * alpha) << 24) | 0x0A0A0A);
            int barColor = (((byte)(0x7A * alpha) << 24) | 0xEFEFEF);
            GuiUtil.fill(x, top, -100, 2, bottom - top, backgroundColor);
            GuiUtil.fill(x, barTop, -100, 2, barHeight, barColor);
            RenderSystem.enableTexture();
        }
    }

    public static void texture(int x, int y, int z, int width, int height, float u, float v, float uw, float vh, float r, float g, float b, float a) {
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.enableTexture();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
        bufferBuilder.vertex(x, y + height, z).uv(u, v + vh).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x + width, y + height, z).uv(u + uw, v + vh).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x + width, y, z).uv(u + uw, v).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x, y, z).uv(u, v).color(r, g, b, a).endVertex();
        tessellator.end();
    }

    public static void texture(int x, int y, int z, int width, int height, float u, float v, float uw, float vh) {
        texture(x, y, z, width, height, u, v, uw, vh, 1f, 1f, 1f, 1f);
    }

    public static void texture(int x, int y, int z, int width, int height, int u, int v, int uw, int vh, int texWidth, int texHeight) {
        texture(x, y, z, width, height, (float)u / texWidth, (float)v / texHeight, (float)uw / texWidth, (float)vh / texHeight);
    }

    public static void texture(int x, int y, int z, int width, int height, int u, int v) {
        texture(x, y, z, width, height, u, v, width, height, 256, 256);
    }

    public static void fill(int x, int y, int z, int width, int height, int colorARGB) {
        int a = (colorARGB >> 24) & 0xFF;
        int r = (colorARGB >> 16) & 0xFF;
        int g = (colorARGB >> 8) & 0xFF;
        int b = colorARGB & 0xFF;
        Tesselator tessellator = Tesselator.getInstance();
        BufferBuilder bufferBuilder = tessellator.getBuilder();
        RenderSystem.setShader(GameRenderer::getPositionColorShader);
        RenderSystem.disableTexture();
        RenderSystem.enableBlend();
        bufferBuilder.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        bufferBuilder.vertex(x, y + height, z).uv(0f, 1f).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x + width, y + height, z).uv(1f, 1f).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x + width, y, z).uv(1f, 0f).color(r, g, b, a).endVertex();
        bufferBuilder.vertex(x, y, z).uv(0f, 0f).color(r, g, b, a).endVertex();
        tessellator.end();
    }

    public static void fill(int x, int y, int width, int height, int colorARGB) {
        fill(x, y, -100, width, height, colorARGB);
    }

    public static void borderedRect(int x, int y, int z, int width, int height, int colorARGB) {
        fill(x, y, z, width, 1, colorARGB);
        fill(x, y + height - 1, z, width, 1, colorARGB);
        fill(x, y + 1, z, 1, height - 2, colorARGB);
        fill(x + width - 1, y + 1, z, 1, height - 2, colorARGB);
    }

	/**
	 * Plays the {@code UI_BUTTON_CLICK} sound event as a
	 * master sound effect.
	 *
	 * Used in non-{@code Button} UI elements upon click
	 * or other action.
	 */
	public static void playButtonClickSound() {
		client().getSoundManager().play(SimpleSoundInstance.forUI(SoundEvents.UI_BUTTON_CLICK, 1));
	}

	public static Component trimmed(Font font, String text, int lenPixels, boolean translated, boolean ellipsis, ChatFormatting... formats) {
		String tx = translated ? I18n.get(text) : text;
		TextComponent t = (TextComponent) new TextComponent(tx).withStyle(formats);
		if (font.width(t) > lenPixels) {
			return new TextComponent(font.plainSubstrByWidth(tx, lenPixels - (ellipsis ? 8 : 0)) + (ellipsis ? "..." : "")).withStyle(formats);
		}
		return t;
	}


	/**
	 * Binds Iris's widgets texture to be
	 * used for succeeding draw calls.
	 */
	public static void bindIrisWidgetsTexture() {
		RenderSystem.setShaderTexture(0, IRIS_WIDGETS_TEX);
	}

	/**
	 * Draws a button. Button textures must be mapped with the
	 * same coordinates as those on the vanilla widgets texture.
	 *
	 * @param x X position of the left of the button
	 * @param y Y position of the top of the button
	 * @param width Width of the button, maximum 398
	 * @param height Height of the button, maximum 20
	 * @param hovered Whether the button is being hovered over with the mouse
	 * @param disabled Whether the button should use the "disabled" texture
	 */
	public static void drawButton(PoseStack poseStack, int x, int y, int width, int height, boolean hovered, boolean disabled, boolean isLink) {
		UiTheme theme = Iris.getIrisConfig().getUITheme();
		if (theme == UiTheme.AQUA) {
			y += 1;
			height -= 2;
		}
		if (theme == UiTheme.VANILLA) {
			Minecraft.getInstance().getTextureManager().bindForSetup(AbstractButton.WIDGETS_LOCATION);

			// Create variables for half of the width and height.
			// Will not be exact when width and height are odd, but
			// that case is handled within the draw calls.
			int halfWidth = width / 2;
			int halfHeight = height / 2;

			// V offset for which button texture to use
			int vOffset = disabled ? 46 : hovered ? 86 : 66;

			// Sets RenderSystem to use solid white as the tint color for blend mode, and enables blend mode
			RenderSystem.enableBlend();

			// Sets RenderSystem to be able to use textures when drawing
			// This doesn't do anything on 1.17
			RenderSystem.enableTexture();

			// Top left section
			GuiComponent.blit(poseStack, x, y, 0, vOffset, halfWidth, halfHeight, 256, 256);
			// Top right section
			GuiComponent.blit(poseStack, x + halfWidth, y, 200 - (width - halfWidth), vOffset, width - halfWidth, halfHeight, 256, 256);
			// Bottom left section
			GuiComponent.blit(poseStack, x, y + halfHeight, 0, vOffset + (20 - (height - halfHeight)), halfWidth, height - halfHeight, 256, 256);
			// Bottom right section
			GuiComponent.blit(poseStack, x + halfWidth, y + halfHeight, 200 - (width - halfWidth), vOffset + (20 - (height - halfHeight)), width - halfWidth, height - halfHeight, 256, 256);
		} else {
			if (hovered) {
				fill(x, y, width, height, theme == UiTheme.IRIS ? 0x8AE0E0E0 : 0xE0000000);
				if (theme == UiTheme.AQUA && isLink) GuiUtil.fill(x, y + height, width, 1, 0xFF94E4D3);
			} else if (theme == UiTheme.IRIS) {
				borderedRect(x, y, -100, width, height, 0x8AE0E0E0);
			} else {
				fill(x, y, width, height, 0x90000000);
			}
		}
	}

	public static void drawSlider(PoseStack poseStack, int x, int y, int width, int height, boolean hovered, float progress) {
		UiTheme theme = Iris.getIrisConfig().getUITheme();
		if (theme == UiTheme.IRIS) {
			int color = 0x8AE0E0E0;

			GuiUtil.borderedRect(x, y + 2, -100, width, height - 4, color);
			GuiUtil.fill(x + 1, y + 3, width - 2, height - 6, 0x73000000);

			int sx = (x + 2) + Math.round(progress * (width - 10));

			if (hovered) {
				GuiUtil.fill(sx, y + 4, 6, height - 8, color);
			} else {
				GuiUtil.borderedRect(sx, y + 4, -100, 6, height - 8, color);
			}
		} else if (theme == UiTheme.AQUA) {
			fill(x, y, width, height, hovered ? 0xE0000000 : 0x90000000);

			GuiUtil.fill(x + 2, (int)(y + (height * 0.75)), width - 4, 1, 0xFFFFFFFF);

			int sx = x + 2 + Math.round(progress * (width - 7));
			GuiUtil.fill(sx, (int)(y + (height * 0.75)) - 3, 3, 7, 0xFFFFFFFF);
		} else {
			Minecraft.getInstance().getTextureManager().bindForSetup(AbstractButton.WIDGETS_LOCATION);

			// Create variables for half of the width and height.
			// Will not be exact when width and height are odd, but
			// that case is handled within the draw calls.
			int halfWidth = width / 2;
			int halfHeight = height / 2;

			// V offset for which button texture to use
			int vOffset = 46;

			// Top left section
			GuiComponent.blit(poseStack, x, y, 0, vOffset, halfWidth, halfHeight, 256, 256);
			// Top right section
			GuiComponent.blit(new PoseStack(), x + halfWidth, y, 200 - (width - halfWidth), vOffset, width - halfWidth, halfHeight, 256, 256);
			// Bottom left section
			GuiComponent.blit(new PoseStack(), x, y + halfHeight, 0, vOffset + (20 - (height - halfHeight)), halfWidth, height - halfHeight, 256, 256);
			// Bottom right section
			GuiComponent.blit(new PoseStack(), x + halfWidth, y + halfHeight, 200 - (width - halfWidth), vOffset + (20 - (height - halfHeight)), width - halfWidth, height - halfHeight, 256, 256);

			int yp = y + (int)Math.ceil((float)Math.max(0, height - 20) / 2);

			int sx = x + Math.round(progress * (width - 8));
			int sv = (hovered ? 2 : 1) * 20;

			texture(sx, yp, -100, 4, 20, 0, 46 + sv);
			texture(sx + 3, yp, -100, 4, 20, 196, 46 + sv);
		}
	}
}
