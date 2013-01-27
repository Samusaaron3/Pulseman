package edu.calpoly.csc.pulseman;

import java.io.File;
import java.net.InetAddress;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

import org.lwjgl.LWJGLUtil;
import org.newdawn.slick.AppGameContainer;
import org.newdawn.slick.BasicGame;
import org.newdawn.slick.GameContainer;
import org.newdawn.slick.Graphics;
import org.newdawn.slick.Image;
import org.newdawn.slick.SlickException;
import org.newdawn.slick.Sound;

import edu.calpoly.csc.pulseman.MessageHandler.MessageReceiver;

public class Main extends BasicGame
{
	private volatile static LinkedList<String> messageQueue = new LinkedList<String>();

	public enum GameState
	{
		MENU, GAME, GAMEOVER
	};

	public class AndroidStates
	{
		public static final int NOT_CONNECTED = 0, CONNECTING = 1,
				CONNECTED = 2;
	}

	private static GameState state = GameState.MENU;
	private static int curLevel = -1;
	private static String[] levels =
	{ "res/level1.png", "res/level2.png", "res/level3.png", "res/level4.png",
	  "res/level5.png", "res/level6.png", "res/level7.png", "res/level8.png",
	  "res/level9.png", "res/level10.png"};
	private static final int width = 1280, height = 720;
	private static volatile int androidState = AndroidStates.NOT_CONNECTED;
	Map<GameState, GameInterface> interfaceMap = new HashMap<GameState, GameInterface>();

	Sound debugMusic;
	public static int tVelocity;

	/*private class Heart
	{
		public Image image;
		public float scale;
		public int beat;

		public void init(Image image)
		{
			this.image = image;
			scale = 1;
			beat = 1;
		}

		public void update(GameContainer gc, int delta)
		{
			if(scale > 1.2)
				beat = 0;
			if(scale < 1.0)
				beat = 1;
			if(beat == 1)
			{
				scale += .0005 * delta;
			}
			else
				scale -= .0005 * delta;

		}

		public void render(GameContainer gc, Graphics g)
		{
			image.draw(0, 0, scale);
		}

	}*/


	public Main()
	{
		super("Pulse of Nature");
	}

	public static int getScreenWidth()
	{
		return width;
	}

	public static int getScreenHeight()
	{
		return height;
	}

	public static int getAndroidState()
	{
		return androidState;
	}

	public static void setAndroidConnecting()
	{
		androidState = AndroidStates.CONNECTING;
	}

	public static String nextLevel()
	{
		return levels[++curLevel];
	}

	public static void setState(GameState state)
	{
		Main.state = state;
	}

	@Override
	public void render(GameContainer gc, Graphics g) throws SlickException
	{
		interfaceMap.get(state).render(gc, g);
	}

	@Override
	public void init(GameContainer gc) throws SlickException
	{
		StartMenu menu = new StartMenu();
		menu.init(gc);
		interfaceMap.put(GameState.MENU, menu);

		final GameScreen game = new GameScreen();
		game.init(gc);
		interfaceMap.put(GameState.GAME, game);

		GameOver gameOver = new GameOver();
		gameOver.init(gc);
		interfaceMap.put(GameState.GAMEOVER, gameOver);

		debugMusic = new Sound("res/pulse_of_nature.ogg");
		debugMusic.play();

		MessageHandler.addmessageReceiver(new MessageReceiver()
		{
			@Override
			public void onMessageReceived(String message)
			{
				game.playerTwoTap();
			}

			@Override
			public void onConnectionEstablished(InetAddress client)
			{
				System.out.println("Connection established: " + client.getHostAddress());
				androidState = AndroidStates.CONNECTED;
			}

			@Override
			public void onConnectionLost(InetAddress client)
			{
				if(client != null)
				{
					System.out.println("Connection lost: " + client.getHostAddress());
				}

				androidState = AndroidStates.NOT_CONNECTED;
			}
		});
	}

	public static void main(String[] arg) throws SlickException
	{
		System.setProperty("org.lwjgl.librarypath", new File(new File(System.getProperty("user.dir"), "native"), LWJGLUtil.getPlatformName()).getAbsolutePath());
		System.setProperty("net.java.games.input.librarypath", System.getProperty("org.lwjgl.librarypath"));
		AppGameContainer app = new AppGameContainer(new Main());

		app.setDisplayMode(width, height, false);
		app.setTargetFrameRate(60);
		app.setVSync(true);
		app.start();
	}

	@Override
	public void update(GameContainer gc, int dt) throws SlickException
	{
		interfaceMap.get(state).update(gc, dt);
		if(tVelocity > 0)
			tVelocity -= 0.0001;
	}

	public static LinkedList<String> getMessageQueue()
	{
		return messageQueue;
	}

}
