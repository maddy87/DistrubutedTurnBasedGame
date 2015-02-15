/**
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * FileName    : TCPTicTacToe.java
 * Description : The program implements the Tic Tac Toe Game Server
 * It Accepts the Client request and runs the Tic Tac Game in an Distributed 
 * Environment
 * 
 * @version  :  TCPTicTacToe.java v 1.0  2014/12/08  5:31 AM
 * 
 * @author 	rss2158 (Rajesh Shetty)
 *          vf1739  (Vanessa Fernandes)
 * 
 *=============================================================================
 */

import java.net.*;
import java.util.*;
import java.io.*;

public class TCPTicTacToe 
{
//Creating the Server Socket to accept Client Requests
static ServerSocket sServer;
//Creating the Client Socket on which the remaining 
//request of the game will be played
static Socket sSocket;
//Declaring an default port in case no argumnets were passed
static final int iDefaultPort = 8080;
//Maximum Turns can be 9 in a Tic Tac Game
final int iMax = 9;
//Buffered Reader that recieves the buffers the requests from the client
BufferedReader bRecieve;
//PrintWriter to write output to the Output Stream which sends it to the 
//Client
PrintWriter pSend;
//Integer array board which stores the status 
//of the board. Initially all values are intitialized 
//to 11
int[] iBoard = { 11,11,11,11,11,11,11,11,11};
//Scanner to take input 
Scanner sc;
//By Default Server Plays X
String sMe= "X";
//By Default Client plays 0
String sHim = "O";
//String Board stores the input of the server
//and the client 
String sBoard[] = { "","","","","","","","",""}; 
	
    /**
     * Constructor to OPEN the server port to 
     * start accepting connections
     */
	public TCPTicTacToe()
	{
		
	  try 
	  {
		sServer = new ServerSocket(iDefaultPort);
		System.out.println(" \n\n TicTacToe Game Server Started on port " + iDefaultPort);
		System.out.println(" \n\n Awaiting Gamer Request");
		//Waiting till a Gamer has joined.
		while(true)
		{
			//When Client Request recieved
			
			sSocket = sServer.accept();
			System.out.println(" Gamer is NOW Connected \n\n");
			HowToPlay();
			System.out.println("Let the Games Begin");
			break;
		}
		StartGame();
      } 
	  catch (IOException e) 
	  {
		System.err.println("Port No. Not available. Maybe the Server is already running on the port : " +iDefaultPort );
	  }
	  
	}
	/**
	 * Function :ChekRows
	 * Description : Check if any row has complete 'X's or 'Os
	 * @return TRUE if a row has complete X's or 'O's
	 */
	public boolean CheckRows()
	{
		return ((sBoard[0]+sBoard[1]+sBoard[2]).equals(sMe+sMe+sMe) ||
				(sBoard[3]+sBoard[4]+sBoard[5]).equals(sMe+sMe+sMe) ||
				(sBoard[6]+sBoard[7]+sBoard[8]).equals(sMe+sMe+sMe)
				 );
	}
	
	/**
	 * Function :ChekCols
	 * Description : Check if any column has complete 'X's or 'Os
	 * @return TRUE if a column has complete X's or 'O's
	 */
	public boolean CheckCols()
	{
		return ((sBoard[0]+sBoard[3]+sBoard[6]).equals(sMe+sMe+sMe) ||
				(sBoard[1]+sBoard[4]+sBoard[7]).equals(sMe+sMe+sMe) ||
				(sBoard[2]+sBoard[5]+sBoard[8]).equals(sMe+sMe+sMe)
				 );
	}
	
	/**
	 * Function :ChekDig()
	 * Description : Check if any diagonal has complete 'X's or 'Os
	 * @return TRUE if a diagonal has complete X's or 'O's
	 */
	public boolean CheckDig()
	{
		return ((sBoard[0]+sBoard[4]+sBoard[8]).equals(sMe+sMe+sMe) ||
				(sBoard[2]+sBoard[4]+sBoard[6]).equals(sMe+sMe+sMe)
				 );
	}
	
