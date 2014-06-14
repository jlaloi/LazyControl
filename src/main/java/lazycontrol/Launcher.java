package main.java.lazycontrol;

import javax.swing.UIManager;

import main.java.lazycontrol.network.Server;
import main.java.lazycontrol.ressources.Factory;

public class Launcher {

	public static void main(String[] args) {

		if (args.length >= 1 && "client".equals(args[0])) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Factory.getServerFrame();
		} else if (args.length >= 4 && "server".equals(args[0])) {
			int port = Integer.valueOf(args[1]);
			boolean allowControl = Boolean.valueOf(args[2]);
			String password = args[3];
			int interlacedPass = args.length > 4 ? Integer.valueOf(args[4]) : 2;
			new Server(port, password, allowControl, interlacedPass).start();
		} else {
			System.out.println("Proper Usage is:\n 1 - client/server\n 2 - port\n 3 - allowControl true/false\n 4 - password\n 5 - interlacedPass\n\nExample:\n java -Xmx256M -jar LazyControl.jar client\n java -Xmx256M -jar LazyControl.jar server 45878 true password 2");
		}

		// new Server(Factory.defaultPort, "",false,2).start();
		// Factory.getServerFrame();
	}

}
