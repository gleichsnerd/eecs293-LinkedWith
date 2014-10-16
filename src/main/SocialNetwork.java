package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * SocialNetwork class manages a map of users and links associated with them,
 * as well as providing access to creating links and events between users
 * @author Adam Gleichsner (amg188@case.edu)
 */
public class SocialNetwork {

	//Pairs each user to a list of all links they have
	private HashMap<User, ArrayList<Link>> network;

	/**
	 * Class constructor
	 * Creates a new network HashMap
	 */
	public SocialNetwork() {
		this.network = new HashMap<User, ArrayList<Link>>();
	}
	
	/**
	 * Checks if user is in the system. If he isn't, add them.
	 * @param user - User to add
	 * @return boolean - true if added, false otherwise
	 */
	public boolean addUser(User user) {
		this.checkNullInput(user);
		//If our user isn't a member and he's a valid user
		if (!this.isMember(user.getID()) && user.isValid()) {
			this.network.put(user, new ArrayList<Link>());
			return true;
		}
		return false;
	}
	
	/**
	 * Tries to grab the user using getUser(). If the user is returned,
	 * then obviously it must be in the map.
	 * @param id - target user id
	 * @return boolean - true if found, false if not
	 */
	public boolean isMember(String id) {
		this.checkNullInput(id);
		if (!id.isEmpty()) {
			User user = this.getUser(id);
			if (user != null)
				return true;
		}
		return false;
	}
	
	/**
	 * Retrieve a user from network by scanning each user key for a matching
	 * id.
	 * @param id - Search target id
	 * @return User - user that has a matching id
	 */
	public User getUser(String id) {
		this.checkNullInput(id);
		if (!id.isEmpty()) {
			//For each user key in network
			for(User user: this.network.keySet()) {
				if (user.getID().equals(id))
					return user;
			}
		}
		return null;
	}
	
	/**
	 * Establishes a link between two users if they exist in the system. If they
	 * don't share a link yet, a new link is created and established.
	 * @param ids - Ids of the two users
	 * @param date - Date to establish on
	 * @param status - Wrapper class to return a status result depending on whether or not actions occurred and if not, why
	 * @return boolean - true if established, false if otherwise
	 * @throws UninitializedObjectException
	 */
	public boolean establishLink(Set<String> ids, Date date, SocialNetworkStatus status) throws UninitializedObjectException{
	
		Link userLink;	//Link to catch whether or not a link exists
		HashSet<User> userSet;	//Set to create a user set if necessary
		boolean returnBool = false;	//Variable to keep track of our return value
	
		this.checkNullInput(ids);
		this.checkNullInput(date);
		
		//If our ids don't match each other and are in the system
		if (this.idSetIsLegal(ids)){
			//Search the users for a common link
			userLink = this.findLinkWithUsers(ids);
				
			//If a link was returned, check to see that it isn't null. If it's inactive, establish a link
			if (userLink != null) {
				/* If the date comes before the most recent event or it's already active, then
				 * status will propagate that error message
				 */
				returnBool = userLink.establish(date, status);
				
			//Else if the link doesn't exist yet, create one
			} else {
				//Setup user set
				userSet = new HashSet<User>();
				userSet.add(this.getUser((String) ids.toArray()[0]));
				userSet.add(this.getUser((String) ids.toArray()[1]));
				
				//Create a new link, add the users, and establish the link
				userLink = new Link();
				userLink.setUsers(userSet, status);
				userLink.establish(date, status);
				
				//Add the link to both users in the network 
				this.network.get(this.getUser((String) ids.toArray()[0])).add(userLink);
				this.network.get(this.getUser((String) ids.toArray()[1])).add(userLink);
				
				//Set status to SUCCESS
				status.setStatus(SocialNetworkStatus.StatusCode.SUCCESS);
				//We've modified network, so return true
				returnBool = true;
			}
		} else 
			status.setStatus(SocialNetworkStatus.StatusCode.INVALID_USERS);
			
		
		return returnBool;
		
	}
	
	/**
	 * Tears down a link if it exists and is active. Since we can't tear down a link
	 * that hasn't been established yet, no link creation is done here.
	 * @param ids - ids of users to tear
	 * @param date	- date of teardown
	 * @param status - Status of event
	 * @return boolean - true if torn down, false if otherwise
	 * @throws UninitializedObjectException
	 */
	public boolean tearDownLink(Set<String> ids, Date date, SocialNetworkStatus status) throws UninitializedObjectException {
		Link userLink;	//userLink to reference for the teardown
		boolean returnBool = false;
		
		this.checkNullInput(ids);
		this.checkNullInput(date);
		//If the users are in the system, the ids are unique, the link exists and it's active
		if (this.idSetIsLegal(ids)){
			userLink = this.findLinkWithUsers(ids);
			if (userLink != null){
				//Grab the link and tear it down
				returnBool = userLink.tearDown(date, status);
			} else
				status.setStatus(SocialNetworkStatus.StatusCode.ALREADY_INACTIVE);
			
		} else
			status.setStatus(SocialNetworkStatus.StatusCode.INVALID_USERS);
		
		return returnBool;
	}
	
