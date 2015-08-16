package assignment2;

// Imports
import java.text.NumberFormat;

// Statics
public final class Statics
{
	// Do not allow instantiation of this class.
	private Statics() {}
	
	// Widths of columns
	public static final int WIDTH_NUMBER = 15;
	public static final int WIDTH_TYPE = 15;
	public static final int WIDTH_AMOUNT = 30;
	
	// Deliberately make this format string respond to strings for reusability.
	public static final String TOSTRING_FORMAT = "%-" + Statics.WIDTH_NUMBER + "s %-" + Statics.WIDTH_TYPE + "s %" + Statics.WIDTH_AMOUNT + "s";
	public static final String TOSTRING_FORMAT_SHORT = "%-" + Statics.WIDTH_NUMBER + "s %" + Statics.WIDTH_AMOUNT + "s";
	
	// NumberFormat instance of a dollar amount.
	public static final NumberFormat dollarFormat = NumberFormat.getCurrencyInstance();
}
