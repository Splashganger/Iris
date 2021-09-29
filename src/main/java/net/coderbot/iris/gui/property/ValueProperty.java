package net.coderbot.iris.gui.property;

import net.coderbot.iris.gui.element.PropertyDocumentWidget;
import net.minecraft.network.chat.Component;

public abstract class ValueProperty<T> extends Property {
    protected final String key;
    protected final PropertyDocumentWidget document;
    protected Component valueText;

    protected int cachedWidth = 0;
    protected int cachedX = 0;

    public ValueProperty(PropertyDocumentWidget document, String key, Component label) {
        super(label);
        this.key = key;
        this.document = document;
    }

    public String getKey() {
        return key;
    }

    public abstract T getValue();

    public abstract Component createValueText(int width);

    public final Component getValueText() {
        if (valueText == null) {
            valueText = createValueText((int)(cachedWidth * 0.4) - 6);
        }
        return valueText;
    }

    public abstract boolean isDefault();

    public abstract void setValue(T value);

    public abstract void setValue(String value);

    public void resetValueText() {
        this.valueText = null;
    }

    public void updateCaches(int width, int x) {
        this.cachedWidth = width;
        this.cachedX = x;
    }
}
