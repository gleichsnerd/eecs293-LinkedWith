package tests;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import main.Friend;
import main.SocialNetwork;
import main.SocialNetworkStatus;
import main.UninitializedObjectException;
import main.User;

import org.junit.Before;
import org.junit.Test;

/**
 * JUnit testing for the SocialNetwork class
 * @author Adam Gleichsner (amg188@case.edu)
 */
public class SocialNetworkTest {

	//General variables to be used during testing
	SocialNetwork network;
	User user1;
	User user2;
	Date date1;
	Date date2;
	Set<String> ids;
	SocialNetworkStatus status;
	
	/**
	 * Setup general network necessities before each test
	 */
	@Before
	public void setupTests() {
		this.network = new SocialNetwork();
		
		this.user1 = new User();
		this.user2 = new User();
		
		this.user1.setID("1");
		this.user2.setID("2");
		
		this.ids = new HashSet<String>();
		this.ids.add("1");
		this.ids.add("2");
		
		this.date1 = new Date(951886800000L);		//Mar 1, 2000
		this.date2 = new Date(954565200000L);		//Apr 1, 2000
		
		this.status = new SocialNetworkStatus();
	}
	
	/**
	 * Test user addition, retrieval, and checking
	 */
	@Test
	public void testUsers() {
		
		assertFalse("Cannot be a member if not added", this.network.isMember(user1.getID()));
		assertNull("Cannot get a member that doesn't exist", this.network.getUser(user1.getID()));
		assertTrue("Member should be added if not previously in the system", this.network.addUser(user1));
		
		assertTrue("Must be a member if added to system", this.network.isMember(user1.getID()));
		
		assertEquals("Member should be retrievable", this.network.getUser(user1.getID()), user1);
		assertNotEquals("Members should be unique", this.network.getUser(user1.getID()), user2);
		
		assertFalse("Member cannot be readded once put into the system", this.network.addUser(user1));
		
	}
	
