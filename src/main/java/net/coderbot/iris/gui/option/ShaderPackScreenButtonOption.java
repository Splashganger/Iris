package net.coderbot.iris.gui.option;

import net.coderbot.iris.gui.screen.ShaderPackScreen;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Option;
import net.minecraft.client.Options;
import net.minecraft.client.gui.components.AbstractButton;
import net.minecraft.client.gui.components.OptionButton;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.network.chat.TranslatableComponent;

public class ShaderPackScreenButtonOption extends Option {
    private final Screen parent;
    private final Minecraft minecraft;

    public ShaderPackScreenButtonOption(Screen parent, Minecraft minecraft) {
        super("options.iris.shaderPackSelection");
        this.parent = parent;
        this.minecraft = minecraft;
    }

    @Override
    public AbstractButton createButton(Options options, int x, int y, int width) {
        return new OptionButton(x, y, width, 20, this, new TranslatableComponent("options.iris.shaderPackSelection"), button -> minecraft.setScreen(new ShaderPackScreen(this.parent)));
    }
}
