package net.coderbot.iris.gui.property;

import net.coderbot.iris.gui.GuiUtil;
import net.coderbot.iris.gui.element.PropertyDocumentWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;

import java.util.List;

public class IntOptionProperty extends OptionProperty<Integer> {
    public IntOptionProperty(List<Integer> values, int defaultIndex, PropertyDocumentWidget document, String key, Component label, boolean isSlider) {
        super(values, defaultIndex, document, key, label, isSlider);
    }

	@Override
	protected Integer fallbackValue() {
		return 0;
	}

	@Override
    public Component createValueText(int width) {
    	String translation = "value." + key + "." + this.getValue();
    	boolean hasTranslation = I18n.exists(translation);
        return GuiUtil.trimmed(Minecraft.getInstance().font, hasTranslation ? translation : Integer.toString(this.getValue()), width, hasTranslation, true, isDefault() ? ChatFormatting.RESET : ChatFormatting.YELLOW);
    }

    @Override
    public void setValue(String value) {
        this.valueText = null;
        try {
            this.setValue(Integer.parseInt(value));
        } catch (NumberFormatException ignored) {
            this.index = defaultIndex;
        }
    }
}
