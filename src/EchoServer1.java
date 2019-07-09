import java.io.*;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * This module contains the application logic of an echo server
 * which uses a connectionless datagram socket for interprocess 
 * communication.
 * A command-line argument is required to specify the server port.
 * @author M. L. Liu
 */

public class EchoServer1 {
    public static void main(String[] args) {
        int serverPort = 7; // default port - Used if no port is specified

        String response = "";
        if (args.length == 1 )
            serverPort = Integer.parseInt(args[0]);

        
        try {
            // instantiates a datagram socket for both sending
            // and receiving data
            MyServerDatagramSocket mySocket = new MyServerDatagramSocket(serverPort);
            System.out.println("Echo server ready.");
            while (true) {  // forever loop
                DatagramMessage request = mySocket.receiveMessageAndSender();
                System.out.println("Request received");
                String message = request.getMessage();
                System.out.println("Message received: "+ message);

                //get the first 3 character of message
                //Only get the first 3 characters of the server message - This is the unique code i.e 100, 201 etc.
                String serverMessage = message.substring(0,3);
                //System.out.println(strMsg);
                
                //In this section, there is a check to see what type of unique code is found.
                //Different operations are going to take place for each code.
                //100 - Login
                //200 - Logout
                //300 - Upload
                //400 - Download
                
                //---| Login |---//
                if(serverMessage.contains("100"))
                {
                    //Gets the full server response i.e. 100 | Login - username - password
                    String getMsg = message;

                    //An array which find the first dash (-) in the server response and splits the response at each dash
                    String[] messageSections = getMsg.split("-");

                    //splits the response into three parts
                    String codeSect = messageSections[0]; //The 3 digit code i.e. 100, 200, 300
                    String usernameSect = messageSections[1]; //The username of the logged in user
                    String passwordSect = messageSections[2]; //The password of the logged in user

                    //Creates a folder named with the logged in users username and places it within the following
                    //directory: FTPFolder/Users directory
                    File userFolder = new File("FTPFolder/Users/" + usernameSect);

                    boolean createdYet = false; //A boolean variable to be used in the folder check statement
					
                    //This if statement is used to check if a folder has already been created in the users name.
                    //If this is the case, a new folder will not be created, as this would overwrite the folder that was
                    //already created at the first login
                    if(!userFolder.exists()&& !userFolder.isDirectory())
                    {
                        try{
                            System.out.println("101 | You have Successfully Logged in as " + usernameSect);
                            userFolder.mkdirs(); //create the directory
                            createdYet = true; //Folder has now been created. Set to true
			}
			catch(Exception e){
                            System.out.println("102 | You failed to Log in");
                            e.printStackTrace();
			}
                    }
                    else if(userFolder.exists()&& userFolder.isDirectory())
                        System.out.println("101 | Welcome back " + usernameSect);
                }

                //---| Logout |---//
                //if statement used for logging out
                else if(serverMessage.contains("200"))
                {   
                try{
                    System.out.println("201 | You have Successfully Logged Out");    
                }
                catch(Exception e){
                    System.out.println("202 | You have failed to log out");
                }
                    
                }

                //---| Upload |---//
                //If statement used for reading in the name of the file the user wishes to upload to the FTP server.
                //This file is created using a FileOutputStream method and added to the directory:
                //FTPFolder/Users/
                else if(serverMessage.contains("300"))
                {
                    String getMsg = message;

                    //An array which find the first dash (-) in the server response and splits the response at each dash
                    //i.e. String serverResponse = "300 | Upload" + "-" + username + "-" + filename + "-" + byteDataString;
                    String[] messageSections = getMsg.split("-");

                    
                    //splits the response into three parts
                    String codeSect = messageSections[0]; //The 3 digit code i.e. 100, 200, 300
                    String usernameSect = messageSections[1]; //The username of the logged in user
                    String fileNameSect = messageSections[2]; //The name of the file that the user has entered.
                    
                    File fileToUpload = new File("FTPFolder/" + fileNameSect);
                    
                    try{
                        if(fileToUpload.exists()){
                        String uploadSection = messageSections[2];
                        //Create a file named as the filename provided by the user
                        FileOutputStream fos = new FileOutputStream("FTPFolder/Users/" + usernameSect + "/" + fileNameSect);
                        fos.write(uploadSection.getBytes()); //Writes the text from the uploadSection and places it in the textfile.
                        fos.close(); //Finishes the file creation process
                        //System.out.println(pathToFile.getAbsolutePath());
                        System.out.println("301 | The file: " + fileNameSect + ", has been uploaded successfully");
                        }
                        else if(!fileToUpload.exists()){
                            System.out.println("303 | The file: " + fileNameSect + ", does not exist. Please try another file");
                        }
                    }
                    catch(NoSuchFileException e){
                        System.out.println("302 | Upload failed");
                    }
                }    
                
                //---| Download |---//
                if(serverMessage.contains("400"))
                {
                    try{
                        System.out.println("401 | File Downloaded Successfully");
                    }
                    catch(Exception e){
                        System.out.println("402 | File Download failed");
                    }
                    
                    }

                mySocket.sendMessage(request.getAddress( ),
                        request.getPort( ), message);
            } //end while
        } // end try
        catch (Exception ex) {
            ex.printStackTrace( );
        } // end catch
    } //end main
} // end class