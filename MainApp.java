/*
 * Austin Cathey
 * Computer Science 3
 * Assignment 1
 */

package assignment2;

// Imports
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;

/**
 * The main application class.
 *
 * @author Austin Cathey
 */
public final class MainApp extends JFrame
{
	// Class variables
	CheckingAccount ca;
	
	/**
	 * This class is responsible for setting up the panel.
	 * @author Austin Cathey
	 */
	private class ApplicationPanel extends JPanel implements ActionListener
	{
		private JLabel lblMessage;
		private JRadioButton rdoEnterTransaction, rdoListAllTransactions, rdoListAllChecks, rdoListAllDeposits;
		
		/**
		 * This is where the magic happens.
		 */
		public ApplicationPanel()
		{
		}
		
		public void actionPerformed(ActionEvent event)
		{
		}
		
		/*
		 * I steal a lot of Microsoft's .NET naming conventions here.
		 */
		
		public void rdoEnterTransaction_onClick(JRadioButton j)
		{
		}
		
		public void rdoListAllTransactions_onClick(JRadioButton j)
		{
		}
		
		public void rdoListAllChecks_onClick(JRadioButton j)
		{
		}
		
		public void rdoListAllDeposits_onClick(JRadioButton j)
		{
		}
	}
	
	/**
	 * The constructor responsible for creating a new window.
	 * Declare it private so no one else can instantiate this class.
	 */
	private MainApp()
	{
		// Set the title and default close operation.
		super("Checking Account actions");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		// Create a new CheckingAccount object
		ca = new CheckingAccount();
		
		// If we have a valid initial balance, then create the window.
		if (ca.inputInitialBalance())
		{
			setTitle("Checking Account actions");
			setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);			
		}
		
		// Otherwise, display a message.
		else
		{
			new Message().println("Have a nice day.").show();
		}
	}
	
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args)
    {
    	// Get out of static context.
    	new MainApp();
    }
}
