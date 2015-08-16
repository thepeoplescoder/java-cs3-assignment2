/*
 * Austin Cathey
 * Computer Science 3
 * Assignment 1
 */

package assignment2;

// Imports
import java.text.NumberFormat;
import java.util.*;

/**
 * @author Austin Cathey
 * @since September 12th, 2009
 * @see Transaction
 * @see ServiceCharge
 */
@SuppressWarnings("unchecked")
public class CheckingAccount
{
    // Private member variables
    private long balanceInPennies;
    private long totalServiceChargesInPennies;
    private boolean done;
    private ServiceCharge under500Charge = null;
    private ArrayList<ServiceCharge> postedServiceChargesAsPending = new ArrayList<ServiceCharge>();
    
    // Use an untyped array list to carry every transaction.
    private int transCount = 0;
    private ArrayList transList = new ArrayList();

    // Private static final variables
    private static final NumberFormat dollarFormat = NumberFormat.getCurrencyInstance();

    // Public constants

    /**
     * This is the limit that the CheckingAccount balance must never go
     * under to avoid the monthly service charge.
     */
    public static final long AMOUNT_TO_AVOID_MONTHLY_SERVICE_CHARGE = 50000l;

    /**
     * If the checking account balance goes under this amount,
     * a warning message will be displayed on every transaction.
     */
    public static final long WARNING_LIMIT = 5000l;

    /*--------------*
     * Constructors *
     *--------------*/

    /**
     * Main constructor.
     *
     * @param balanceInPennies The initial balance in pennies.
     * @param serviceChargesInPennies The initial service charges in pennies.
     */
    public CheckingAccount(long balanceInPennies, long serviceChargesInPennies)
    {
        this.balanceInPennies = balanceInPennies;
        this.totalServiceChargesInPennies = serviceChargesInPennies;
        this.done = false;
    }

    /**
     * Default constructor.
     *
     * This constructor creates a CheckingAccount objects with all fields
     * set to zero.
     */
    public CheckingAccount()
    { // Default constructor.  Sets balance and service charges to zero.
        this(0, 0);
    }

    /*----------------*
     * Static Methods *
     *----------------*/

    /**
     * Converts a dollar amount to pennies.
     *
     * @param amount The dollar amount to convert.
     * @return The dollar amount, in pennies.
     */
    public static long toPennies(double amount)
    {
        return (long)(amount * 100);
    }

    /**
     * Converts a given number of pennies to a dollar amount.
     *
     * @param amountInPennies The number of pennies.
     * @return The amount in pennies, converted to dollars.
     */
    public static double toDollars(long amountInPennies)
    {
        return (double)amountInPennies / 100.0;
    }

    /**
     * Converts a number of pennies, to a string, representing its dollar amount.
     * 
     * @param amountInPennies The amount in pennies.
     * @return A string representing a dollar amount.
     */
    public static String toDollarString(long amountInPennies)
    {
        return dollarFormat.format(toDollars(amountInPennies));
    }

    /*------------------*
     * Instance methods *
     *------------------*/

    /**
     * Allows the user to input the initial balance of the checking account.
     *
     * @return <code>true</code> if an initial balance was entered, otherwise
     *         <code>false</code> if one was not, or if the user pressed Cancel/Escape.
     */
    public boolean inputInitialBalance()
    {
        Double initialBalance;

        // Get the initial balance.
        initialBalance = Input.nextCurrency("Enter initial balance (ESC or blank to cancel):");
        if (initialBalance == null)
        {
            done();
            return false;
        }
        else
        {
            setBalance(CheckingAccount.toPennies(initialBalance));
            return true;
        }
    }

