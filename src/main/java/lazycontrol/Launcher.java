package main.java.lazycontrol;

import javax.swing.UIManager;

import main.java.lazycontrol.network.Server;
import main.java.lazycontrol.ressources.Factory;

public class Launcher {

	public enum Parameter {
		port, allowControl, password, interlacedPass, threadSleep;

		public String toString() {
			return "-" + name() + ":";
		}

		public String getValue(String arg) {
			return arg.substring(toString().length());
		}
	}

	public static void main(String[] args) {

		if (args.length > 0 && "client".equals(args[0])) {
			try {
				UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
			} catch (Exception e) {
				e.printStackTrace();
			}
			Factory.getServerFrame();
		} else if (args.length > 0 && "server".equals(args[0])) {
			int port = Factory.defaultPort;
			boolean allowControl = false;
			String password = "";
			int interlacedPass = 2;
			int threadSleep = 25;
			for (int i = 1; i < args.length; i++) {
				String arg = args[i];
				if (arg.startsWith(Parameter.allowControl.toString())) {
					allowControl = Boolean.valueOf(Parameter.allowControl.getValue(arg));
				} else if (arg.startsWith(Parameter.port.toString())) {
					port = Integer.valueOf(Parameter.port.getValue(arg));
				} else if (arg.startsWith(Parameter.interlacedPass.toString())) {
					interlacedPass = Integer.valueOf(Parameter.interlacedPass.getValue(arg));
				} else if (arg.startsWith(Parameter.password.toString())) {
					password = Parameter.password.getValue(arg);
				} else if (arg.startsWith(Parameter.threadSleep.toString())) {
					threadSleep = Integer.valueOf(Parameter.threadSleep.getValue(arg));
				}
			}
			new Server(port, password, allowControl, interlacedPass, threadSleep).start();
		} else {
			System.out.println("Proper Usage is:");
			System.out.println("client/server\n" + Parameter.port + "<Port number>\n" + Parameter.allowControl + "<true/false>" + "\n" + Parameter.password + "<password>\n" + Parameter.interlacedPass + "<Number of pass>\n" + Parameter.threadSleep + "<Sleep ms>");
			System.out.println("\nExample:");
			System.out.println(" java -Xmx256M -jar LazyControl.jar client");
			System.out.println(" java -Xmx256M -jar LazyControl.jar server");
			System.out.println(" java -Xmx256M -jar LazyControl.jar server " + Parameter.port + Factory.defaultPort + " " + Parameter.allowControl + "false " + Parameter.password + "password " + Parameter.interlacedPass + "4 " + Parameter.threadSleep + "100");
		}

		// new Server(Factory.defaultPort, "", false, 2, 25).start();
		// Factory.getServerFrame();
	}

}
