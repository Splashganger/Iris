package net.coderbot.iris.gui.property;

import net.minecraft.network.chat.Component;

public abstract class TupleProperty extends Property {
    public TupleProperty(Component label) {
        super(label);
    }

    public abstract Property[] getContainedProperties();
}
