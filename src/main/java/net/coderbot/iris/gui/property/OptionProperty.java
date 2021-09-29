package net.coderbot.iris.gui.property;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.vertex.PoseStack;
import net.coderbot.iris.Iris;
import net.coderbot.iris.gui.GuiUtil;
import net.coderbot.iris.gui.element.PropertyDocumentWidget;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import org.lwjgl.glfw.GLFW;

import java.util.List;

public abstract class OptionProperty<T> extends ValueProperty<T> {
    protected List<T> values;
    protected int index;
    protected final int defaultIndex;
    protected final boolean isSlider;

    public OptionProperty(List<T> values, int defaultIndex, PropertyDocumentWidget document, String key, Component label, boolean isSlider) {
        super(document, key, label);
        this.values = values;
        this.index = defaultIndex;
        this.defaultIndex = defaultIndex;
        this.isSlider = isSlider;
    }

    public void cycle(boolean reverse) {
    	if (reverse) {
			this.index--;
			if (index < 0) index = values.size() - 1;
		} else {
    		this.index++;
			if (index >= values.size()) index = 0;
		}
        this.valueText = null;
    }

    @Override
    public T getValue() {
    	if (values.size() == 0) return fallbackValue();
    	index = Math.max(0, Math.min(index, values.size() - 1));
        return values.get(index);
    }

    @Override
    public void setValue(T value) {
        if (values.contains(value)) {
            this.index = values.indexOf(value);
        } else {
            Iris.logger.warn("Unable to set value of {} to {} - Invalid value!", key, value);
            this.index = defaultIndex;
        }
        this.valueText = null;
    }

    protected boolean isButtonHovered(double mouseX, boolean entryHovered) {
        return entryHovered && mouseX > cachedX + (cachedWidth * 0.6) - 7;
    }

    @Override
    public boolean onClicked(double mouseX, double mouseY, int button) {
        if (isButtonHovered(mouseX, true) && button == 0) {
            GuiUtil.playButtonClickSound();
            if (!isSlider) {
            	// Cycle in reverse if the shift key is pressed
            	cycle(InputConstants.isKeyDown(Minecraft.getInstance().getWindow().getWindow(), GLFW.GLFW_KEY_LEFT_SHIFT));
            }
            return true;
        }
        return false;
    }

    @Override
    public boolean mouseDragged(double mouseX, double mouseY, int button, double deltaX, double deltaY) {
        if (isSlider && isButtonHovered(mouseX, true)) {
            float pos = (float)((mouseX - (cachedX + (cachedWidth * 0.6) - 7)) / ((cachedWidth * 0.4)));
            this.index = Math.min((int)(pos * this.values.size()), this.values.size() - 1);
            this.valueText = null;
            return true;
        }
        return false;
    }

    @Override
    public void render(PoseStack poseStack, int x, int y, int width, int height, int mouseX, int mouseY, boolean isHovered, float delta) {
        updateCaches(width, x);
        Minecraft mc = Minecraft.getInstance();
        this.drawText(mc, label, poseStack, x + 10, y + (height / 2), 0xFFFFFF, false, true, true);
        if (isSlider) this.renderSlider(mc, poseStack, x, y, width, height, mouseX, isHovered);
        else this.renderButton(mc, poseStack, x, y, width, height, mouseX, isHovered);
    }

    private void renderButton(Minecraft mc, PoseStack poseStack, int x, int y, int width, int height, int mouseX, boolean isHovered) {
        int bx = (int)(x + (width * 0.6)) - 7;
        int bw = (int)(width * 0.4);

        GuiUtil.drawButton(poseStack, bx, y, bw, height, this.isButtonHovered(mouseX, isHovered), false, false);

        Component vt = this.getValueText();
        this.drawText(mc, vt, poseStack, (int)(x + (width * 0.8)) - (mc.font.width(vt) / 2) - 7, y + (height / 2), 0xFFFFFF, false, true, true);
    }

    private void renderSlider(Minecraft mc, PoseStack poseStack, int x, int y, int width, int height, int mouseX, boolean isHovered) {
        float progress = ((float)this.index / (this.values.size() - 1));
        int sx = (int)(x + (width * 0.6)) - 7;
        int sw = (int)(width * 0.4);

        GuiUtil.drawSlider(poseStack, sx, y, sw, height, this.isButtonHovered(mouseX, isHovered), progress);

        Component vt = this.getValueText();
        this.drawText(mc, vt, poseStack, (int)(x + (width * 0.8)) - (mc.font.width(vt) / 2) - 7, y + (height / 2), 0xFFFFFF, false, true, true);
    }

    @Override
    public boolean isDefault() {
        return index == defaultIndex;
    }

    protected abstract T fallbackValue();
}