    /**
     * Posts a service charge to this account.
     * Please note that service charges are not applied immediately.
     * Service charges are applied in the order in which they are posted.
     *
     * @param sc The service charge to be posted.  It will only be posted if it is
     *           linked to this account.
     * @return If the service charge was posted, it returns <code>sc</code>. Otherwise,
     *         if the charge was not posted, it returns <code>null</code>.
     * @see CheckingAccount#applyServiceCharges()
     * @see CheckingAccount#numPostedServiceChargesAsPending()
     */
    public ServiceCharge postServiceChargeAsPending(ServiceCharge sc)
    { // Posts a service charge to be applied later.

        // Do not post the service charge if it is not linked to this account.
        if (sc.getAccount() != this)
        {
            return null;
        }

        // Handle the special case for the under $500 service charge.
        if (sc.getChargeType() == ServiceCharge.ID_UNDER_500)
        {
            // Do not post the under $500 charge if it has already been posted.
            if (under500Charge != null)
            {
                return null;
            }

            // Otherwise, we got here, so identify the service charge.
            under500Charge = sc;
        }

        // Post the service charge and return it.
        postedServiceChargesAsPending.add(sc);
        return sc;
    }

    /**
     * Returns the number of service charges that are currently posted.
     *
     * @return The number of service charges currently posted.
     * @see CheckingAccount#postServiceChargeAsPending(assignment1.ServiceCharge)
     * @see CheckingAccount#applyServiceCharges()
     */
    public int numPostedServiceChargesAsPending()
    { // Returns the number of posted service charges.
        return postedServiceChargesAsPending.size();
    }

    /**
     * Applies all service charges, and removes them from the queue.
     * Service charges are applied in the same order which they are posted.
     *
     * @see CheckingAccount#postServiceChargeAsPending(assignment1.ServiceCharge)
     * @see CheckingAccount#numPostedServiceChargesAsPending()
     */
    public void applyServiceCharges()
    { // Applies all posted service charges in the order in which they were posted.

        // Apply the charges
        for (ServiceCharge charge : postedServiceChargesAsPending)
        {
            charge.apply();
        }

        // Clear all posted service charges.
        postedServiceChargesAsPending.clear();
    }

    /**
     * Sets the current balance.  This method handles all internal service charges.
     *
     * @param amountInPennies The amount of the balance, in pennies.
     * @see CheckingAccount#getBalance()
     * @see CheckingAccount#addBalance(long)
     */
    public void setBalance(final long amountInPennies)
    { // Sets the balance in pennies.

        // Do nothing if we are not changing the account balance.
        if (amountInPennies == balanceInPennies)
        {
            return;
        }

        // Only set a service charge for when the balance DROPS under $500.
        // This will not be charged if the initial balance IS under 500, so,
        // in order for this charge to occur, the balance has to previously be
        // $500, and it has to THEN drop under $500.  This charge is only taken
        // once "for the month."
        if (balanceInPennies >= AMOUNT_TO_AVOID_MONTHLY_SERVICE_CHARGE
                && amountInPennies < AMOUNT_TO_AVOID_MONTHLY_SERVICE_CHARGE)
        {
            // If the user has not been charged, then charge them.
            // This method handles the details accordingly.
            postServiceChargeAsPending(new ServiceCharge(this, ServiceCharge.ID_UNDER_500));
        }

        // If the balance is under $50, then display a warning message.
        if (amountInPennies < WARNING_LIMIT)
        {
            Message.out.postln("\nWARNING: Your balance is under " + CheckingAccount.toDollarString(WARNING_LIMIT) + " - Please monitor your account carefully.\n");
        }

        // If the balance is negative, make a service charge.
        if (amountInPennies < 0)
        {
            postServiceChargeAsPending(new ServiceCharge(this, ServiceCharge.ID_NEGATIVE_BALANCE));
        }

        // Set the new balance.
        balanceInPennies = amountInPennies;
    }

    /**
     * Gets the balance in pennies.
     *
     * @return The current balance, in pennies.
     * @see CheckingAccount#setBalance(long)
     * @see CheckingAccount#addBalance(long)
     */
    public long getBalance()
    { // Returns the balance in pennies.
        return balanceInPennies;
    }

