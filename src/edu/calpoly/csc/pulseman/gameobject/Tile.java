package edu.calpoly.csc.pulseman.gameobject;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.geom.Rectangle;
import org.newdawn.slick.geom.Vector2f;

public class Tile extends Collidable {
	private static Image image;
	
	
	static public void init(Image image) {
		Tile.image = image;
	}
	
	public Tile(int x, int y) {
		super(new Rectangle(x, y, image.getWidth(), image.getHeight()));
	}
	
	
	
	@Override
	public void render(GameContainer gc, Graphics g) {
		g.drawImage(image, getHitBox().getX(), getHitBox().getY());
		
	}

	@Override
	public void update(GameContainer gc, int delta) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public boolean isAffectedByPulse() {
		return false;
	}

}
