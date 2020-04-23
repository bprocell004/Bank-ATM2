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
//main class to create the client socket object and call the message method.
public class ATM {

	public static void main(String[] args) {
		client sender = new client("localhost", 4020); //create client object
		sender.sendMessage(); //call send message method.

	}

}
