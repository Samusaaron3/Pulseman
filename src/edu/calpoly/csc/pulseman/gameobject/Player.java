package edu.calpoly.csc.pulseman.gameobject;

import java.util.List;

import org.newdawn.slick.Animation;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Input;
import org.newdawn.slick.geom.Rectangle;

import edu.calpoly.csc.pulseman.Main;
import edu.calpoly.csc.pulseman.World;
import edu.calpoly.csc.pulseman.Main.GameState;

public class Player extends Entity
{
	Direction dir;
	private static Animation anim;
	public final static float PLAYER_SPEED = 0.25f;

	public static void init(Animation animation)
	{
		Player.anim = animation;
	}

	public Player(int x, int y)
	{
		super(new Rectangle(x, y, anim.getWidth(), anim.getHeight()));
		dir = Direction.LEFT;
	}

	@Override
	public void render(GameContainer gc, Graphics g)
	{
		if(dir == Direction.RIGHT)
			g.drawImage(anim.getCurrentFrame(), position.x, position.y);
		else
			g.drawImage(anim.getCurrentFrame(), position.x, position.y, anim.getWidth(), 0, 0, anim.getHeight());

	}

	@Override
	public void update(GameContainer gc, int delta)
	{
		Input input = gc.getInput();
		anim.update(delta);
		if(input.isKeyDown(Input.KEY_A) || input.isKeyDown(Input.KEY_LEFT))
		{
			dir = Direction.LEFT;
			position.x -= PLAYER_SPEED * delta;
		}
		if(input.isKeyDown(Input.KEY_D) || input.isKeyDown(Input.KEY_RIGHT))
		{
			dir = Direction.RIGHT;
			position.x += PLAYER_SPEED * delta;
		}

		if(input.isKeyDown(Input.KEY_SPACE) && floor != null)
		{
			velocity.y -= 0.4f;
		}

		super.update(gc, delta);
	}

	@Override
	protected void handleCollision(Collidable collidable, Rectangle oldBounds)
	{
		if(collidable instanceof Murderer)
		{
			Main.setState(GameState.GAMEOVER);
		}
		else if(collidable instanceof Goal)
		{
			World.getWorld().nextLevel();
		}
		else
		{
			super.handleCollision(collidable, oldBounds);

			List<Enemy> enemies = World.getWorld().getEnemies();
			for(int i = 0; i < enemies.size(); ++i)
			{
				if(bounds.intersects(enemies.get(i).bounds))
				{
					handleCollision(enemies.get(i), oldBounds);
				}
			}
		}
	}

	@Override
	public boolean isAffectedByPulse() {
		return false;
	}
}
