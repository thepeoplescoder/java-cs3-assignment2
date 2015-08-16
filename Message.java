/*
 * Austin Cathey
 * Computer Science 3
 * Assignment 1
 */

package assignment2;

// Imports
import javax.swing.*;
import java.util.*;

/**
 * This class provides a wrapper for JOptionPane.showMessageDialog().  The majority
 * of the methods provide ways to construct message strings, so a message box can be
 * displayed only when a proper message string is constructed.
 *
 * @author Austin Cathey
 * @since September 12th, 2009
 */
public class Message
{
    /**
     * This the default Message object, to be treated similar to
     * <code><i>out</i></code> in the <code>java.lang.System</code> class.
     */
    public static final Message out = new Message();

    // Private instance variables.
    private String message;
    private ArrayList<String> postedMessages;
    private boolean locked;

    /**
     * <p>
     * Creates a new Message object that maintains its own internal
     * message string.  This newly created object is independent
     * of any other Message objects.
     * </p>
     * <p>
     * All message objects start out unlocked, with an empty message string,
     * and contain no posted messages.
     * </p>
     */
    public Message()
    {
        message = "";
        postedMessages = new ArrayList<String>();
        locked = false;
    }
    
    /*------------------*
     * Instance methods *
     *------------------*/

    /**
     * Locks or unlocks the Message object.  When a Message object is locked,
     * its message string cannot be changed, and messages cannot be posted
     * to be displayed later.  This method is only here primarily for restoring
     * a lock state that was previously saved with .isLocked().
     *
     * @param lockState <code>true</code> to lock the object, <code>false</code>
     *                  to unlock it.
     *
     * @see Message#lock()
     * @see Message#unlock()
     * @see Message#isLocked()
     */
    public void setLocked(boolean lockState)
    {
        locked = lockState;
    }

    /**
     * Checks to see whether or not the calling object is locked.
     * By default, all Message objects start out unlocked.
     *
     * @return <code>true</code> if the object is locked, <code>false</code>
     *         if the object is unlocked.
     *
     * @see Message#setLocked(boolean)
     * @see Message#lock()
     * @see Message#unlock()
     */
    public boolean isLocked()
    {
        return locked;
    }

    /**
     * Locks the calling Message object.
     * A locked Message object cannot be updated whatsoever.
     *
     * @see Message#setLocked(boolean)
     * @see Message#isLocked()
     * @see Message#unlock()
     */
    public void lock()
    {
        setLocked(true);
    }

    /**
     * Unlocks the calling Message object
     *
     * @see Message#setLocked(boolean)
     * @see Message#isLocked()
     * @see Message#lock()
     */
    public void unlock()
    {
        setLocked(false);
    }

    /**
     * Gets the message string.
     *
     * @return The current message string to be displayed.
     * @see Message#setMessage(String)
     */
    public String getMessage()
    {
        return message;
    }

    /**
     * Sets the message string.  If the calling object is locked, this
     * method does nothing.
     *
     * @param msg The new message string.
     * @see Message#getMessage()
     * @see Message#clear()
     * @see Message#print(java.lang.String)
     * @see Message#print(char)
     * @see Message#println()
     * @see Message#println(java.lang.String)
     * @see Message#printPostedMessages()
     */
    public void setMessage(String msg)
    { // Changes the message string.

        // Only change the message if it is unlocked.
        if (!isLocked())
        {
            message = msg;
        }
    }

    /**
     * Clears the string holding the message to be displayed.
     *
     * @return A reference to the calling object.
     * @see Message#print(java.lang.String)
     * @see Message#print(char)
     * @see Message#println()
     * @see Message#println(java.lang.String)
     */
    public Message clear()
    {
        setMessage("");
        return this;
    }

    /**
     * Appends a string to the message string.
     *
     * @return A reference to the calling object.
     * @see Message#print(char)
     * @see Message#println()
     * @see Message#println(java.lang.String)
     */
    public Message print(String s)
    { // Adds to the message without going to the next line.
        setMessage(getMessage() + s);
        return this;
    }

    /**
     * Appends a character to the message string.
     *
     * @return A reference to the calling object.
     * @see Message#print(java.lang.String)
     * @see Message#println()
     * @see Message#println(java.lang.String)
     */
    public Message print(char ch)
    {
        setMessage(getMessage() + ch);
        return this;
    }

    /**
     * Appends a string to the message string, along with a newline.
     *
     * @return A reference to the calling object.
     * @see Message#print(java.lang.String)
     * @see Message#print(char)
     * @see Message#println()
     */
    public Message println(String s)
    { // Adds to the message, going to the next line.
        print(s + '\n');
        return this;
    }

    /**
     * Appends a newline to the message string.
     *
     * @return A reference to the calling object.
     * @see Message#print(java.lang.String)
     * @see Message#print(char)
     * @see Message#println(java.lang.String)
     */
    public Message println()
    {
        print('\n');
        return this;
    }

    /**
     * Posts a message to be displayed at a later time.
     * If the calling object is locked, this method does nothing.
     *
     * @param s The message to be posted.
     * @return A reference to the calling object.
     * @see Message#postln(String)
     * @see Message#printPostedMessages()
     */
    public Message post(String s)
    { // Posts a message to be displayed at a later time.
        if (!isLocked())
        {
            postedMessages.add(s);
        }
        return this;
    }

    /**
     * Posts a message to be displayed at a later time.
     * The message will have a newline added to the end of it.
     * If the calling object is locked, this method does nothing.
     *
     * @param s The message to be posted.
     * @return A reference to the calling object.
     * @see Message#post(String)
     * @see Message#printPostedMessages()
     */
    public Message postln(String s)
    { // Posts a message to be displayed at a later time.
        post(s + '\n');
        return this;
    }

    /**
     * Displays all posted messages by appending them to
     * the message string.
     *
     * @return A reference to the calling object.
     * @see Message#post(String)
     * @see Message#postln(String)
     */
    public Message printPostedMessages()
    { // Adds the posted messages to the message string.
        if (!isLocked())
        {
            for (String s : postedMessages)
            {
                message += s;
            }
            postedMessages.clear();
        }
        return this;
    }

    /**
     * Displays a message box, containing the contents of the message string.
     * After this method is called, the object becomes unlocked, with its
     * internal message string cleared.
     * 
     * @return A reference to the calling object.
     */
    public Message show()
    { // Displays the message and unlocks it.
        JOptionPane.showMessageDialog(null, message);   // Display the message.
        unlock();                                       // Ensure that the message is unlocked.
        clear();                                        // Clear the message.
        return this;
    }
}
