package com.rekoe.ssh;

import java.io.File;
import java.io.IOException;

import ch.ethz.ssh2.Connection;
import ch.ethz.ssh2.SCPClient;

public class Scp {
	public static void main(String[] args) {
		String hostname = "xxx.xxxx.xxxx.xxxx";
		String username = "root";
		File keyfile = new File("~/.ssh/id_dsa"); // or ""
		try {
			/* Create a connection instance */
			Connection conn = new Connection(hostname, 59231);
			/* Now connect */
			conn.connect();
			boolean isAuthenticated = conn.authenticateWithPublicKey(username, keyfile, "");
			if (isAuthenticated == false)
				throw new IOException("Authentication failed.");
			/* Create a session */
			SCPClient scpClient = conn.createSCPClient();
			scpClient.put("d:/xxx.json", "/root/");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}