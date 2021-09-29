package net.coderbot.iris.gui.property;

import net.coderbot.iris.gui.GuiUtil;
import net.coderbot.iris.gui.element.PropertyDocumentWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

import java.util.List;

public class StringOptionProperty extends OptionProperty<String> {
	protected final boolean translated;

    public StringOptionProperty(List<String> values, int defaultIndex, PropertyDocumentWidget document, String key, Component label, boolean isSlider, boolean displayTranslated) {
        super(values, defaultIndex, document, key, label, isSlider);
        this.translated = displayTranslated;
    }

    @Override
    public Component createValueText(int width) {
        return GuiUtil.trimmed(Minecraft.getInstance().font, getValue(), width, translated, true, isDefault() ? ChatFormatting.RESET : ChatFormatting.YELLOW);
    }

	@Override
	protected String fallbackValue() {
		return "";
	}
}
