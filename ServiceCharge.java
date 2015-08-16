/*
 * Austin Cathey
 * Computer Science 3
 * Assignment 1
 */

package assignment2;

/**
 * <p>
 * The idea to treat each service charge as an object allowed for greater
 * control of my code, to promote scalability.
 * </p>
 * <p>
 * Also, treating the service charges this way gives me much greater
 * control of when a service charge should be applied.
 * </p>
 *
 * @author Austin Cathey
 * @since September 12th, 2009
 * @see CheckingAccount
 * @see Transaction
 */
public class ServiceCharge
{
    /*-----------*
     * Constants *
     *-----------*/

    /**
     * This is the service charge code for when the account balance
     * falls under $500.00.
     */
    public static final int ID_UNDER_500 = 0;

    /**
     * This is the service charge code for a check.
     */
    public static final int ID_CHECK = 1;

    /**
     * This is the service charge code for a deposit.
     */
    public static final int ID_DEPOSIT = 2;

    /**
     * This is the service charge code for when the account balance
     * is negative.
     */
    public static final int ID_NEGATIVE_BALANCE = 3;

    // Private constants
    private static final int SC_ID_MIN = 0;
    private static final int SC_ID_MAX = 3;
    private static final long CHARGES[] = {500, 15, 10, 1000};
    private static final String CHARGE_TYPES[] = {"Balance Under $500.00", "Check", "Deposit", "Negative Balance"};

    /*
     * Variables
     */

    // Private member variables
    private int transNumber;			// The number of this transaction.
    private int chargeType;             // The type of service charge
    private CheckingAccount account;    // The account associated with it.
    
    /*--------------*
     * Constructors *
     *--------------*/

    /**
     * Primary constructor.
     *
     * @param ca The CheckingAccount associated with this service charge.
     * @param charge The type of service charge.
     * @throws IllegalArgumentException
     */
    public ServiceCharge(CheckingAccount ca, int charge) throws IllegalArgumentException
    {
    	// Make sure the CheckingAccount is not null.
    	if (ca == null)
    	{
    		throw new IllegalArgumentException("This service charge must have a CheckingAccount associated with it.");
    	}
    	
        // Throw an exception if the service charge is not a valid type.
    	else if (!isValidServiceCharge(charge))
        {
        	throw new IllegalArgumentException("Invalid service charge type.");
        }
        
        // Initialize the fields.
    	account = ca;
        chargeType = charge;
    }

    /*---------------*
     * Class methods *
     *---------------*/

    /**
     * A method containing rules that determine whether or not a service charge
     * is valid.
     *
     * @param sc The service charge code.
     * @return <code>true</code> if the service charge code is valid.
     *         <code>false</code> if the service charge code is not valid.
     */
    public static boolean isValidServiceCharge(int sc)
    { // Checks to see if a service charge is a valid one.
        return (sc >= SC_ID_MIN) && (sc <= SC_ID_MAX);
    }

    /**
     * Posts a service charge to a CheckingAccount object.
     *
     * @param ca The CheckingAccount to post the charge to.
     * @param chargeType The service charge code.
     */
    public static void postToAccount(CheckingAccount ca, int chargeType)
    {
        ca.postServiceChargeAsPending(new ServiceCharge(ca, chargeType));
    }

    /*------------------*
     * Instance methods *
     *------------------*/

    /**
     * This method actually applies the service charge to its
     * associated CheckingAccount.
     */
    public void apply()
    {
        // Display the message
        Message.out.print("Service Charge: " + CHARGE_TYPES[chargeType] + " --- charge ");
        Message.out.print(CheckingAccount.toDollarString(CHARGES[chargeType]));
        Message.out.println();

        // Add the service charge to the list of transactions.
        transNumber = account.getTransCount();
        account.addTrans(this);
        
        // Apply the service charge.
        account.addServiceCharge(CHARGES[chargeType]);
    }

    /**
     * Returns the type of service charge that the calling object is.
     *
     * @return The type of service charge.
     */
    public int getChargeType()
    {
        return chargeType;
    }

    /**
     * Returns the CheckingAccount object associated with this service charge.
     *
     * @return The CheckingAccount associated with this service charge.
     */
    public CheckingAccount getAccount()
    {
        return account;
    }
    
    /**
     * Returns the string representation of this object.
     * 
     * @return The string representation of this object.
     */
    public String toString()
    {
    	return String.format(
    			Statics.TOSTRING_FORMAT,
    			Integer.toString(transNumber),
    			"svc.chrg.",
    			CheckingAccount.toDollarString(CHARGES[chargeType]));
    }
}