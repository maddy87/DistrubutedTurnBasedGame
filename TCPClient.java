/**
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 * FileName    : TCPClient.java
 * Description : The program implements the Tic Tac Toe Client Server
 * It Accepts the Client request and runs the TicTacToe Game in an Distributed 
 * Environment
 * 
 * @version  :  TCPClient.java v 1.0  2014/12/08  5:31 AM
 * 
 * @author 	rss2158 (Rajesh Shetty)
 *          vf1739  (Vanessa Fernandes)
 * 
 *=============================================================================
 */

import java.io.*;
import java.net.*;
import java.util.*;


public class TCPClient 
{
	//Creating the Client Socket on which the remaining 
	//request of the game will be played
	Socket sSocket;
	//Buffered Reader that recieves the buffers the requests from the client
	BufferedReader bReadSTEP;
	//PrintWriter to write output to the Output Stream which sends it to the 
	//Client
	PrintWriter pWrite;
	
	///int iPort = 8080;
	Scanner sc ;
	//Integer array board which stores the status 
	//of the board. Initially all values are intitialized 
	//to 11
	int[] iBoard = { 11,11,11,11,11,11,11,11,11};
	//By Default Server Plays X and Gamer plays O
	String sMe = "O";
	String sHim = "X";
	//String Board stores the input of the server
	//and the client 
	String sBoard[] = { "","","","","","","","",""}; 
	public TCPClient()
	{
		
			System.out.println("Connecting the Game Server");
			HowToPlay();
			StartGame();
	}
	
	public TCPClient(int iPort)
	{
		try 
		{
			
			System.out.println("Connecting the Game Server");
			sSocket = new Socket("localhost",iPort);
			System.out.println("Connected Now to Socket " + sSocket); 
			//Writing to Game Server
			//String sRecieve  =  bReadSTEP.readLine();
			//System.out.println(sRecieve);
			StartGame();
		} catch (UnknownHostException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) 
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * Constructor to initialize the host and Port no
	 * @param sHost
	 * @param iPortNo
	 */
	public TCPClient(String sHost, int iPortNo) 
	{
			
			try 
			{
				//Send the Request to the Game Server  and start
				//the game
				System.out.println("Connecting the Game Server");
				sSocket = new Socket(sHost,iPortNo);
				HowToPlay();
				StartGame();
			
			} 
			catch (SocketException e) 
			{
				e.printStackTrace();
			} catch (UnknownHostException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			catch (Exception e) {
				// TODO Auto-generated catch block
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
		
		System.out.println(" \n\n Enjoy the Game \n\n");
		System.out.println("\n\n");
		
	   
	}

	
	/**
	 * Function :DisplayBoard
	 * Description : Displays the status of the Board
	 */
	public void DisplayBoard(int[] iBoard)
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
			    System.out.print(" | "+sBoard[k]+" | ");
			  k++;
			}
			System.out.println();
		}
		System.out.println("\n\n");
	   
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
    	try 
    	{
    		System.out.println(" \n\n *********** STARING THE GAME  *****************\n\n");
    		String sRecieve; //String value of the current valued recieved
    		String sSend = ""; //String value of the current value played
    		int iRecievedTurn;//Stores the value of the current Turn recived
    		int iTurn = 0; // Stores the value of the current Turn played
    		
    		int iCurrentTurn = 0;
    		//Initialize the BufferedReader for Input and PrintWriter Output Stream
    		bReadSTEP = new BufferedReader(new InputStreamReader(sSocket.getInputStream()));
    		pWrite = new PrintWriter(new OutputStreamWriter(sSocket.getOutputStream()));
    		//Scan the Input 
    		sc =  new Scanner(System.in);
    		System.out.println("\n\nServer is now playing ....\n");
    		//While the board is not full
    		while(iTurn<9)
    		{
    			//System.out.print("Your Turn : ");
    			sRecieve =  bReadSTEP.readLine();
    			System.out.println("Computer Played : " + sRecieve);
    			iRecievedTurn = Integer.parseInt(sRecieve);
    			//If Termination code sent 
    			if(iRecievedTurn > 9)
    			{
    				//Terminate Game
    				iRecievedTurn = iRecievedTurn%100;
    	    	    iBoard[iRecievedTurn-1] = iRecievedTurn;
    	  	        sBoard[iRecievedTurn-1] = sHim;
    	  	        DisplayBoard(iBoard);
    	  	        System.out.println("Okay You Win. Congrats ");
    				break;
    			}
    			else
    			{
    			   iBoard[iRecievedTurn-1] = iRecievedTurn;
    			   sBoard[iRecievedTurn-1] = sHim;
    			   DisplayBoard(iBoard);
    			}
    			//If No Winner then print a draw
    			 if(iTurn == 9)
			      {
			    	  System.out.println("Game is now a draw");
			    	  break;
			      }
			
    			  
    			//Take input from client
    			System.out.print("Your Turn : ");
    			//sSend = sc.nextLine();
    			//Validate Input
    			boolean bStatus = false;
    		    while(!bStatus)
    		    {
    			      try
    			      {
    			    	sSend = sc.nextLine();  
    			        iCurrentTurn = Integer.parseInt(sSend);
    			        bStatus = true;
    			      }
    			      catch(NumberFormatException e)
    			      {
    			    	  System.out.println("\n Input entered is not valid. Please Try Again");
    			    	  bStatus = false;
    			      }
    		      }
    			//System.out.println("TCPClient : " + sSend);
    			while(iCurrentTurn > 9 )
    		      {
    		    	  System.out.print(" Invalid Move (Dont you know how to play TicTacToe) : ");
    			      sSend = sc.nextLine();
    			      iCurrentTurn = Integer.parseInt(sSend);
    		      }
    		      while(iBoard[iCurrentTurn-1]!=11 )
    		      {
    		    	  System.out.print(" Invalid Move (Dont you know how to play TicTacToe) : ");
    			      sSend = sc.nextLine();
    			      iCurrentTurn = Integer.parseInt(sSend);
    		      }
    		     
    		      iBoard[iCurrentTurn-1] = iCurrentTurn;
    		      sBoard[iCurrentTurn-1] = sMe;
    		      DisplayBoard(iBoard);
    		      //Check if anybody won.
    		      if(CheckRows() || CheckCols() || CheckDig())
    		      { 
    		    	  //Then Client has WON
    		    	  pWrite.println(sSend);
    	    		  pWrite.flush();
    		    	  System.out.println("I win !!!!");
    		    	  break;
    		      }
    		      else
    		      {
    		    	  //Write Data
    		    	  pWrite.println(sSend);
    	    		  pWrite.flush();
    		      }
    		      //Increment Turn
    			  iTurn++;
    			  System.out.println("\n\nServer is now playing ....\n");
    		}
    		
    	} 
    	catch (IOException e) 
    	{
			e.printStackTrace();
		}
    }
	
    
	public static void main(String[] args) 
	{
        //If No Arguments Passed
		int iPortNo = 8080;
		Scanner sc = new Scanner(System.in);
		//Gather the details of the host Name and Port No.
		System.out.print(" \n\nEnter the Host Name of Game Server :  ");
		String sHost = sc.nextLine();
		System.out.print(" \n \n Enter the port you want to connect : ");
		String sIP = sc.nextLine();
		try
		{
			iPortNo = Integer.parseInt(sIP);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
		}
		
		TCPClient gObj = new TCPClient(sHost,iPortNo);
		
	}

}
