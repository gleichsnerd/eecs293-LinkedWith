package tests;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import main.Link;
import main.SocialNetworkStatus;
import main.UninitializedObjectException;
import main.User;

import org.junit.Before;
import org.junit.Test;

public class LinkTest {

	//Testing users
	User user1;
	User user2;
	User user3;
	
	//Testing link
	Link link;
	
	//Testing status
	SocialNetworkStatus status;
	
	/**
	 * Setup testing users and the test link before each test
	 */
	@Before
	public void setupTests() {
		this.status = new SocialNetworkStatus();
		
		this.link = new Link();
		
		this.user1 = new User();
		this.user2 = new User();
		this.user3 = new User();
		
		user1.setID("1");
		user2.setID("2");
		user3.setID("3");
	}
	
	
	/**
	 * Test creating a link with a set of users. A set must contain
	 * exactly two unique users.
	 */
	@Test
	public void testLinkCreation() {
		Set<User> duplicateSet = new HashSet<User>();
		Set<User> largeSet = new HashSet<User>();
		Set<User> smallSet = new HashSet<User>();
		Set<User> rightSet = new HashSet<User>();
		Set<User> redundantSet = new HashSet<User>();
		
		//Not unique
		duplicateSet.add(user1);
		duplicateSet.add(user1);
		
		//Too many users
		largeSet.add(user1);
		largeSet.add(user2);
		largeSet.add(user3);
		
		//Too few users
		smallSet.add(user1);
		
		//Just right
		rightSet.add(user1);
		rightSet.add(user2);
		
		//Can't reset users after first set
		redundantSet.add(user2);
		redundantSet.add(user3);
		
		assertFalse("Link should initially be invalid", this.link.isValid());
		
		assertFalse("Shouldn't be able to set link with identical users", this.link.setUsers(duplicateSet, this.status));
		assertEquals("Status code should be INVALID_USERS", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_USERS);
		assertFalse("Shouldn't be able to set link with more than two users", this.link.setUsers(largeSet, this.status));
		assertEquals("Status code should be INVALID_USERS", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_USERS);
		assertFalse("Shouldn't be able to set link with less than two users", this.link.setUsers(smallSet, this.status));
		assertEquals("Status code should be INVALID_USERS", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_USERS);
		assertTrue("Should be able to set two unique Users for the first time", this.link.setUsers(rightSet, this.status));
		assertEquals("Status code should be SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		
		assertTrue("Link should be valid once Users are set", this.link.isValid());
		assertFalse("Shouldn't be able to reset link with another set", this.link.setUsers(redundantSet, this.status));	
		assertEquals("Status code should be ALREADY_ACTIVE", this.status.getStatus(), SocialNetworkStatus.StatusCode.ALREADY_ACTIVE);
	}
	
	/**
	 * Test for getUsers while link is invalid
	 * @throws UninitializedObjectException
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testInvalidGetUsers() throws UninitializedObjectException {
		assertFalse("Link should initially be invalid", this.link.isValid());
		link.getUsers();
	}
	
	/**
	 * Test for establish() while link is invalid
	 * @throws UninitializedObjectException
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testInvalidEstablish() throws UninitializedObjectException {
		Date janOne2000 = new Date(946702800000L);
		assertFalse("Link should initially be invalid", this.link.isValid());
		link.establish(janOne2000, this.status);
	}
	
	/**
	 * Test for tearDown() while link is invalid
	 * @throws UninitializedObjectException
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testInvalidTearDown() throws UninitializedObjectException {
		Date janOne2000 = new Date(946702800000L);
		assertFalse("Link should initially be invalid", this.link.isValid());
		link.tearDown(janOne2000, this.status);
	}
	
	/**
	 * Test for firstEvent() while link is invalid
	 * @throws UninitializedObjectException
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testInvalidFirstEvent() throws UninitializedObjectException {
		assertFalse("Link should initially be invalid", this.link.isValid());
		link.firstEvent();
	}
	
	/**
	 * Test for nextEvent() while link is invalid
	 * @throws UninitializedObjectException
	 */
	@Test (expected = UninitializedObjectException.class)
	public void testInvalidNextEvent() throws UninitializedObjectException {
		Date janOne2000 = new Date(946702800000L);
		assertFalse("Link should initially be invalid", this.link.isValid());
		link.nextEvent(janOne2000);
	}
	
	/**
	 * Test for null input for isActive()
	 * @throws UninitializedObjectException
	 */
	@Test (expected = NullPointerException.class)
	public void testNullDateInput() throws NullPointerException, UninitializedObjectException {
		link.isActive(null);
	}
	
	/**
	 * Test for various establish() and tearDown() sequences
	 * @throws UninitializedObjectException
	 */
	@Test
	public void testLinkEstAndTear() throws UninitializedObjectException {
		Date janOne2000 = new Date(946702800000L);
		Date febOne2000 = new Date(949381200000L);
		Date marOne2000 = new Date(951886800000L);
		Date aprOne2000 = new Date(954565200000L);
		
		Set<User> rightSet = new HashSet<User>();
		rightSet.add(user1);
		rightSet.add(user2);
		
		this.link.setUsers(rightSet, this.status);
		
		assertTrue("Link should be valid once Users are set", this.link.isValid());
		assertFalse("Link should be considered inactive if no elements", this.link.isActive(janOne2000));
		
		assertFalse("Shouldn't be able to tear down a link that hasn't been established", this.link.tearDown(febOne2000, this.status));
		assertEquals("Status code should be ALREADY_INACTIVE", this.status.getStatus(), SocialNetworkStatus.StatusCode.ALREADY_INACTIVE);
		assertTrue("Should be able to establish an initial link", this.link.establish(febOne2000, this.status));
		assertEquals("Status code should be SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		
		assertFalse("Shouldn't be able to establish a link while active", this.link.establish(marOne2000, this.status));
		assertEquals("Status code should be ALREADY_ACTIVE", this.status.getStatus(), SocialNetworkStatus.StatusCode.ALREADY_ACTIVE);
		assertFalse("Shouldn't be able to tear down an active link with a date before establish", this.link.tearDown(janOne2000, this.status));
		assertEquals("Status code should be INVALID_DATE", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_DATE);
		assertTrue("Should be able to tear down a link while it's active", this.link.tearDown(marOne2000, this.status));
		assertEquals("Status code should be SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		
		assertFalse("Shouldn't be able to establish an inactive link with a date before tear down", this.link.establish(janOne2000, this.status));
		assertEquals("Status code should be INVALID_DATE", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_DATE);
		assertFalse("Shouldn't be able to tear down an inactive link", this.link.tearDown(aprOne2000, this.status));
		assertEquals("Status code should be ALREADY_INACTIVE", this.status.getStatus(), SocialNetworkStatus.StatusCode.ALREADY_INACTIVE);
		assertTrue("Should be able to establish a link while inactive", this.link.establish(aprOne2000, this.status));
		assertEquals("Status code should be SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		
		assertTrue("Should be able to tear down on same date", this.link.tearDown(aprOne2000, this.status));
		assertEquals("Status code should be SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		assertTrue("Should be able to establish on same date", this.link.establish(aprOne2000, this.status));
		assertEquals("Status code should be SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		
		assertTrue("", this.link.isActive(new Date()));
	}
	
	/**
	 * Test for firstEvent and nextEvent at zero, one, and many events
	 * @throws UninitializedObjectException
	 */
	@Test
	public void testLinkEvents() throws UninitializedObjectException {
		Date janOne2000 = new Date(946702800000L);
		Date febOne2000 = new Date(949381200000L);
		Date marOne2000 = new Date(951886800000L);
		Date aprOne2000 = new Date(954565200000L);
		
		Set<User> rightSet = new HashSet<User>();
		rightSet.add(user1);
		rightSet.add(user2);
		
		this.link.setUsers(rightSet, this.status);
		
		//Zero events
		assertNull("Shouldn't be able to grab a first event if there are no events", this.link.firstEvent());
		assertNull("Can't get next event if there are no events", this.link.nextEvent(febOne2000));
		
		//One events
		this.link.establish(febOne2000, this.status);
		assertEquals("Should be able to grab the first event if initialized", this.link.firstEvent(), febOne2000);
		assertNull("Can't grab next event if there isn't one", this.link.nextEvent(febOne2000));
		assertEquals("Can grab an event if the date given is before the event", this.link.nextEvent(janOne2000), febOne2000);
		
		//Many events
		this.link.tearDown(marOne2000, this.status);
		assertEquals("First event should always be the first event", this.link.firstEvent(), febOne2000);
		assertEquals("Can grab next event if date is equal to an earlier event", this.link.nextEvent(febOne2000), marOne2000);
		assertNull("Can't grab next event if date is after last event", this.link.nextEvent(aprOne2000));
		
	}
	
	/**
	 * Test for toString() output
	 * @throws UninitializedObjectException 
	 * @throws InterruptedException 
	 */
	@Test
	public void testToString() throws UninitializedObjectException, InterruptedException {
		Date janOne2000 = new Date(946702800000L);
		Date febOne2000 = new Date(949381200000L);
		
		Set<User> rightSet = new HashSet<User>();
		rightSet.add(user1);
		rightSet.add(user2);
		
		assertEquals("Invalid link should result in special text", this.link.toString(), "Invalid Link: Uninitialized IDs");
		
		this.link.setUsers(rightSet, this.status);
		
		//Zero events
		assertEquals("No events should only produce first line of text", this.link.toString(), "Link between User ID: 1 and User ID: 2\n");
		
		//One event
		this.link.establish(janOne2000, this.status);
		assertEquals("One event should produce an additional established link", this.link.toString(), "Link between User ID: 1 and User ID: 2\nLink established on Sat Jan 01 00:00:00 EST 2000\n");

		//Many events
		this.link.tearDown(febOne2000, this.status);
		assertEquals("One event should produce an additional established link", this.link.toString(), "Link between User ID: 1 and User ID: 2\nLink established on Sat Jan 01 00:00:00 EST 2000\nLink torn down on Tue Feb 01 00:00:00 EST 2000\n");
	}
	

}