	/**
	 * Checks to see if the ids are unique and in the system, if they have a link,
	 * and if so if it's active
	 * @param ids - ids of users
	 * @param date - date to check activity
	 * @return boolean - true if active, false if inactive or no link
	 * @throws UninitializedObjectException
	 */
	public boolean isActive(Set<String> ids, Date date) throws UninitializedObjectException {
		Link userLink;
		boolean returnBool = false;
		
		this.checkNullInput(ids);
		this.checkNullInput(date);
		
		//If our id set has two ids that aren't equal
		if (this.idSetIsLegal(ids)){
			userLink = this.findLinkWithUsers(ids);
			if (userLink != null && userLink.isValid() && userLink.isActive(date)) {
				returnBool = true;
			}
		}
		
		return returnBool;
	}
	
	/**
	 * Searches the network to find all connections and their distances from the user with the id given.
	 * No limit to distance. Throws errors if inputs are null or if an object is uninitialized.
	 * @param id - id of the user to find all connections of
	 * @param date - date to check activity against
	 * @param status - return status of event
	 * @return Set<Friend> - Set of all users and their distances (i.e. friends)
	 * @throws UninitializedObjectException
	 */
	public Set<Friend> neighborhood(String id, Date date, SocialNetworkStatus status) throws UninitializedObjectException {
		Set<Friend> returnFriends = new HashSet<Friend>();
		
		this.checkNullInput(id, date, status);
				
		//If we're dealing with an empty string or a nonexistent user, stop and set status
		if (id.isEmpty() || this.getUser(id) == null) {
			status.setStatus(SocialNetworkStatus.StatusCode.INVALID_USERS);
			return null;
		} else {
			
			returnFriends = this.makeNeighborhood(id, date, -1);
			
			//Success!
			status.setStatus(SocialNetworkStatus.StatusCode.SUCCESS);
			return returnFriends;
		}
	}
	
	/**
	 * Method to grab all connections of a user with given id up to a certain distance. Throws an
	 * exception if parameters are null or an object is uninitialized.
	 * @param id - id of the user in question
	 * @param date - date to cross activity against
 	 * @param distance_max - max distance of connections
	 * @param status - return status of event
	 * @return Set<Friend> - Set of all users and their distances (i.e. friends)
	 * @throws UninitializedObjectException
	 */
	public Set<Friend> neighborhood(String id, Date date, int distance_max, SocialNetworkStatus status) throws UninitializedObjectException {
		Set<Friend> returnFriends = new HashSet<Friend>();
		
		this.checkNullInput(id, date, distance_max, status);
				
		//If we're dealing with an empty string or a nonexistent user, stop and set status
		if (id.isEmpty() || this.getUser(id) == null) {
			status.setStatus(SocialNetworkStatus.StatusCode.INVALID_USERS);
			return null;
		//If the distance is less that 0, it's invalid and we should stop
		} else if (distance_max < 0) {
			status.setStatus(SocialNetworkStatus.StatusCode.INVALID_DISTANCE);
			return null;
		} else {
			
			returnFriends = this.makeNeighborhood(id, date, distance_max);
			
			//Success!
			status.setStatus(SocialNetworkStatus.StatusCode.SUCCESS);
			return returnFriends;
		}
	}
	
	/**
	 * Method that maps dates of events in a neighborhood to the size of the neighborhood
	 * at that time. If user isn't in the network, we return null and a status code
	 * to reflect that state, otherwise return a map and success code.
	 * @param id - id of the user 
	 * @param status - object to keep track of outcome of the action
	 * @return Map<Date, Integer> - Map of event dates to size of neighborhood
	 * @throws UninitializedObjectException
	 */
	public Map<Date, Integer> neighborhoodTrend (String id, SocialNetworkStatus status) throws UninitializedObjectException {
		
		Map<Date, Integer> returnMap = null;
		Set<Date> eventDates;
		Set<Friend> neighborhood;
		this.checkNullInput(id, status);
		
		//If our user isn't in the network, there's no event map
		if (!this.isMember(id)) {
			status.setStatus(SocialNetworkStatus.StatusCode.INVALID_USERS);
		} else {
			//Gather the neighborhood and dates of events and get ready to store them in a map
			returnMap = new HashMap<Date, Integer>();
			neighborhood = this.neighborhood(id, new Date(), status);
			eventDates = this.getNeighborhoodDates(neighborhood);
			//For each date that an event occurred on
			for (Date date: eventDates)
				returnMap.put(date, this.neighborhood(id, date, status).size());
			
			status.setStatus(SocialNetworkStatus.StatusCode.SUCCESS);
		}
		
		return returnMap;
	}
	
	
	//Private Methods
	
