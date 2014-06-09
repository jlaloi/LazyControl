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
		} else if (args.length >= 2 && "server".equals(args[0])) {
			int port = Integer.valueOf(args[1]);
			String password = args.length >= 3 ? args[2] : "";
			new Server(port, password).start();
		} else {
			System.out.println("Proper Usage is:\n 1 - client/server\n 2 - port\n 3 - password\n\nExample:\n java -Xmx256M -jar LazyControl.jar client\n java -Xmx256M -jar LazyControl.jar server 45878 password");
		}

		// Server server = new Server(Factory.defaultPort, "123456");
		// server.start();
		// Factory.getServerFrame();
	}

}
