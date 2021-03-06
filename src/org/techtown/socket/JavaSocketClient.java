package org.techtown.socket;

import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

public class JavaSocketClient {

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			int portNumber = 5001;
			Socket sock = new Socket("192.168.101.164", portNumber);
			
			ObjectOutputStream outstream = new ObjectOutputStream(sock.getOutputStream());
			outstream.writeObject("Hello Android Town");
			outstream.flush();
			
			ObjectInputStream instream = new ObjectInputStream(sock.getInputStream());
			System.out.println(instream.readObject());
			sock.close();
			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

}
