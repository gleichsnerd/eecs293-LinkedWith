package main;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

public class FriendTest {
	
	User user1;
	User user2;
	
	int distance1;
	int distance2;
	
	Friend friend1;
	Friend friend2;
	
	@Before
	public void testSetup() {
		user1 = new User();
		user2 = new User();
		friend1 = new Friend();
		friend2 = new Friend();
		
		distance1 = 0;
		distance2 = 1;
		
		user1.setID("1");
		user2.setID("2");
		
	}
	
	@Test
	public void testSet() throws UninitializedObjectException {
		
		assertEquals("An unset friend should be invalid", friend1.toString(), "Invalid Friend");
		
		friend1.set(user1, distance1);
		assertEquals("Set friend should return the right user", friend1.getUser(), user1);
		assertEquals("Set friend should return the right distance", friend1.getDistance(), distance1);
		assertEquals("Set friend should return the right string", friend1.toString(), "Friend " + user1 + "\nDistance: " + distance1);
		
		friend1.set(user2, distance2);
		assertEquals("Already set friend should return the first user", friend1.getUser(), user1);
		assertEquals("Already set friend should return the first distance", friend1.getDistance(), distance1);
		assertEquals("Already set friend should return the first string", friend1.toString(), "Friend " + user1 + "\nDistance: " + distance1);
	}
	
	@Test
	public void testEquality() {
		friend1.set(user1, distance1);
		friend2.set(user2, distance2);
		
		assertNotEquals("Friends with different users shouldn't be equal", friend1, friend2);
		
		friend2 = new Friend();
		friend2.set(user1, distance2);
		
		assertEquals("Friends with same users should be equal", friend1, friend2);
		
	}
	

}
