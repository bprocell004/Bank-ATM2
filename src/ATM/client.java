/****************
 * Name:          Brent Procell
 * Course:		  CIS 3970.O1I
 * Semester: 	  Spring 2019
 * Assignment:	  Lab 12
 * Date started:  5/02/2019
 * Date Finished: 5/05/2019
 * Description:  You are to write a Java program using the following instructions to build a bank-ATM server system with sockets that allows multiple concurrent users who could even be sharing accounts 
 ***************/

package ATM;

	import java.io.ObjectInputStream;
	import java.io.ObjectOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
	import java.net.Socket;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;
import java.util.Arrays;
import java.util.Scanner;

import javax.net.ssl.KeyManager;
import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLServerSocket;
import javax.net.ssl.SSLServerSocketFactory;
import javax.net.ssl.SSLSocket;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;

import transaction.Transaction;
	//client class to represent the ATM socket side and send a transaction object to the server and get a message back that the transcation was complete or not.
	public class client {
		Scanner scnr = new Scanner(System.in);
		private SSLSocket client;			// client socket
		private static String algorithm = "SSL";
		private ObjectInputStream input; //get the input from server
		private ObjectOutputStream output; //send output to server
		
		//constructor to create client socket.
		public client(String ip, int port) {
			try {
				//pw = Lith2ump
				//char [] password = System.console().readPassword("Password: ");
				char [] password = {'L', 'i', 't', 'h', '2', 'u', 'm', 'p'};
				
				//instantiate keystore object with JKS algorithm.
				KeyStore keyS = KeyStore.getInstance("JKS");
				keyS.load(new FileInputStream("jnp4e.keys"), password); //load key file
				
				//instantiate key manager factory with SunX509 algorithm
				KeyManagerFactory keyManFact = KeyManagerFactory.getInstance("SunX509");
				keyManFact.init(keyS, password); 
				KeyManager[] km = keyManFact.getKeyManagers(); //KeyManager array
				
				Arrays.fill(password, '0'); //clear area.
				
				//instantiate trust manager facctory object with SunX509 algorithm
				TrustManagerFactory tmf = TrustManagerFactory.getInstance("SunX509");
				tmf.init(keyS);
				TrustManager[] tm = tmf.getTrustManagers(); //trust manager array
				
				//instantiate ssl context object with SSL
				SSLContext sslContext = SSLContext.getInstance(algorithm);
				sslContext.init(km,  tm, null);
				
				//instantiate ssl socket factory object
				SSLSocketFactory sslSocketFactory = sslContext.getSocketFactory();
				client = (SSLSocket) sslSocketFactory.createSocket(ip, port); //client secure socket.
				
				
			} catch (IOException e) {
				System.out.println("Unable to connect to " + ip + ":" + port);
				e.printStackTrace();
			} catch (UnrecoverableKeyException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyStoreException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (NoSuchAlgorithmException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KeyManagementException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (CertificateException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} 
		}
		
		//method to connect to a server socket, retrieve a message and  send a message to it.
		public void sendMessage() {
			try {
	            this.output = new ObjectOutputStream(client.getOutputStream());
	            this.input = new ObjectInputStream(client.getInputStream());
	           
	          //  while (true) {
	            System.out.println("Enter your account number:");
	            int num = scnr.nextInt();
	            
	            //ask the user for the type of transaction and record it into variable type
				System.out.println("Type (1-deposit, 2-withdraw, 3-balance)?");
				int type = scnr.nextInt();
				
				//if type is 3 amount equals 0 otherwise ask user for amount and store it in variable
				double amount;
				if (type == 3) {
					amount = 0;
				}
				else {
					System.out.println("Amount?");
					amount = scnr.nextDouble();
				}
				
				//new transaction object with user input for arguments then send object to server.
				Transaction sending = new Transaction(type, amount, num);
				this.output.writeObject(sending);
				this.output.flush();
				
				//read in the server's response and print it.
	            String response = (String) input.readObject();
	            System.out.println(response);
	            
	            //close readers and client socket
	            this.input.close();
	            this.output.close();
	            client.close();
	            
	          //  }
	            //throw exception
			} catch(IOException e) {
				System.out.println("Unable to send message.");
				System.out.println(e);
			} catch(ClassNotFoundException e) {
				e.printStackTrace();
			}
		}
	}


