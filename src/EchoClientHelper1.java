import java.net.*;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This class is a module which provides the application logic
 * for an Echo client using connectionless datagram socket.
 * @author M. L. Liu
 */
public class EchoClientHelper1 {
    private MyClientDatagramSocket mySocket;
    private InetAddress serverHost;
    private int serverPort;
    
    

/*---| EchoClienthelper1 method - Used for ensuring proper connection
  ---| between server and client |---*/
//------------------------------------------------------------------------------
   EchoClientHelper1(String hostname, String portnum)
        throws SocketException, UnknownHostException { 
            this.serverHost = InetAddress.getByName(hostname);
            this.serverPort = Integer.parseInt(portnum);
            // instantiates a datagram socket for both sending
            // and receiving data
            this.mySocket = new MyClientDatagramSocket(); 
   }
//------------------------------------------------------------------------------
   
   
   
/*---| loginHelper method - Used for helping user login to program |---*/
//------------------------------------------------------------------------------	
   public String loginHelper(String username, String password)
        throws SocketException, IOException {                                                                                 
        String serverResponse = "100 | Login" + "-" + username + "-" + password;
        //serverResponse string for login to be sent to the server
        mySocket.sendMessage( serverHost, serverPort, serverResponse);
	// now receive the echo
        serverResponse = mySocket.receiveMessage();
        return serverResponse;
   }
//------------------------------------------------------------------------------
   
  
   
/*---| logoutHelper method - Used for helping user log out of program |---*/
//------------------------------------------------------------------------------   
   public String logoutHelper(String q)
        throws SocketException, IOException {
            String serverResponse = "200 | Logout" + q;
            //serverResponse string for logout to be sent to the server
            mySocket.sendMessage( serverHost, serverPort, serverResponse);
            // now receive the echo
            serverResponse = mySocket.receiveMessage();
            return serverResponse;
   }
//------------------------------------------------------------------------------
  

   
/*---| uploadHelper method - Used for helping user upload a file |---*/
//------------------------------------------------------------------------------   
    public String uploadHelper(String username,String filename)
        throws SocketException, IOException {
            String serverPath = "FTPFolder//";
            Path path = Paths.get(serverPath + filename);
            byte[] data = Files.readAllBytes(path);
            String byteDataString = new String(data);
            String serverResponse = 
            "300 | Upload" + "-" + username + "-" + filename + "-" + byteDataString;
            //serverResponse string for upload to be sent to the server
            mySocket.sendMessage( serverHost, serverPort, serverResponse);
	   // now receive the echo
            serverResponse = mySocket.receiveMessage();
            return serverResponse;
   }
//------------------------------------------------------------------------------
    
    
    
/*---| downloadHelper method - Used for helping user download a file |---*/
//------------------------------------------------------------------------------   
   public String downloadHelper(String username, String filename)
        throws SocketException, IOException {
            String serverResponse = "400 | Download" + "-" + username + "-" + filename;
            FileOutputStream fos = new FileOutputStream("FTPFolder//" + filename);
            fos.write(serverResponse.getBytes());
            fos.close();
            //serverResponse string for download to be sent to the server
            mySocket.sendMessage( serverHost, serverPort, serverResponse);
            // now receive the echo
            serverResponse = mySocket.receiveMessage();
            return serverResponse;
   }
//------------------------------------------------------------------------------
   
   
   
/*---| done method - Called when user logs out of program |---*/
//------------------------------------------------------------------------------
   //Helper method that is called to end the client side operation.
   //This is called when the user logs out.
   //Server will continue to run, giving opportunity for another user to log in.
   public void done() throws SocketException {
        System.out.println("Exit program");
        mySocket.close( );
   }
//------------------------------------------------------------------------------


}
