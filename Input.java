/*
 * Austin Cathey
 * Computer Science 3
 * Assignment 1
 */

package assignment2;

// Imports
import javax.swing.*;

/**
 * This class provides wrapper methods around JOptionPane.showInputDialog()
 * to allow for an easier, more intuitive way to retrieve numeric values.
 *
 * @author Austin Cathey
 * @since September 12th, 2009
 * @version 1.0
 */
public class Input
{
    // Do not allow anyone to instantiate this class.
    private Input()
    {
    }

    /**
     * Prompts the user for a currency value.
     *
     * @param prompt The prompt that the user sees.
     * @return The number entered by the user, or <code>null</code> if the user
     *         decided to cancel, or enter an empty string.
     */
    public static Double nextCurrency(String prompt)
    { // Gets a currency value.
        boolean done = false;
        boolean exceptionCaught;
        Double currency = null;                 // To avoid compiler warning.

        String input;
        String pleaseEnterNumber, onlyTwoAfterDecimalPoint;
        int lengthAfterDecimalPoint;

        // Initialize variables
        pleaseEnterNumber = "";
        onlyTwoAfterDecimalPoint = "";

        // Get the number.
        while (!done)
        {
            // Read the number first.
            input = JOptionPane.showInputDialog(pleaseEnterNumber + onlyTwoAfterDecimalPoint + prompt);
            if (input == null || input.isEmpty())
            {
                break;
            }

            // Now, let's try to parse it.
            try
            {
                exceptionCaught = false;
                currency = Double.parseDouble(input);
            }

            // In case we were not able to get something numeric.
            catch (NumberFormatException ex)
            {
                // Do not handle the exception here because if the exception
                // is handled, there is some code that I wish to skip, and
                // I want to do so when I know that the stack frame has been
                // restored.
                exceptionCaught = true;
            }

            // Change the prompt if the exception was caught.
            if (exceptionCaught)
            {
                pleaseEnterNumber = "Please enter a numeric value.\n";
            }

            // Otherwise, check to see if the number was entered properly.
            else
            {
                // Find out how many characters occur after the decimal point.
                lengthAfterDecimalPoint = -input.indexOf('.');  // Look for a decimal point.
                if (lengthAfterDecimalPoint <= 0)               // If one exists, then find out
                {                                               // how many characters come after
                    lengthAfterDecimalPoint--;                  // it.
                    lengthAfterDecimalPoint += input.length();
                }
                else
                {
                    // Otherwise, a decimal point does not exist, so
                    // there are no characters after it.
                    lengthAfterDecimalPoint = 0;
                }

                // If there are more than two, then go again.
                if (lengthAfterDecimalPoint > 2)
                {
                    onlyTwoAfterDecimalPoint = "There should only be two digits after the decimal point.\n";
                }

                // Otherwise, get out of here.
                else
                {
                    done = true;
                }
            }
        }

        // Return the currency value.
        return currency;
    }

    /**
     * Prompts the user for an integer value.
     * 
     * @param prompt The prompt that the user sees.
     * @return The number that the user entered, or <code>null</code> if the user
     *         decided to cancel.
     */
    public static Integer nextInteger(String prompt)
    { // Gets an integer value.
        int originalLength = prompt.length();
        boolean done = false;
        Integer i = null;
        String input;


        // Get the integer.
        while (!done)
        {
            input = JOptionPane.showInputDialog(prompt);
            if (input == null || input.isEmpty())
            {
                break;
            }

            // Try to get something meaningful out of this.
            try
            {
                i = Integer.parseInt(input);
                done = true;
            }
            catch (NumberFormatException ex)
            {
                // Change the prompt if we need to do so.
                if (prompt.length() == originalLength)
                {
                    prompt = "Please enter a numeric value.\n" + prompt;
                }
            }
        }

        // Return the integer that was entered.
        return i;
    }
}
