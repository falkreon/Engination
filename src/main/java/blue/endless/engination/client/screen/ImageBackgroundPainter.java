package blue.endless.engination.client.screen;

import io.github.cottonmc.cotton.gui.client.BackgroundPainter;
import io.github.cottonmc.cotton.gui.client.ScreenDrawing;
import io.github.cottonmc.cotton.gui.widget.WWidget;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.util.Identifier;

public class ImageBackgroundPainter implements BackgroundPainter {
	protected Identifier image;
	protected int xPadding = 4;
	protected int yPadding = 4;
	protected int width = 64;
	protected int height = 64;
	
	public ImageBackgroundPainter(Identifier image, int xPadding, int yPadding, int width, int height) {
		this.image = image;
		this.xPadding = xPadding;
		this.yPadding = yPadding;
		this.width = width;
		this.height = height;
	}
	
	@Override
	public void paintBackground(DrawContext context, int left, int top, WWidget widget) {
		//TODO: Auto-center the image on the widget and possibly add coverage rules to stretch, contain, or cover
		
		var matrices = context.getMatrices();
		matrices.push();
		matrices.translate(left - xPadding, top - yPadding, 0);
		//float px = 1/64f;
		ScreenDrawing.texturedRect(context, 0, 0, width+xPadding+xPadding, height+yPadding+yPadding, image, 0, 0, 1, 1, 0xFF_FFFFFF);
		matrices.pop();
	}

}