	/**
	 * Private helper that starts the recursive process and converts the hashmap into a set
	 * @param id - id of user to get the neighborhood of
	 * @param date -  date to match activity against
	 * @param distance_max - max distance to get users of
	 * @return Set<Friend> - Set of friends to make up the neighborhood
	 * @throws UninitializedObjectException
	 */
	private Set<Friend> makeNeighborhood (String id, Date date, int distance_max) throws UninitializedObjectException {
		HashMap<User, Integer> mappedFriends = new HashMap<User, Integer>();
		User thisUser;
		ArrayList<Link> userLinks;
		ArrayList<String> idList;
		
		//Grab the user who the id belongs to so we can
		thisUser = this.getUser(id);
		
		//Get a list of all links and create a list of all ids that map to them
		userLinks = this.network.get(this.getUser(id));
		idList = SocialNetwork.createListOfManyElements(id, userLinks.size());
		
		//Start a recursive route to grab all of the connections.
		//We send -1 as the limit since we aren't concerned with max distance
		mappedFriends = this.mapFriends(new HashMap<User, Integer>(), idList, userLinks, date, 0, distance_max);
		//Remove this user since he'll be added by a 0 degree connection
		mappedFriends.remove(thisUser);
		
		return this.makeFriendSetFromMap(mappedFriends);
	}
		
	/**
	 * Helper method that organizes all links in the neighborhood into a set of dates
	 * where events occurred in the links
	 * @param neighborhood - set of friends to get dates for
	 * @return Set<Date> - set of all dates of events
	 * @throws UninitializedObjectException
	 */
	private Set<Date> getNeighborhoodDates (Set<Friend> neighborhood) throws UninitializedObjectException {
		Set<Date> returnSet = new HashSet<Date>();
		User friendUser;

		//For each friend in the neighborhood, grab the dates 
		for(Friend friend: neighborhood) {
			friendUser = friend.getUser();
			returnSet.addAll(this.getDatesFromManyLinks(this.network.get(friendUser)));
		}
		
		return returnSet;
	}
	
	/**
	 * Helper method that grabs all of the dates from a list of links
	 * corresponding to a user in the network
	 * @param linkList - array list of all the links of a user
	 * @return Set<Date> - a set of all the dates in those links
	 */
	private Set<Date> getDatesFromManyLinks (ArrayList<Link> linkList) {
		Set<Date> returnSet = new HashSet<Date>();
		
		//For each link in the linked list, grab the dates
		for (Link link: linkList) {
			returnSet.addAll(link.getLinks());
		}
		
		return returnSet;
	}
	
	/**
	 * Helper method that checks if the given inputs are null. Throws exception
	 * if so.
	 * @param input
	 * @throws NullPointerException
	 */
	private void checkNullInput(Object... input) throws NullPointerException{
		for(Object obj: input) {
			if (obj == null)
				throw new NullPointerException("Input parameter is null");
		}
	}
	
	/**
	 * Private helper to determine if ids in a set are unique and the members are in the network
	 * @param set - set of user ids
	 * @return boolean - true if legal, false otherwise
	 */
	private boolean idSetIsLegal(Set<String> set) {
		//If our set has two ids, then we can compare for legality
		if (set.size() == 2) {
			String string1 = (String) set.toArray()[0];
			String string2 = (String) set.toArray()[1];
			//If our ids aren't the same and each id is in the member set
			if (!string1.equals(string2) && this.isMember(string1) && this.isMember(string2))
				return true;
		}
		return false;
	}
	
	/**
	 * Private helper to find a link between two users
	 * @param ids - set of user ids
	 * @return Link - the shared link or null if not found
	 */
	private Link findLinkWithUsers(Set<String> ids) {
		String user1ID = (String) ids.toArray()[0];
		ArrayList<Link> links;
		SocialNetworkStatus status = new SocialNetworkStatus();
		
		this.checkNullInput(ids);
		
		//Create a link to compare against
		Link compLink = new Link();
		compLink.setUsers(this.createUserSetFromIDs(ids), status);
		
		links = this.network.get(this.getUser(user1ID));
		//Check each link in the network
		for (Link link: links) {
			if (link.equals(compLink)) 
				return link;
		}
	
		return null;
	}

