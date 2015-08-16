/*
 * Austin Cathey
 * Computer Science 3
 * Assignment 1
 */

package assignment2;

/**
 * The purpose of this class was to separate code that was strictly relevant to
 * a transaction separate from the CheckingAccount class.
 *
 * @author Austin Cathey
 * @since September 12th, 2009
 * @see CheckingAccount
 * @see ServiceCharge
 */
public class Transaction
{
	/**
	 * This is the transaction code to exit the program.
	 */
	public static final int ID_EXIT = 0;
	
	/**
	 * This is the transaction code for a check.  It is intentionally
	 * the same value as <code>ServiceCharge.ID_CHECK</code> to make
	 * the internal implementation easier to code.
	 */
	public static final int ID_CHECK = ServiceCharge.ID_CHECK;
	
	/**
	 * This is the transaction code for a deposit.  It is intentionally
	 * the same value as <code>ServiceCharge.ID_DEPOSIT</code> to make
	 * the internal implementation easier to code.
	 */
	public static final int ID_DEPOSIT = ServiceCharge.ID_DEPOSIT;
	
	// Range of valid transaction codess
	private static final int T_ID_MIN = 0;
	private static final int T_ID_MAX = 2;
	
	// Names of transaction types
	private static final String TRANS_TYPES[] = {"Exit Program", "Check", "Deposit"};
	
	// Instance variables.
	private int transNumber;			// The transaction number relative to a given checking account.
	private int transID;				// The transaction ID.
	private long transAmt;				// The amount of the transaction, in pennies.
	
	/*--------------*
	 * Constructors *
	 *--------------*/
	
	// Constructor
	public Transaction(int transNumber, int transID, long transAmtInPennies) throws IllegalArgumentException
	{
		// Make sure a valid transaction ID was passed.
		if (!isValidTransaction(transID))
		{
			throw new IllegalArgumentException("Invalid transaction ID of " + transID + " passed.");
		}
		
		// Properly adjust the transaction amount if this is an exit transaction.
		if (transID == ID_EXIT)
		{
			transAmtInPennies = 0;
		}
		
		// Make sure the transaction amount is valid if this is not an exit transaction.
		else if (transAmtInPennies <= 0)
		{
			throw new IllegalArgumentException("Transaction amount must be positive.");
		}
		
		
		// Set up the information about the transaction.
		this.transID = transID;
		this.transAmt = transAmtInPennies;
		this.transNumber = transNumber;
	}
	
	/*----------------*
	 * Static Methods *
	 *----------------*/
	
	public static boolean isTransaction(Object o, int tid)
	{
		return (o instanceof Transaction) && ((Transaction)o).getTransID() == tid;
	}
	
	/**
	 * Checks to see if a transaction ID is valid.
	 *
	 * @param transID The transaction ID to check.
	 * @return <code>true</code> if the ID is valid, <code>false</code> if not.
	 */
	public static boolean isValidTransaction(int transID)
	{ // Checks to see if a given transaction code is valid.
	    return (transID >= T_ID_MIN) && (transID <= T_ID_MAX);
	}
	
	private static boolean toStringLongFormat_isOn = true;
	
	/**
	 * Determines how toString() should represent the object.
	 * 
	 * @param on true means to display the type of transaction,
	 *           false means do not display it.
	 */
	public static void toStringLongFormat(boolean on)
	{
		toStringLongFormat_isOn = on;
	}
	
	/**
	 * Handles a new transaction.
	 *
	 * @param ca The CheckingAccount object that this transaction will act on.
	 * @param transID The ID of the transaction.
	 * @param amountInPennies The amount in pennies of the transaction.  If this number is
	 *                        negative, a warning message will be displayed.
	 * @throws IllegalArgumentException
	 */
	public static void newTransaction(CheckingAccount ca, int transID, long amountInPennies) throws IllegalArgumentException
	{
		Transaction t;
		
		// Clear the message.
		Message.out.clear();
		
		// Create the transaction.
		try
		{
			t = new Transaction(ca.getTransCount(), transID, amountInPennies);
			ca.addTrans(t);
		}
		catch (IllegalArgumentException ex)
		{
			// TODO: display an error message if needed.
			throw ex;	// For now, rethrow the exception.
		}
		
		// Make the proper adjustment to the balance.
		if (transID == Transaction.ID_CHECK)
		{
			ca.addBalance(-amountInPennies);
		}
		else if (transID == Transaction.ID_DEPOSIT)
		{
			ca.addBalance(amountInPennies);
		}
		
		// Display the current balance.
		Message.out.print("Current Balance: ");
		Message.out.println(CheckingAccount.toDollarString(ca.getBalance()));
	
		// User wants to be done with this checking account.
		if (transID == ID_EXIT)
		{
		    ca.done();              // User is done.  Stop the transaction loop.
		}
		
		// Else, make the appropriate service charge.
		else
		{
			// Apply any service charges that are posted, and get
			// ready to display their corresponding messages.
			ca.applyServiceCharges();
			Message.out.printPostedMessages();
			
			// Apply the relevant service charge.
			ServiceCharge.postToAccount(ca, transID);
			
			// Apply service charges and display.
			ca.applyServiceCharges();
			Message.out.printPostedMessages();
		}
	
		// Show the total service charges
		Message.out.print("Total service charge: ");
		Message.out.println(CheckingAccount.toDollarString(ca.getServiceCharge()));
		
		// Display any posted messages.  This is primarily to handle
		// the messages that are posted when the CheckingAccount.done()
		// instance method is ran.  Otherwise, it does nothing.
		Message.out.printPostedMessages();
	}
	
	/*------------------*
	 * Instance Methods *
	 *------------------*/
	
	/**
	 * Gets the number of this transaction.
	 * 
	 * @return The transaction number.
	 */
	public int getTransNumber()
	{
		return transNumber;
	}
	
	/**
	 * Gets the ID for this transaction.
	 * 
	 * @return The transaction ID.
	 */
	public int getTransID()
	{
		return transID;
	}
	
	/**
	 * Gets the transaction amount in pennies.
	 * 
	 * @return The transaction amount in pennies.
	 */
	public long getTransAmountInPennies()
	{
		return transAmt;
	}
	
	/**
	 * Gets the transaction amount in dollars.
	 * 
	 * @return The transaction amount in dollars.
	 * 
	 */
	public double getTransAmountInDollars()
	{
		return (double)getTransAmountInPennies() / 100.0;
	}
	
	/**
	 * The string representation of this object.
	 * 
	 * @return The string representation of this object.
	 */
	public String toString()
	{
		// Use long format if it is on.
		if (toStringLongFormat_isOn)
		{
			return String.format(
					Statics.TOSTRING_FORMAT,
					Integer.toString(transNumber),
					TRANS_TYPES[transID],
					Statics.dollarFormat.format(getTransAmountInDollars())
					);
		}
		
		// Otherwise, display short format.
		else
		{
			return String.format(
					Statics.TOSTRING_FORMAT_SHORT,
					Integer.toString(transNumber),
					Statics.dollarFormat.format(getTransAmountInDollars())
					);
		}
	}
}