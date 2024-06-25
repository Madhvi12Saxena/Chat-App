import java.net.*;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.SwingConstants;

import java.awt.BorderLayout;
import java.awt.Font;
import java.awt.Image;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.*;


class Server extends JFrame 
{   
    ServerSocket server;
    Socket socket;

    BufferedReader br;
    PrintWriter out;

    // Declare Components
    private JLabel heading = new JLabel("Server Area");
    private JTextArea messageArea = new JTextArea();
    private JTextField messageInput = new JTextField();
    private Font font = new Font("Roboto", Font.PLAIN, 20);


    // Constructor..
    public Server()
    {
        try {
            server = new ServerSocket(7777);
            System.out.println("Server is ready to accept connection");
            System.out.println("Waiting...");
            socket = server.accept();

            br = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            out = new PrintWriter(socket.getOutputStream());

            createGUI();
            handleEvents();

            startReading();
            // startWriting();


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleEvents()
    {
        messageInput.addKeyListener(new KeyListener() {
            
            @Override
            public void keyTyped(KeyEvent e) {
                // TODO Auto-generated method stub

            }

            @Override
            public void keyPressed(KeyEvent e) {
                // TODO Auto-generated method stub
                
            }

            @Override
            public void keyReleased(KeyEvent e) {
                // TODO Auto-generated method stub
                // System.out.println("key released "+ e.getKeyCode());
                if(e.getKeyCode() == 10)
                {
                    // System.out.println("You have pressed enter button");
                    String contentToSend = messageInput.getText();
                    messageArea.append("Me: " + contentToSend + "\n");
                    out.println(contentToSend);
                    out.flush();
                    messageInput.setText("");
                    messageInput.requestFocus();
                }
            }

        });
    }

    private void createGUI()
    {
        //GUI Code...

        this.setTitle("Slient Messager[END]");
        this.setSize(600, 600);
        this.setLocationRelativeTo(null);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // Coding for Component
        heading.setFont(font);
        messageArea.setFont(font);
        messageInput.setFont(font);
        //  heading.setIcon(new ImageIcon("clogo.jpg"));

        // Load the image
        ImageIcon icon = new ImageIcon("clogo.jpg");

        // Set the desired size
        Image image = icon.getImage();
        Image scaledImage = image.getScaledInstance(60, 60, Image.SCALE_SMOOTH);

        // Create a new ImageIcon with the scaled image
        ImageIcon scaledIcon = new ImageIcon(scaledImage);

        // Set the scaled icon to the component
        heading.setIcon(scaledIcon);



        heading.setHorizontalTextPosition(SwingConstants.CENTER);
        heading.setVerticalTextPosition(SwingConstants.BOTTOM);
        heading.setHorizontalAlignment(SwingConstants.CENTER);
        heading.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        messageArea.setEditable(false);
        messageInput.setHorizontalAlignment(SwingConstants.CENTER);

        // Frame ka layout set karenge
        this.setLayout(new BorderLayout());

        // Adding the components to frame
        this.add(heading, BorderLayout.NORTH);
        JScrollPane jScrollPane = new JScrollPane(messageArea);
        this.add(jScrollPane, BorderLayout.CENTER);
        this.add(messageInput, BorderLayout.SOUTH);

        this.setVisible(true);
    }


    public void startReading()
    {
        // thread - read karke deta rahega
        Runnable r1 = () -> {

            System.out.println("Reader Started..");

            try{
            
            while(true)
            {
             
                String msg = br.readLine();
                if(msg.equals("exit"))
                {
                    System.out.println("Client terminated the chat");
                    JOptionPane.showMessageDialog(this, "Server Terminated the Chat");
                    messageInput.setEnabled(false);
                    socket.close();

                    break;
                }
                // System.out.println("Client: "+msg);
                messageArea.append("Client: "+ msg + "\n");
                
            }

            }catch(Exception e)
            {
                // e.printStackTrace();
                System.out.println("Connection closed");
            }
        };

        new Thread(r1).start();
    }

    public void startWriting()
    {
        // thread - data ko user se lega and then send karega client tak
        Runnable r2 = () -> {
            System.out.println("Writer Started..");

            try{

            while (!socket.isClosed())
            {
               
                    BufferedReader br1 = new BufferedReader(new InputStreamReader(System.in));
                    String content = br1.readLine();

                    
                    out.println(content);
                    out.flush();

                    if(content.equals("exit"))
                    {
                        socket.close();
                        break;
                    }
               
            }

            }catch(Exception e)
            {
                // e.printStackTrace();
                System.out.println("Connection closed");
            }

        };
        
        new Thread(r2).start();
    }

    public static void main (String[] args){
        System.out.println("This is server..going to start server");
        new Server();
    }
}