	/**
	 * Test for link establishing and tearing down, including initial generation of links
	 * upon first establishment
	 * @throws UninitializedObjectException
	 */
	@Test
	public void testLink() throws UninitializedObjectException {
		this.network.addUser(user1);
		this.network.addUser(user2);
		
		assertFalse("Link is inactive if it hasn't been  established yet", this.network.isActive(ids, date1));
		assertFalse("Cannot tear down a link if it hasn't been established", this.network.tearDownLink(ids, date1, this.status));
		assertEquals("Status code should be ALREADY_INACTIVE", this.status.getStatus(), SocialNetworkStatus.StatusCode.ALREADY_INACTIVE);

		
		assertTrue("If previous link does not exist, a new link should be created and established", this.network.establishLink(ids, date2, this.status));
		assertEquals("Status code should be SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		
		assertTrue("An established link should be active", this.network.isActive(ids, date2));
		assertFalse("Cannot re-establish an active link", this.network.establishLink(ids, date2, this.status));
		assertEquals("Status code should be ALREADY_ACTIVE", this.status.getStatus(), SocialNetworkStatus.StatusCode.ALREADY_ACTIVE);

		assertFalse("Cannot tear down on a date earlier than the most recent date", this.network.tearDownLink(ids, date1, this.status));
		assertEquals("Status code should be INVALID_DATE", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_DATE);

		assertTrue("Tear down on the same date should be allowed", this.network.tearDownLink(ids, date2, this.status));
		assertEquals("Status code should be SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		assertFalse("Cannot tear down what is already down", this.network.tearDownLink(ids, date2, this.status));
		assertEquals("Status code should be ALREADY_INACTIVE", this.status.getStatus(), SocialNetworkStatus.StatusCode.ALREADY_INACTIVE);
		assertFalse("Cannot establish on a date earlier than the most recent tear", this.network.establishLink(ids, date1, this.status));
		assertEquals("Status code should be INVALID_DATE", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_DATE);
		
		
	}
	
	@Test
	public void testNeighborhood() throws UninitializedObjectException {
		Set<String> idSet = new HashSet<String>();
		Set<Friend> friendSet = new HashSet<Friend>();
		Map<Date, Integer> trendMap = new HashMap<Date, Integer>();
		
		User user3 = new User();
		User user4 = new User();
//		User user5 = new User();
//		User user6 = new User();
		
		Friend fUser2 = new Friend();
		Friend fUser3 = new Friend();
		Friend fUser4 = new Friend();
//		Friend fUser5 = new Friend();
//		Friend fUser6 = new Friend();
		
		user3.setID("3");
		user4.setID("4");
//		user5.setID("5");
//		user6.setID("6");
		
		fUser2.set(user2, 0);
		fUser3.set(user3, 1);
		fUser4.set(user4, 2);
//		fUser5.set(user5, 0);
//		fUser6.set(user6, 1);
			
		this.network.addUser(user1);
		this.network.addUser(user2);
		this.network.addUser(user3);
		this.network.addUser(user4);
//		this.network.addUser(user5);
		
		//Begin testing procedure
		
		//Test none
		this.network.neighborhood("1", date1, -1, status);
		assertEquals("A negative distance should return status code INVALID_DISTANCE", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_DISTANCE);
		this.network.neighborhood("", date1, 0, status);
		assertEquals("A blank user string should return status code INVALID_USERS", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_USERS);
		this.network.neighborhood("42", date1, status);
		assertEquals("A nonexistent user string should return status code INVALID_USERS", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_USERS);
		this.network.neighborhoodTrend("42", status);
		assertEquals("A nonexistent user string should return status code INVALID_USERS", this.status.getStatus(), SocialNetworkStatus.StatusCode.INVALID_USERS);
		
		assertEquals("An unlinked user should net no friends in his network", this.network.neighborhood("1", date1, status).toString(), "[" + "]");
		assertEquals("An unlinked user should net no friends in his network with distance limit", this.network.neighborhood("1", date1, 0, status).toString(), "[" + "]");
		
		//Test one
		idSet.add(user1.getID());
		idSet.add(user2.getID());
		
		this.network.establishLink(idSet, date1, status);
		
		assertEquals("A user with one active link should have that friend in his neighborhood", this.network.neighborhood("1", date1, status).toString(), "[" + fUser2.toString() + "]");
		assertEquals("A user with one active link should have that friend in his neighborhood within a distance limit", this.network.neighborhood("1", date1, 0, status).toString(), "[" + fUser2.toString() + "]");
		
		trendMap.put(date1, 1);
		assertEquals("A user with one active link should have a trend map with all dates in that link and a size of 1", this.network.neighborhoodTrend("1", status), trendMap);
		assertEquals("A successful map generation should have status code SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		
		this.network.tearDownLink(idSet, date1, status);
		
		assertEquals("A user with only an inactive link shouldn't have that friend in his network", this.network.neighborhood("1", date1, status).toString(), "[" + "]");
		
		trendMap = new HashMap<Date, Integer>();
		assertEquals("A user with one active link should have a trend map with all dates in that link and a size of 1", this.network.neighborhoodTrend("1", status), trendMap);
		assertEquals("A successful map generation should have status code SUCCESS", this.status.getStatus(), SocialNetworkStatus.StatusCode.SUCCESS);
		
		this.network.establishLink(idSet, date1, status);
		
		//Test many
		idSet = new HashSet<String>();
		idSet.add(user2.getID());
		idSet.add(user3.getID());
		this.network.establishLink(idSet, date1, status);
		
		friendSet.add(fUser2);
		friendSet.add(fUser3);
	
		assertTrue("All friends should be in the neighborhood", this.setsAreEqual(this.network.neighborhood("1", date1, status), friendSet));		
		assertEquals("Limited neighborhood should only have the first friend", this.network.neighborhood("1", date1, 0, status).toString(), "[" + fUser2.toString() + "]");
		
		trendMap.put(date1, friendSet.size());
		assertEquals("A neighborhood of two friends with the same link date should yield a map with value 2", this.network.neighborhoodTrend("1", status), trendMap);
		
		idSet = new HashSet<String>();
		idSet.add(user3.getID());
		idSet.add(user4.getID());
		this.network.establishLink(idSet, date2, status);
		
		friendSet.add(fUser4);
		assertTrue("All friends should be in the neighborhood", this.setsAreEqual(this.network.neighborhood("1", date2, status), friendSet));
		assertEquals("Limited neighborhood should only have the first friend", this.network.neighborhood("1", date2, 0, status).toString(), "[" + fUser2.toString() + "]");
		
		trendMap.put(date1, 2);
		trendMap.put(date2, 3);
		assertEquals("A neighborhood of two friends with the same link date should yield a map with two dates", this.network.neighborhoodTrend("1", status), trendMap);
		
		
	}
	
	private <T> boolean setsAreEqual(Set<T> set1, Set<T> set2) {
		for (T item: set1) {
			if (set2.contains(item)) {
				return false;
			}
		}
		
		return true;
	}

}
