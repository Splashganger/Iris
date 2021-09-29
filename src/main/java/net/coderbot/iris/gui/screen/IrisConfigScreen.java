package net.coderbot.iris.gui.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import net.coderbot.iris.Iris;
import net.coderbot.iris.config.IrisConfig;
import net.coderbot.iris.gui.GuiUtil;
import net.coderbot.iris.gui.ScreenStack;
import net.coderbot.iris.gui.element.PropertyDocumentWidget;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.CommonComponents;
import net.minecraft.network.chat.TextComponent;
import net.minecraft.network.chat.TranslatableComponent;

public class IrisConfigScreen extends Screen implements HudHideable {
    protected final IrisConfig config = Iris.getIrisConfig();
    protected PropertyDocumentWidget configProperties;

    private final Screen parent;

	public IrisConfigScreen(Screen parent) {
        super(new TextComponent(""));
        this.parent = parent;
    }

    @Override
    protected void init() {
        super.init();
        int bottomCenter = this.width / 2 - 50;
        boolean inWorld = this.minecraft.level != null;

        float scrollAmount = 0.0f;
        String page = "main";

        if (this.configProperties != null) {
            scrollAmount = (float)this.configProperties.getScrollAmount() / this.configProperties.getMaxScroll();
            page = this.configProperties.getCurrentPage();
        }

        this.configProperties  = new PropertyDocumentWidget(minecraft, width, height, 20, this.height - 34, 0, this.width, 26, width - 39);
        if (inWorld) this.configProperties.setRenderBackground(false);
        this.configProperties.setDocument(this.config.createDocument(this.minecraft.font, this, this.configProperties, 320), "main");

        this.configProperties.setScrollAmount(this.configProperties.getMaxScroll() * scrollAmount);
        this.configProperties.goTo(page);

        this.addRenderableWidget(configProperties);

        this.addRenderableWidget(new Button(bottomCenter + 104, this.height - 27, 100, 20, CommonComponents.GUI_DONE, button -> { this.saveConfig(); onClose(); }));
        this.addRenderableWidget(new Button(bottomCenter, this.height - 27, 100, 20, new TranslatableComponent("options.iris.apply"), button -> this.saveConfig()));
        this.addRenderableWidget(new Button(bottomCenter - 104, this.height - 27, 100, 20, new TranslatableComponent("options.iris.refresh"), button -> this.loadConfig()));

        loadConfig();

		if (parent != null) {
			ScreenStack.push(parent);
		}
    }

    @Override
    public void render(PoseStack poseStack, int mouseX, int mouseY, float delta) {
        if (this.minecraft.level == null) this.renderBackground(poseStack);
        else this.fillGradient(poseStack, 0, 0, width, height, 0x4F232323, 0x4F232323);
        this.configProperties.render(poseStack, mouseX, mouseY, delta);

        GuiUtil.drawDirtTexture(minecraft, 0, 0, -100, width, 20);
        GuiUtil.drawDirtTexture(minecraft, 0, this.height - 34, -100, width, 34);
        super.render(poseStack, mouseX, mouseY, delta);
    }

    @Override
    public void onClose() {
		ScreenStack.pull(this.getClass());
		minecraft.setScreen(ScreenStack.pop());
	}

    private void loadConfig() {
        this.configProperties.loadProperties();
    }

    private void saveConfig() {
        this.configProperties.saveProperties();
    }
}
