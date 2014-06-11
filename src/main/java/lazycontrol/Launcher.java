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
		} else if (args.length >= 3 && "server".equals(args[0])) {
			int port = Integer.valueOf(args[1]);
			boolean allowControl = Boolean.valueOf(args[2]);
			String password = args.length >= 4 ? args[3] : "";
			new Server(port, password, allowControl).start();
		} else {
			System.out.println("Proper Usage is:\n 1 - client/server\n 2 - port\n 3 - true/false\n 4 - password\n\nExample:\n java -Xmx256M -jar LazyControl.jar client\n java -Xmx256M -jar LazyControl.jar server 45878 true password");
		}

		// new Server(Factory.defaultPort, "123456").start();
		// Factory.getServerFrame();
	}

}