	/**
	 * Helper that creates a set of users from a set of ids
	 * @param ids - ids to create users from
	 * @return Set<User> - the user set we want, but not the one we deserve
	 */
	private Set<User> createUserSetFromIDs (Set<String> ids) {
		String[] idArray;
		Set<User> returnSet = new HashSet<User>();
		User addUser = new User();
		
		this.checkNullInput(ids);
		
		idArray = ids.toArray(new String[ids.size()]);
		//If we can create the first user, then add and make the second	
		if (addUser.setID(idArray[0])) {
			returnSet.add(addUser);
			addUser = new User();
			//If we can make the second user, make and add baby
			if (addUser.setID(idArray[1]))
				returnSet.add(addUser);
			else
				returnSet = null;
		} else
			returnSet = null;
		
		return returnSet;
		
	}
	
	/**
	 * Private helper that makes a set of friends from a hashmap of users and distances
	 * @param map - map of users and distances
	 * @return Set<Friend> - Set of all users and their distances (i.e. friends)
	 */
	private Set<Friend> makeFriendSetFromMap(HashMap<User, Integer> map) {
		Friend makeableFriend;
		Set<Friend> returnSet = new HashSet<Friend>();
		
		this.checkNullInput(map);
		
		//For each user in the map, make a friend out of him
		for(User keyUser: map.keySet()) {
			makeableFriend = new Friend();
			makeableFriend.set(keyUser, map.get(keyUser));
			returnSet.add(makeableFriend);
		}
		
		return returnSet;
	}
	
	/**
	 * Private recursive helper that goes each step of distance and adds any unique connections to the neighborhood
	 * @param totalMap - map that users and distances are being added to
	 * @param ids - List of ids of parent connections in the list of links
	 * @param links - list of links to check children connections in
	 * @param date - date to compare activity against
	 * @param distance - distance that the new users should be added at
	 * @param limit - limit for the distance, if -1 then no limit
	 * @return HashMap<User, Integer> - map of users to distances
	 * @throws UninitializedObjectException
	 */
	private HashMap<User, Integer> mapFriends(HashMap<User, Integer> totalMap, ArrayList<String> ids, ArrayList<Link> links, Date date, int distance, int limit) throws UninitializedObjectException {
		//Create new lists for the next batch of ids and links
		ArrayList<String> nextIDs = new ArrayList<String>();
		ArrayList<Link> nextLinks = new ArrayList<Link>();
		User checkUser;
		Link link;
		String id;
		
		this.checkNullInput(totalMap, ids, links, date, distance, limit);
		
		//For each link we need to check
		for(int linkCounter = 0; linkCounter < links.size(); linkCounter++) {
			//Grab the current link and the parent id in that link
			link = links.get(linkCounter);
			id = ids.get(linkCounter);
			
			//Get the child user
			checkUser = new User();
			checkUser = link.getOtherUser(id);
			
			//If the user is unique, active, and within acceptable distance, then continue
			if(this.userShouldBeAddedToMap(totalMap, checkUser, link, date, distance, limit)) {
				totalMap.put(checkUser, distance);
				//If the user is unique, then we need to process all of his links
				nextLinks.addAll(this.network.get(checkUser));
				//Add his id to pair against his links
				nextIDs.addAll(SocialNetwork.createListOfManyElements(checkUser.getID(), this.network.get(checkUser).size()));
			}
				
		}
		//If there are more links to process, then recurse and be glad!
		if (!nextLinks.isEmpty())
			return this.mapFriends(totalMap, nextIDs, nextLinks, date, distance + 1, limit);
		else //Otherwise, finish up
			return totalMap;
	}
	
	/**
	 * Private helper that determines if, given all these factors, a user should be added to the map.
	 * @param map - Map to check if user is present
	 * @param user - user in question
	 * @param link - link containing user
	 * @param date - date to check activity
	 * @param distance - distance of the user to the original id
	 * @param limit - limit for the distance, if -1 then no limit to consider
	 * @return boolean - true if unique and active, false if otherwise 
	 * @throws UninitializedObjectException
	 */
	private boolean userShouldBeAddedToMap (HashMap<User, Integer> map, User user, Link link, Date date, int distance, int limit) throws UninitializedObjectException {
		this.checkNullInput(map, user, link, date, distance, limit);
		//If the user isn't in the map, he's active, and if there isn't a limit or if he's not too far away yet, then he's good
		if (!map.containsKey(user) && link.isActive(date) && (limit == -1 || distance <= limit))
			return true;
		else
			return false;
	}
	
	/**
	 * Private helper that creates an array list full of copies of a given element e
	 * @param e - Element to copy
	 * @param count - number of times to copy the element 
	 * @return ArrayList<T> - list of copies of e
	 * @throws NullPointerException
	 */
	private static <T> ArrayList<T> createListOfManyElements(T e, int count) throws NullPointerException{
		ArrayList<T> returnList = new ArrayList<T>();
		
		if (e == null)
			throw new NullPointerException("Input parameter is null");
		
		for(int i = 0; i < count; i++) {
			returnList.add(e);
		}
		
		return returnList;
	}


}
