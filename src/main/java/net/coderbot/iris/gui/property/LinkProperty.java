package net.coderbot.iris.gui.property;

import com.mojang.blaze3d.vertex.PoseStack;
import net.coderbot.iris.gui.GuiUtil;
import net.coderbot.iris.gui.element.PropertyDocumentWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.TextComponent;

public class LinkProperty extends Property {
    protected final PropertyDocumentWidget document;
    protected final String page;
    protected final Align align;

    public LinkProperty(PropertyDocumentWidget document, String pageName, Component label, Align align) {
        super(label);
        this.document = document;
        this.page = pageName;
        this.align = align;
    }

    @Override
    public boolean onClicked(double mouseX, double mouseY, int button) {
        if (button == 0) {
            GuiUtil.playButtonClickSound();
            this.document.goTo(page);
            return true;
        }
        return false;
    }

    @Override
    public void render(PoseStack poseStack, int x, int y, int width, int height, int mouseX, int mouseY, boolean isHovered, float delta) {
        int bx = x + 4;
        int bw = width - 12;

        GuiUtil.drawButton(poseStack, bx, y, bw, height, isHovered, false, true);
        Minecraft mc = Minecraft.getInstance();
        int tx;
        int w = mc.font.width(this.label);
        if (this.align.center) tx = (x + (width/2)) - (w / 2) - 2;
        else if (this.align.left) tx = x + 10;
        else tx = x + width - 10 - w;
        this.drawText(mc, label, poseStack, tx, y + (height / 2), 0xFFFFFF, false, true, true);
        this.drawText(mc, new TextComponent(this.align.left ? ">" : "<"), poseStack, this.align.left ? x + width - 19 : x + 11, y + (height / 2), 0xFFFFFF, false, true, true);
    }

    public enum Align {
        LEFT(true, false), CENTER_LEFT(true, true), RIGHT(false, false), CENTER_RIGHT(false, true);

        public final boolean left;
        public final boolean center;

        Align(boolean left, boolean center) {
            this.left = left;
            this.center = center;
        }
    }
}
