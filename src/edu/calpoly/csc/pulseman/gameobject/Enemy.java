package edu.calpoly.csc.pulseman.gameobject;

import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;

public class Enemy extends Entity implements Murderer
{
	private static Image image;

	public static void init(Image image)
	{
		Enemy.image = image;
	}

	public Enemy(int x, int y)
	{
		super(new Rectangle(x, y, image.getWidth(), image.getHeight()));
	}

	public void render(GameContainer gc, Graphics g)
	{
		g.drawImage(image, position.x, position.y);
	}

	public void update(GameContainer gc, int delta)
	{
		super.update(gc, delta);
	}
}
