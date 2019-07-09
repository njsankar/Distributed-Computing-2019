import java.io.*;
import java.util.InputMismatchException;

/**
 * This module contains the presentaton logic of an Echo Client.
 * @author M. L. Liu
 */
public class EchoClient1 {
    static final String endMessage = "";

    public static void main(String[] args) {
        InputStreamReader is = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(is);

        try {
 
            /*System.out.println("| Welcome to the Client/Server FTP System | " +
                               "\n| Please enter your username | ");
            String username = br.readLine();*/
            
            boolean validUsername = false;           
            
            System.out.println("| Welcome to the Client/Server FTP System | " +
                               "\n| Please enter your username | ");
            String username = br.readLine();  
            
            while ((username.isEmpty())){
                System.out.println("| Invalid Username! Must not be blank!|");
                System.out.println("| Please enter a valid username |");
                username = br.readLine();
            }    
       
            System.out.println("| Please enter a password | ");
            String password = br.readLine();
            
            while ((password.isEmpty())){
                System.out.println("| Invalid Password! Must not be blank! |");
                System.out.println("| Please enter a valid username |");
                password = br.readLine();
            }         

            //Connection strings for client/server connection method
            String hostname = "localhost";
            String portnum = "7";
            
            String menuChoice,echo,response;

            //hostname and port number are sent to the helper method EchoClientHelper1 in the EchoClientHelper1 class.
            //This helps establish the client/server connection and catches an error if a connection fails
            EchoClientHelper1 helper = new EchoClientHelper1(hostname, portnum);
            
            //username and password provided by user get sent to the loginHelper method in EchoClientHelper1 class.
            echo = helper.loginHelper(username,password);
            

            boolean done = false;
		
            
            //---| Client Menu Interface |---//
            while (!done)
            {
                System.out.println("\n| Please select an option |" + 
                                   "\n| u - Upload |" +
                                   "\n| d - Download |" +
                                   "\n| q - Quit |");
                menuChoice = br.readLine( );
            
//---| quit if statement |---//          
//------------------------------------------------------------------------------
                if (menuChoice.equalsIgnoreCase("q"))
                {
                    echo = helper.logoutHelper(endMessage);
                    done = true;
                    helper.done( );
                }
//------------------------------------------------------------------------------
         
                
//---| upload if statement |---//                 
//------------------------------------------------------------------------------
                else if(menuChoice.equalsIgnoreCase("u"))
                {
                    System.out.println("| What file do you wish to Upload? |");
                    String filename = br.readLine();
                    while(filename.isEmpty()){
                        System.out.println("| The Upload file name must not be blank! |");
                        System.out.println("| Please enter a valid file name to Upload! |");
                        filename = br.readLine();
                    }
                    echo = helper.uploadHelper(username,filename);

                }
//------------------------------------------------------------------------------
                

//---| download if statement |---// 
//------------------------------------------------------------------------------
                else if(menuChoice.equalsIgnoreCase("d"))
                {
                    System.out.println("| What file do you with to Download |");
                    String filename = br.readLine();
                    while(filename.isEmpty()){
                        System.out.println("| Download file name must not be blank! |");
                        System.out.println("| Please enter a valid file name to Download! |");
                        filename = br.readLine();
                    }
                    echo = helper.downloadHelper(username,filename);
                }
//------------------------------------------------------------------------------

            } // end while

        } // end try
        catch (Exception ex) {
            ex.printStackTrace( );
        } // end catch
    } //end main
} // end class      
