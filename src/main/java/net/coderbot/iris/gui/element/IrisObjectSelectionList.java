package net.coderbot.iris.gui.element;

import com.mojang.blaze3d.vertex.PoseStack;
import net.coderbot.iris.Iris;
import net.coderbot.iris.gui.GuiUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.components.ObjectSelectionList;
import net.minecraft.client.gui.components.events.GuiEventListener;


public abstract class IrisObjectSelectionList<E extends ObjectSelectionList.Entry<E>> extends ObjectSelectionList<E> implements GuiEventListener {
	protected int scrollbarFade = 0;
	protected boolean hovered = false;

	public IrisObjectSelectionList(Minecraft minecraftClient, int width, int height, int top, int bottom, int left, int right, int itemHeight) {
		super(minecraftClient, width, height, top, bottom, itemHeight);

		this.x0 = left;
		this.x1 = right;
	}

	@Override
	public int getRowWidth() {
		return width - 6;
	}
	
	public void tick() {
		if (hovered) {
			if (scrollbarFade < 3) scrollbarFade++;
		} else if (scrollbarFade > 0) scrollbarFade--;
	}

	@Override
	protected final int getScrollbarPosition() {
		return 32767; // Make the vanilla scrollbar not
	}

	protected int getScrollbarXOffset() {
		return -2;
	}

	protected int getScrollbarTopMargin() {
		return 2;
	}

	protected int getScrollbarBottomMargin() {
		return 2;
	}

	public void select(int entry) {
		setSelected(this.getEntry(entry));
	}

	@Override
	public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
		super.render(poseStack, mouseX, mouseY, delta);
		GuiUtil.drawCompactScrollBar(this.x0 + this.width + getScrollbarXOffset(), this.y0 + getScrollbarTopMargin(), this.y1 - getScrollbarBottomMargin(), this.getMaxScroll(), this.getScrollAmount(), this.getMaxPosition(), Math.max(0, Math.min(3, this.scrollbarFade + (hovered ? delta : -delta))) / 3);
		this.hovered = this.isMouseOver(mouseX, mouseY);
	}

	@Override
	public boolean mouseScrolled(double mouseX, double mouseY, double amount) {
		this.setScrollAmount(this.getScrollAmount() - amount * (double)this.itemHeight / 2.0D * (Iris.getIrisConfig().getScrollSpeed() / 100D));
		return true;
	}
}