	/**
	 * Function : StartGame
	 * Description: The function starts the game moving each turn at
	 * a time and terminates when either of the players win or its a 
	 * draw.
	 */
	public void StartGame()
	{
	  
	 
 	   int iTurn = 0; //Keeps the count of the current no. of Turns
	   int iCurrentTurn = 0; // Stores the value of the current Turn played 
	   int iRecievedTurn = 0; //Stores the value of the current Turn recived
	   String sSend; //String value of the current value played
	   String sRecieved; //String value of the current valued recieved
	   
	   try 
	   {
		
		   System.out.println(" \n\n *********** STARING THE GAME  *****************\n\n");
		   //Start
		   bRecieve = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
		   pSend = new PrintWriter(new OutputStreamWriter(sSocket.getOutputStream()));
		   sc = new Scanner(System.in);
		   //Run till a MAX number of Moves have been played 
		   while(iTurn<9)
		   {
			   //Server plays the first move
			   System.out.print("Your Turn : ");
			   sSend = sc.next();
			   boolean bStatus = false;
			   //Validate if the Input was a number or not.
			   while(!bStatus)
			   {
				   try
				   {
					   iCurrentTurn = Integer.parseInt(sSend);
					   bStatus = true;
				   }
				   catch(NumberFormatException e)
				   {
					   System.out.print("\n Input entered is not valid. Please Try Again : ");
					   sSend = sc.next();
					   bStatus = false;
				   }
			   }
			   //If Invalid Value entered then enter the next one
			   while(iCurrentTurn > 9 || iCurrentTurn <= 0)
			   {
				   System.out.print("\n  Invalid Move : Out of Bounds  (Dont you know how to play TicTacToe) : ");
				   sSend = sc.next();
				   iCurrentTurn = Integer.parseInt(sSend);
			   }
			   //If Already Played move then dont accept
			   while(iBoard[iCurrentTurn-1]!=11 )
			   {
				   System.out.print("\n  Invalid Move : Already Filled (Dont you know how to play TicTacToe) : ");
				   sSend = sc.next();
				   iCurrentTurn = Integer.parseInt(sSend);
			   }
			   
			   //Update the iBoard with the valid move
			   iBoard[iCurrentTurn-1] = iCurrentTurn;
			   //Update String move with the current X or O move
			   sBoard[iCurrentTurn-1] = sMe;
			   //Display the current status of the Board
			   DisplayBoard();
			   //Check if after the current move was any ROW or COL complete
			   if(CheckRows() || CheckCols() || CheckDig())
			   { 
				   //If a Row or Col or Diagonal was complete
				   //Then Send the termination code to the Game and close
				   sSend = "100" + sSend;
				   pSend.println(sSend);
				   pSend.flush();
				   System.out.println("\n\n I win !!!!");
				   break;
			   }
			   //If not update the arrays and carry on
			   else
			   {
				   
				   pSend.println(sSend);
				   pSend.flush();
				}
			   
			   //Increment Turn as one Turn was played
			   iTurn++;

			   //If the board is now full then 
			   //The game was a draw. Print it and Quit.
			   if(iTurn >= 9)
			   {
				   System.out.println("\n\nGame is now a draw\n\n");
				   break;
			   }

			   System.out.println("\n\nClient is now playing ....\n");
			   //Wait Till you Gamer has played a step.
			   sRecieved =bRecieve.readLine();

			   //Print the current output of the client step
			   System.out.println("\nClient Played :  " + sRecieved+"\n");
			   iRecievedTurn = Integer.parseInt(sRecieved);
			   //Validate the received input if it was a termination code
			   if(iRecievedTurn >  9)
			   {
				   iRecievedTurn = iRecievedTurn%100;
				   iBoard[iRecievedTurn-1] = iRecievedTurn;
				   sBoard[iRecievedTurn-1] = sHim;
				   //Display Board After Updation
				   DisplayBoard();
				   System.out.println("Okay Okay Congrats ");
				   break;
			   }
			   else
			   {
				   iBoard[iRecievedTurn-1] = iRecievedTurn;
				   sBoard[iRecievedTurn-1] = sHim;
				 //Display Board After Updation
				   DisplayBoard();
			   }

			   //Increment the no. of Turns.
			   iTurn++;
		   }

	   }
	   catch (IOException e) 
	   {
		   e.printStackTrace();
	   }
	}
	
 
	/**
	 * Function : How To Play 
	 * Description : Displays the instructions on how to play
	 */
	public void HowToPlay()
	{
		System.out.println("\n\n ~~~~~~~~~~~~~~~Instructions on How to Play ~~~~~~~~~~~~~~~\n\n");
		System.out.println(" \n * Please enter the number to place the O on the board. Simple *\n");
		
		int k = 1;
		for(int i = 0;i<3;i++)
		{
			System.out.println();
			for(int j = 0;j<3;j++)
			{
			   System.out.print(" | "+k+" | ");
			  k++;
			}
			System.out.println();
		}
		
		System.out.println(" \n\n Enjoy the Game !!! \n\n");
		System.out.println("\n\n");
		
	   
	}


	/**
	 * Function :DisplayBoard
	 * Description : Displays the status of the Board
	 */
	private void DisplayBoard()
	{
		int k = 0;
		for(int i = 0;i<3;i++)
		{
			System.out.println();
			for(int j = 0;j<3;j++)
			{
			  if(iBoard[k] ==11)
			  {
			      System.out.print(" | . | ");
			  }
			  else
			  {
			    //System.out.print(" | " + iBoard[k++] + " | ");
			    System.out.print(" | "+sBoard[k]+" | ");
			  }
			  k++;
			  
			}
			System.out.println();
		}
		k=0;
		System.out.println("\n\n");
	}


	/**
	 * 
	 * @param iPort
	 */
	public TCPTicTacToe(int iPort)
	{
		try 
		  {
			sServer = new ServerSocket(iPort);
			System.out.println("Game Server Started on port " + iPort);
	      } 
		  catch (IOException e) 
		  {
			System.err.println("Port No. Not available" );
		  }
	}
	
	//Main Function that creates an instance of the TicTacTow Class
	public static void main(String[] args) 
	{
		//If No Arguments Passed
		TCPTicTacToe gObj;
		if(args.length == 0)
		{
		   //Create an instance of the same
           gObj =  new TCPTicTacToe();
		}
		else if(args.length > 0 && args.length > 1)
		{
			int iPort = Integer.parseInt(args[1].toString());
			gObj = new TCPTicTacToe(iPort);
			
		}
	}

}
