package net.coderbot.iris.gui.property;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

/**
 * An element of a document. Used for options
 * or configuration menus. Property objects are
 * added to a PropertyList, and PropertyLists are
 * added to a PropertyDocumentWidget which is used
 * in a GUI.
 */
public class Property {
    protected final Component label;

    /**
     * A completely empty property. When used in the
     * shader pack config document, EMPTYs can be
     * hidden by enabling condensed view.
     */
    public static final Property EMPTY = new Property(TextComponent.EMPTY);

    /**
     * The only difference between this and
     * EMPTY is that it is not EMPTY, and
     * won't be included if EMPTY is ever
     * searched for.
     */
    public static final Property PLACEHOLDER = new Property(TextComponent.EMPTY);

    public Property(Component label) {
        this.label = label;
    }

    public boolean onClicked(double mouseX, double mouseY, int button) {
        return false;
    }

    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        return false;
    }

    public void render(PoseStack poseStack, int x, int y, int width, int height, int mouseX, int mouseY, boolean isHovered, float delta) {
        this.drawText(Minecraft.getInstance(), label, poseStack, x + 10, y + (height / 2), 0xFFFFFF, false, true, true);
    }

    public boolean charTyped(char c, int keyCode) {
        return false;
    }

    protected final void drawText(Minecraft client, Component text, PoseStack poseStack, int x, int y, int color, boolean centerX, boolean centerY, boolean shadow) {
        Font font = client.font;
        if (shadow) font.drawShadow(poseStack, text, centerX ? x - (int)((float)font.width(text) / 2) : x, centerY ? y - 4 : y, color);
        else font.draw(poseStack, text, centerX ? x - (int)((float)font.width(text) / 2) : x, centerY ? y - 4 : y, color);
    }

	public Component getLabel() {
		return label;
	}
}
