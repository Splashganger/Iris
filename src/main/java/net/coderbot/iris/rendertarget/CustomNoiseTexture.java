package net.coderbot.iris.rendertarget;

import com.mojang.blaze3d.platform.NativeImage;
import net.minecraft.client.renderer.texture.DynamicTexture;

import java.io.IOException;
import java.io.InputStream;
import java.util.Objects;

public class CustomNoiseTexture extends DynamicTexture {
	public CustomNoiseTexture(InputStream inputStream) throws IOException {
		super(read(inputStream));
	}

	private static NativeImage read(InputStream inputStream) throws IOException {
		return NativeImage.read(inputStream);
	}

	@Override
	public void upload() {
		NativeImage image = Objects.requireNonNull(getPixels());

		bind();
		// TODO: Respect options (blur, clamp, mipmap, close) in noise.png.mcmeta
		image.upload(0, 0, 0, 0, 0, image.getWidth(), image.getHeight(), true, false, false, false);
	}
}
