package tests;

import static org.junit.Assert.*;
import main.UninitializedObjectException;
import main.User;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit 4 Test cases for User class.
 * @author Adam Gleichsner (amg188@case.edu)
 */
public class UserTest {

	//Testing user
	User testUser;
	
	/**
	 * Setup the test user before each test
	 */
	@Before
	public void beforeTests() {
		this.testUser = new User();
	}
	
	/**
	 * Tests that creating a new user returns an invalid object, that setting the 
	 * id actually sets id and makes user valid, and that re-setting the id will not
	 * change the original id
	 * @throws UninitializedObjectException 
	 */
	@Test
	public void testNewUserCreation() throws UninitializedObjectException {
		String correctString = "1234";
		String incorrectString = "5678";
		
		String firstName1 = "Bill";
		String firstName2 = "Tom";
		String middleName1 = "F.";
		String middleName2 = "M.";
		String lastName1 = "Murray";
		String lastName2 = "Riddle";
		String email1 = "bfm123@is.cool";
		String email2 = "tmr456@is.bad";
		String phone1 = "8675309";
		String phone2 = "1-800-VOLDEMORT";
		
		//A new user shouldn't have an id nor should it be marked as valid
		assertNull("New User doesn't have a null id", testUser.getID());
		assertFalse("New User should be invalid", testUser.isValid());
		
		//If we initialize a user with an id, it should be marked as valid
		testUser.setID(correctString);
		assertEquals("ID should be equal since it is the first time setting it", testUser.getID(), correctString);
		assertTrue("User should be valid after id is set", testUser.isValid());
		
		//We cannot reset the id
		testUser.setID(incorrectString);
		assertEquals("ID should remain equal to first set value after attempted reset", testUser.getID(), correctString);
		
		testUser.setFirstName(firstName1);
		testUser.setMiddleName(middleName1);
		testUser.setLastName(lastName1);
		testUser.setEmail(email1);
		testUser.setPhoneNumber(phone1);
		
		assertEquals("First Name should be set", testUser.getFirstName(), firstName1);
		assertEquals("Middle Name should be set", testUser.getMiddleName(), middleName1);
		assertEquals("Last Name should be set", testUser.getLastName(), lastName1);
		assertEquals("Email should be set", testUser.getEmail(), email1);
		assertEquals("Phone number should be set", testUser.getPhoneNumber(), phone1);
		
		testUser.setFirstName(firstName2);
		testUser.setMiddleName(middleName2);
		testUser.setLastName(lastName2);
		testUser.setEmail(email2);
		testUser.setPhoneNumber(phone2);
		
		assertEquals("First Name should be reset", testUser.getFirstName(), firstName2);
		assertEquals("Middle Name should be reset", testUser.getMiddleName(), middleName2);
		assertEquals("Last Name should be reset", testUser.getLastName(), lastName2);
		assertEquals("Email should be reset", testUser.getEmail(), email2);
		assertEquals("Phone number should be reset", testUser.getPhoneNumber(), phone2);
		
		
	}
	
	/**
	 * Tests for an exception when the user is invalid and a personal property is set
	 * @throws UninitializedObjectException 
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testBadFirstNameAssignment() throws UninitializedObjectException {
		//Can't set name of an invalid user
		testUser.setFirstName("This shouldn't be possible");
	}
	
	/**
	 * Tests for an exception when the user is invalid and a personal property is set
	 * @throws UninitializedObjectException 
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testBadMiddleNameAssignment() throws UninitializedObjectException {
		//Can't set name of an invalid user
		testUser.setMiddleName("This shouldn't be possible");
	}
	
	/**
	 * Tests for an exception when the user is invalid and a personal property is set
	 * @throws UninitializedObjectException 
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testBadLastNameAssignment() throws UninitializedObjectException {
		//Can't set name of an invalid user
		testUser.setLastName("This shouldn't be possible");
	}
	
	/**
	 * Tests for an exception when the user is invalid and a personal property is set
	 * @throws UninitializedObjectException 
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testBadEmailAssignment() throws UninitializedObjectException {
		//Can't set name of an invalid user
		testUser.setEmail("This shouldn't be possible");
	}
	
	/**
	 * Tests for an exception when the user is invalid and a personal property is set
	 * @throws UninitializedObjectException 
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testBadPhoneNumberAssignment() throws UninitializedObjectException {
		//Can't set name of an invalid user
		testUser.setPhoneNumber("This shouldn't be possible");
	}
	/**
	 * Tests for a NullPointerException when the id to set is null
	 */
	@Test (expected = NullPointerException.class)
	public void testNullIDAssignment() {
		//Can't set id as null
		testUser.setID(null);
	}
	
	/**
	 * Tests for a NullPointerException when the id is an empty string
	 */
	@Test (expected = NullPointerException.class)
	public void testEmptyIDAssignment() {
		//Can't set id as empty string
		testUser.setID("");
	}
	
	@Test
	public void testToString() {
		String id = "42";
		//Uninitialized users can't be printed
		assertEquals("Invalid user shouldn't be printable", testUser.toString(), "Invalid User: Uninitialized ID");
		
		//Initialized users can be printed
		testUser.setID(id);
		assertEquals("Valid user should output his id", testUser.toString(), "User ID: " + id);
	}

}