    /**
     * Adds an amount, in pennies, to the current balance.
     *
     * @param amountInPennies The number of pennies to add to the current balance.
     * @see CheckingAccount#setBalance(long)
     * @see CheckingAccount#getBalance()
     */
    public void addBalance(long amountInPennies)
    { // Adds to or subrtracts from the balance.

        // I'm doing it this way on purpose to ensure common code is ran.
        setBalance(getBalance() + amountInPennies);
    }

    /**
     * Gets the total amount of service charges in pennies.
     *
     * @return The total service charges, in pennies.
     * @see CheckingAccount#addServiceCharge(long)
     */
    public long getServiceCharge()
    { // Returns the total of the service charges in pennies.
        return totalServiceChargesInPennies;
    }

    /**
     * Adds a given amount, in pennies, to the total service charges.
     * 
     * @param chargeInPennies The amount to add.
     * @see CheckingAccount#getServiceCharge()
     */
    public void addServiceCharge(long chargeInPennies)
    { // Adds a service charge.
        totalServiceChargesInPennies += chargeInPennies;
    }

    /**
     * The final balance, after service charges have been calculated.
     *
     * @return The current balance minus the total service charges.
     * @see CheckingAccount#getBalance()
     * @see CheckingAccount#getServiceCharge()
     */
    public long getFinalBalance()
    { // Returns the final balance after service charges.
        return getBalance() - getServiceCharge();
    }

    /**
     * Returns whether or not this checking account is done processing transactions.
     * 
     * @return <code>true</code> if this account is done processing transactions,
     *         otherwise, it returns <code>false</code>.
     */
    public boolean isDone()
    {
        return done;
    }

    /**
     * Notifies the calling CheckingAccount object to no longer handle any more
     * transactions.
     */
    public void done()
    {

        // Use the method for correctness,
        // use the variable for speed.
        if (!isDone())
        {
            done = true;
            
            // Post this message to be displayed later.
            Message.out.post("Final balance: ");
            Message.out.postln(CheckingAccount.toDollarString(this.getFinalBalance()));
        }
    }

    /**
     * Prompts the user for a transaction type and amount, and then processes it.
     * If a transaction of type <code>Transaction.ID_EXIT</code> is retrieved during
     * this call, transactions will not stop until the next call.
     *
     * @return <code>true</code> if a loop for processing transactions should continue.
     *         <code>false</code> if a loop for processing transactions should terminate.
     */
    public boolean getTransaction()
    {
        Integer transID = null;
        Double transAmount = null;

        // If we are done, then do not process any transactions.
        if (isDone())
        {
            return false;
        }

        // Get the transaction ID.
        while (transID == null || !Transaction.isValidTransaction(transID))
        {
            transID = Input.nextInteger(
                    "Enter transaction type.\n" +
                    Transaction.ID_EXIT + ") Exit\n" +
                    Transaction.ID_CHECK + ") Write a check\n" +
                    Transaction.ID_DEPOSIT + ") Make a deposit\n"
                    );
            
        }

        // If we are going to exit the program, then the transaction amount does not matter.
        if (transID == Transaction.ID_EXIT)
        {
            // Use a positive number to keep any errors from triggering.
            transAmount = 1.0;
        }

        // Get the transaction amount.
        while (transAmount == null)
        {
            transAmount = Input.nextCurrency("Enter transaction amount:");

            // Make sure the transaction amount is positive only.
            if (transAmount != null && transAmount <= 0.0)
            {
                (new Message()).println("Please enter a positive value.").show();
                transAmount = null;
            }
        }

        // Make the transaction.  If the EXIT transaction was made during this call,
        // it will not be recognized until the next call to getTransaction().
        Transaction.newTransaction(this, transID, CheckingAccount.toPennies(transAmount));
        return true;
    }
    
    /**
     * The most basic loop for handling transactions.  It is coded as:<br /><br />
     *
     * <pre>
     * while (this.getTransaction())
     * {
     *    // Display any messages relevant to the transaction.
     * }
     * </pre>
     */
    public void basicTransactionLoop()
    {
        while (getTransaction())
        {
        	Message.out.show();
        }
    }
    
