//package idk;

import java.awt.*;
import java.awt.MenuBar;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ConnectException;
import java.net.Socket;

import javax.swing.*;

/**
 * Chat Room Client implementation
 * 
 * @author Charles Zhu
 *
 */
public class ChatRoomClient implements ActionListener, Runnable {

	// INSTANCE variables (allocated in the "program object" in Dynamic Storage)
	Socket s;
	ObjectOutputStream oos;
	ObjectInputStream ois;
	String newLine = System.lineSeparator();
	int fontSize = 20;
	int maxFontSize = 50;
	int minFontSize = 5;
	boolean loadedFromMain = false;

	// Chat Window
	JFrame chatWindow = new JFrame();
	JPanel topPanel = new JPanel();
	JPanel bottomPanel = new JPanel();
	JLabel whosInLabel = new JLabel("Who's in the chat room:");
	JButton sendPublicButton = new JButton("Send To All");
	JTextField whosInTextField = new JTextField(48);
	JTextField errMsgTextField = new JTextField("Error messages will show here.");
	JTextArea sendChatArea = new JTextArea();
	JTextArea receiveChatArea = new JTextArea();
	JScrollPane sendScrollPane = new JScrollPane(sendChatArea);
	JScrollPane receiveScrollPane = new JScrollPane(receiveChatArea);
	JSplitPane chatSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, true, sendScrollPane, receiveScrollPane);

	// Menus
	MenuBar menuBar = new MenuBar();
	Menu fontMenu = new Menu("Font");
	Menu screenMenu = new Menu("Screen Orientation");
	MenuItem biggerFontMenuItem = new MenuItem("Bigger");
	MenuItem smallerFontMenuItem = new MenuItem("Smaller");
	MenuItem horizontalOrientationMenuItem = new MenuItem("Horizontal");
	MenuItem verticalOrientationMenuItem = new MenuItem("Vertical");

	public static void main(String[] args) {
		System.out.println("Charles Zhu");
		if (args.length != 3) {
			throw new IllegalArgumentException(
					"Invalid number of parameters entered. Please enter them and restart program.");
		}
		ChatRoomClient crc = null;
		try {
			crc = new ChatRoomClient(args[0], args[1], args[2]);
			crc.loadedFromMain = true; // used at termination
		} catch (Exception e) {
			System.out.println(e); // print the exception object as the error message
			return; // can't continue if can't load the program!
		}

	}

	public ChatRoomClient() {

	}

	public ChatRoomClient(String hostAddress, String clientName, String password) throws Exception {
		if (hostAddress.contains(" ") || clientName.contains(" ") || password.contains(" "))
			throw new IllegalArgumentException("Parameters may not contain blanks."); // also returns.
		System.out.println("Connecting to the chat room server at " + hostAddress + " on port 2222.");
		try {
			this.s = new Socket(hostAddress, 2222); // connect to chat server
		} catch (ConnectException e) {
			throw new IllegalArgumentException(
					"Connection to server failed. Please restart the client and try again. \n"
							+ "Server reject reason: " + e.getMessage());
		}
		System.out.println("Connected to the chat server!");
		oos = new ObjectOutputStream(s.getOutputStream()); // oos is declared as an instance variable.
		oos.writeObject(clientName + " " + password); // send the "join message"
		ois = new ObjectInputStream(s.getInputStream()); // ois is declared as an instance variable.
		String reply = (String) ois.readObject(); // wait for server response
		if (reply.startsWith("Welcome"))
			System.out.println("Join was successful!");
		else
			throw new IllegalArgumentException(
					"Join of " + clientName + " with password " + password + " was not successful.");

		topPanel.add(whosInLabel);
		topPanel.add(whosInTextField);

		bottomPanel.add(sendPublicButton);
		bottomPanel.add(errMsgTextField);

		chatWindow.getContentPane().add(topPanel, "North");
		chatWindow.getContentPane().add(chatSplitPane, "Center");
		chatWindow.getContentPane().add(bottomPanel, "South");
		chatWindow.setTitle(clientName + "'s CHAT ROOM    (Close this window to leave the chat room.)");
		sendPublicButton.setBackground(Color.green);
		whosInLabel.setForeground(Color.blue);
		receiveChatArea.setLineWrap(true); // cause long text added to be properly
		receiveChatArea.setWrapStyleWord(true);// "wrapped" to the next line.
		receiveChatArea.setEditable(false); // keep user from changing the output area!
		errMsgTextField.setEditable(false); // keep user from changing the error message area!
		whosInTextField.setEditable(false); // keep user from changing the whos-in-the-chatroom list!
		chatSplitPane.setDividerLocation(0.5); // half-screen point
		sendChatArea.setText("ENTER a message to send HERE, then push the send button below.");
		receiveChatArea.setText("VIEW received chat messages HERE (including the ones you sent)." + newLine
				+ "The bar separating the IN and OUT areas can be moved.");
		sendPublicButton.addActionListener(this); // so sendPublicButton can call us!

		// Add menus to ChatWindow
		chatWindow.setMenuBar(menuBar);
		menuBar.add(fontMenu);
		menuBar.add(screenMenu);
		fontMenu.add(biggerFontMenuItem);
		biggerFontMenuItem.addActionListener(this);
		fontMenu.add(smallerFontMenuItem);
		smallerFontMenuItem.addActionListener(this);
		screenMenu.add(verticalOrientationMenuItem);
		verticalOrientationMenuItem.addActionListener(this);
		screenMenu.add(horizontalOrientationMenuItem);
		horizontalOrientationMenuItem.addActionListener(this);
		UIManager.setLookAndFeel("javax.swing.plaf.metal.MetalLookAndFeel");
		chatWindow.setSize(1000, 400); // width,height
		chatWindow.setLocation(400, 100); // x,y (x is "to the right of the left margin", y is "down-from-the-top")
		chatWindow.setVisible(true); // show it
		Thread t = new Thread(this); // make an app thread (to do the receive function).
		t.start(); // Thread will begin execution in run() of the "this" object.
	}

	@Override
	public void run() // all received messages are from the server and,
	{ // after the join, they are all received here.
		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
		}
		try {
			if (loadedFromMain)
				chatWindow.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // shuts down JVM!
			else
				chatWindow.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);// garbage collect window
			while (true) // "capture" the calling thread to be our receive-forever thread,
			{
				String incomingChatMessage = (String) ois.readObject();
				if (incomingChatMessage.startsWith("[")) {
					incomingChatMessage = incomingChatMessage.substring(1); // drop leading "["
					whosInTextField.setText(incomingChatMessage);
					continue; // back to loop top to read next message.
				}
				receiveChatArea.append(newLine + incomingChatMessage);
				// auto-scroll textArea to bottom line so the last message will be visible.
				receiveChatArea.setCaretPosition(receiveChatArea.getDocument().getLength()); // really?
			}
		} catch (Exception e) {
			// show error message to user
			errMsgTextField.setBackground(Color.pink); // this will get their attention!
			errMsgTextField.setText("CONNECTION TO THE CHAT ROOM SERVER HAS FAILED! " + newLine
					+ " You must close this chatWindow and then restart the client to reconnect to the server to continue.");
			// disable the GUI
			sendChatArea.setEditable(false); // keep user from trying to send any more messages.
			sendPublicButton.setEnabled(false); // stop button pushing
		}
		// receive thread, now out of the loop, will return and be terminated.
	}

	@Override
	public void actionPerformed(ActionEvent ae) {
		errMsgTextField.setText(""); // clear any error message
		errMsgTextField.setBackground(Color.white); // and remove highlight
		if (ae.getSource() == sendPublicButton) {
			String chatMessageToSend = sendChatArea.getText().trim(); // remove leading/trailing blanks.
			if (chatMessageToSend.length() == 0) // a blank message!
			{
				errMsgTextField.setText("No message entered to send.");
				errMsgTextField.setBackground(Color.pink); // highlight to get attention
				return; // return button's thread to the button.
			}
			try {
				oos.writeObject(chatMessageToSend);
				sendChatArea.setText(""); // clear input area
			} catch (Exception e) // Uh-oh - the server is down!
			{
				errMsgTextField.setText("Connection to the chat server has failed.");
				errMsgTextField.setBackground(Color.pink); // highlight to get attention
				sendChatArea.setEditable(false); // keep user from entering more messages to send.
				sendPublicButton.setEnabled(false); // disable sendButton
			}

			return; // return the button's thread to the JButton program
		} // end of processing block for sendPrivateButton

		if (ae.getSource() == biggerFontMenuItem) { // This button increases the font size in the sendChatArea and the
													// receiveChatArea
			if (fontSize < maxFontSize)
				fontSize += 1;
			{
				sendChatArea.setFont(new Font("default", Font.BOLD, fontSize));
				receiveChatArea.setFont(new Font("default", Font.BOLD, fontSize));
			}
			return;
		}

		if (ae.getSource() == smallerFontMenuItem) { // This button reduces the font size in the sendChatArea and the
														// receiveChatArea
			if (fontSize > minFontSize)
				fontSize -= 1;
			{
				sendChatArea.setFont(new Font("default", Font.BOLD, fontSize));
				receiveChatArea.setFont(new Font("default", Font.BOLD, fontSize));
			}
			return;
		}

		if (ae.getSource() == horizontalOrientationMenuItem) {
			// This button changes the bar that separates the sendChatArea and the
			// receiveChatArea from vertical to horizontal.
			chatSplitPane.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
			chatSplitPane.setDividerLocation((double) 0.5); // half-screen point
			return;
		}

		if (ae.getSource() == verticalOrientationMenuItem) {
			// This button changes the bar that separates the sendChatArea and the
			// receiveChatArea from horizontal to vertical.

			chatSplitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
			chatSplitPane.setDividerLocation((double) 0.5); // half-screen point
			return;
		}

	}

}
