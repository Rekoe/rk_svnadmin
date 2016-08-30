package com.rekoe.ssh;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.ConnectionInfo;
import ch.ethz.ssh2.Session;
import ch.ethz.ssh2.StreamGobbler;

public class PublicKeyAuthentication {
	public static void main(String[] args) {
		String hostname = "xxx.xxx.xxx.xxx";
		String username = "root";

		File keyfile = new File("~/.ssh/id_dsa"); // or "~/.ssh/id_dsa"
		try {
			/* Create a connection instance */
			Connection conn = new Connection(hostname, 59231);
			/* Now connect */
			ConnectionInfo info = conn.connect();
			System.out.println(info.keyExchangeAlgorithm);
			/* Authenticate */
			boolean isAuthenticated = conn.authenticateWithPublicKey(username, keyfile, "");
			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");
			/* Create a session */
			Session sess = conn.openSession();
			sess.execCommand("ls");
			InputStream stdout = new StreamGobbler(sess.getStdout());
			BufferedReader br = new BufferedReader(new InputStreamReader(stdout));
			System.out.println("Here is some information about the remote host:");
			while (true) {
				String line = br.readLine();
				if (line == null)
					break;
				System.out.println(line);
			}
			/* Close this session */
			sess.close();
			/* Close the connection */
			conn.close();
			br.close();
		} catch (IOException e) {
			e.printStackTrace(System.err);
			System.exit(2);
		}
	}
}
