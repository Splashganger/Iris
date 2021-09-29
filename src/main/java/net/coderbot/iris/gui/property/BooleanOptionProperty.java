package net.coderbot.iris.gui.property;

import com.google.common.collect.ImmutableList;
import net.coderbot.iris.gui.GuiUtil;
import net.coderbot.iris.gui.element.PropertyDocumentWidget;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;

public class BooleanOptionProperty extends OptionProperty<Boolean> {
    public BooleanOptionProperty(PropertyDocumentWidget document, boolean defaultValue, String key, Component label, boolean isSlider) {
        super(ImmutableList.of(true, false), defaultValue ? 0 : 1, document, key, label, isSlider);
    }

    @Override
    public Component createValueText(int width) {
        return GuiUtil.trimmed(Minecraft.getInstance().font, getValue() ? "property.iris.boolean.true" : "property.iris.boolean.false", width, true, true, isDefault() ? ChatFormatting.RESET : getValue() ? ChatFormatting.GREEN : ChatFormatting.RED);
    }

    @Override
    public void setValue(String value) {
        this.valueText = null;
        if (value.equals("true") || value.equals("false")) {
            this.setValue(Boolean.parseBoolean(value));
            return;
        }
        this.index = defaultIndex;
    }

	@Override
	protected Boolean fallbackValue() {
		return false;
	}
}