    /**
     * Handles a single transaction and displays its corresponding message.
     * 
     * @return <code>true</code> if there are more transactions to process.<br />
     *         <code>false</code> if there are no more transactions to process.
     */
    public boolean basicTransaction()
    {
    	boolean b = getTransaction();
    	if (b)
    	{
    		Message.out.show();
    	}
    	else
    	{
    		Message.out.clear().println("There are no more transactions to process.").show();
    	}
    	return b;
    }
    
    /**
     * Adds a transaction to the list.
     * 
     * @param transaction The transaction to add.  It must either be of type Transaction
     *                    or of type ServiceCharge.
     */
    public void addTrans(Object ot) throws IllegalArgumentException
    {
    	// Throw an exception on an invalid transaction type.
    	if (!(ot instanceof Transaction) && !(ot instanceof ServiceCharge))
    	{
    		throw new IllegalArgumentException("ot must either be of type Transaction or ServiceCharge");
    	}
    	
    	// Do not add any exit transactions.
    	else if ((ot instanceof Transaction) && ((Transaction)ot).getTransID() == Transaction.ID_EXIT)
    	{
    		return;
    	}
    	
    	// Add the transaction to the list.
    	transList.add(ot);
    	transCount++;					// Increment the number of transactions.
    }
    
    /**
     * Gets a transaction from the list.
     * 
     * @param index The index of the transaction to get.
     * @return A transaction if in range, null if out of range.
     */
    public Object getTrans(int index)
    {
    	// Return a transaction as long as it is in range.
    	if (index >= 0 && index < transCount)
    	{
    		return transList.get(index);
    	}
    	
    	// Return null on out of range.
    	return null;
    }
    
    /**
     * Gets the number of transactions stored.
     * 
     * @return The number of transactions stored.
     */
    public int getTransCount()
    {
    	return transCount;
    }
    
    /**
     * Gets a string containing a list of all transactions.
     * 
     * @return A string containing all transactions with title and header information.
     */
    public String allTransactionsString()
    {
    	String s;
    	
    	// Turn on long format.
    	Transaction.toStringLongFormat(true);
    	
    	// Title and header
    	s = "List all Transactions:\n\n";
    	s += String.format(Statics.TOSTRING_FORMAT, "ID", "Type", "Amount");
    	
    	// Transaction information.
    	for (Object o : transList)
    	{
    		s += "\n" + o.toString();
    	}
    	
    	// Return the newly created string.
    	return s;
    }
    
    /**
     * Gets a string containing all of the checks.
     * 
     * @return A string containing all checks with title and header information.
     */
    public String allChecksString()
    {
    	String s;
    	
    	// Turn off long format.
    	Transaction.toStringLongFormat(false);
    	
    	// Title and header
    	s = "Checks Cashed:\n\n";
    	s += String.format(Statics.TOSTRING_FORMAT_SHORT, "ID", "Amount");
    	
    	// Transaction information.
    	for (Object o : transList)
    	{
    		if (Transaction.isTransaction(o, Transaction.ID_CHECK))
    		{
    			s += "\n" + o.toString();
    		}
    	}
    	
    	// Return the newly created string.
    	return s;
    }
    
    /**
     * Gets a string containing all deposits.
     * 
     * @return A string containing all deposits, with title and header information.
     */
    public String allDepositsString()
    {
    	String s;
    	
    	// Turn off long format.
    	Transaction.toStringLongFormat(false);
    	
    	// Title and header
    	s = "Deposits made:\n\n";
    	s += String.format(Statics.TOSTRING_FORMAT_SHORT, "ID", "Amount");
    	
    	// Transaction information.
    	for (Object o : transList)
    	{
    		if (Transaction.isTransaction(o, Transaction.ID_DEPOSIT))
    		{
    			s += "\n" + o.toString();
    		}
    	}
    	
    	// Return the newly created string.
    	return s;
    }
}
