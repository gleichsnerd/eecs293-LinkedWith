package main;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;


/**
 * Link class to associate two users with each other as well as record
 * the dates of any link creation/deletion
 * @author Adam Gleichsner (amg188@case.edu)
 */
public class Link {
	
	private Set<User> 			users;		//Set of two unique users
	private boolean				isValid;	//If link is initialized and has users
	private ArrayList<Date>		links;		//All events from establishing and tearing down
	
	/**
	 * Class constructor
	 * Creates an empty, invalid link 
	 */
	public Link(){
		this.users = new HashSet<User>();
		this.isValid = false;
		this.links = new ArrayList<Date>();
	}
	
	/**
	 * Takes a set of two users with non-matching ids and creates a link. If link is
	 * already valid (i.e.) or if the set of users contains anything but two unique
	 * users, no action is taken. 
	 * @param users - Set of two unique users
	 * @param status TODO
	 * @return boolean - true if actions occurred, false if otherwise
	 * @throws NullPointerException
	 */
	public boolean setUsers(Set<User> users, SocialNetworkStatus status) {
		this.checkNull(users);
		//If the link is invalid and our set consists of only two unique users
		if (!this.isValid){
			if (this.setIsLegal(users)) {
				this.users = users;
				this.isValid = true;
				status.setStatus(SocialNetworkStatus.StatusCode.SUCCESS);
				return true;
			} else
				status.setStatus(SocialNetworkStatus.StatusCode.INVALID_USERS);
		} else //Else we already have a valid link
			status.setStatus(SocialNetworkStatus.StatusCode.ALREADY_ACTIVE);
		return false;
	}
	
	/**
	 * Return whether or not the link is valid (i.e. has users)
	 * @return boolean - true if valid
	 */
	public boolean isValid() {
		return this.isValid;
	}
	
	/**
	 * Returns users if link is valid, otherwise throws an UninitializedObjectException
	 * @return Set<User> - Set containing the two unique users in the link
	 * @throws UninitializedObjectException
	 */
	public Set<User> getUsers() throws UninitializedObjectException {
		this.checkLinkIsValid("Error: Link is invalid", "Cannot get users of an invalid link");
		return this.users;
	}
	
	/**
	 * Getter method for the links array list
	 * @return ArrayList<Date> - list of all events in the link
	 */
	public ArrayList<Date> getLinks() {
		return this.links;
	}
	
	/**
	 * If the link is currently inactive and the date passed is either after or equal to the most
	 * recent date in links, then we establish the link and record the date
	 * @param date - Desired date to establish a new link
	 * @param status - Wrapper class to return a status result depending on whether or not actions occurred and if not, why
	 * @return Boolean - true if added, false otherwise 
	 * @throws UninitializedObjectException
	 * @throws NullPointerException
	 */
	public boolean establish(Date date, SocialNetworkStatus status) throws UninitializedObjectException {
		this.checkNull(date);
		this.checkNull(status);
		this.checkLinkIsValid("Error: Link is invalid", "Cannot establish an invalid link");
		
		// If our date is after or equal to the current date (i.e. not before) and the link is inactive
		if (this.links.size() == 0  || (!this.isActive(date) && !date.before(this.links.get(this.links.size() - 1)))){
			this.links.add(date);
			status.setStatus(SocialNetworkStatus.StatusCode.SUCCESS);
			return true;
		} else if (date.before(this.links.get(this.links.size() - 1)))
			status.setStatus(SocialNetworkStatus.StatusCode.INVALID_DATE);
		else
			status.setStatus(SocialNetworkStatus.StatusCode.ALREADY_ACTIVE);
		//The date is either before the most recent link or the link is currently active
		return false;
	}
	
	/**
	 * If the link is active and the date passed is after or equal to the most recent date, then
	 * we go ahead and tear down the link and record the new date
	 * @param date - Desired date to tear down the link
	 * @param status - Wrapper class to return a status result depending on whether or not actions occurred and if not, why
	 * @return Boolean - true if torn down, false otherwise
	 * @throws UninitializedObjectException
	 * @throws NullPointerException
	 */
	public boolean tearDown(Date date, SocialNetworkStatus status) throws UninitializedObjectException {
		this.checkNull(date);
		this.checkNull(status);
		this.checkLinkIsValid("Error: Link is invalid", "Cannot tear down an invalid link");
		
		// If our date is after or equal to the current date (i.e. not before) and the link is active
		if (this.links.size() > 0) {
			if (this.isActive(date) && !date.before(this.links.get(this.links.size() - 1))) {
				this.links.add(date);
				status.setStatus(SocialNetworkStatus.StatusCode.SUCCESS);
				return true;
			} else if (date.before(this.links.get(this.links.size() - 1)))
				status.setStatus(SocialNetworkStatus.StatusCode.INVALID_DATE);
			else
				status.setStatus(SocialNetworkStatus.StatusCode.ALREADY_INACTIVE);
		} else
			status.setStatus(SocialNetworkStatus.StatusCode.ALREADY_INACTIVE);
		//The date is either before the most recent link or the link is currently inactive
		return false;
	}
	
	/**
	 * Finds status of the link at the given date. If the link is invalid, throw
	 * an exception. If it is, determine if the date is already present, and if not,
	 * find where it fits into the list
	 * @param date - Desired date to determine whether or not the link was active then
	 * @return boolean - True if link is active, false otherwise
	 * @throws UninitializedObjectException
	 * @throws NullPointerException
	 */
	public boolean isActive(Date date) throws UninitializedObjectException{
		//Variable to hold the event index that determines if we're active
		int dateCount = -1;
		
		this.checkNull(date);
		this.checkLinkIsValid("Error: Link is invalid", "Cannot get status of an invalid link");
		
		if (this.links.size() == 0)
			dateCount = 0;
		else if (this.links.size() == 1) {
			//If the date is before the only element, we're inactive, otherwise we're active
			dateCount = this.binaryDateComparison(date);
		} else {
			//Don't add one because the index returned is also the number of the events
			dateCount = this.findSizeForDate(date);
		}
		
		//If the index is divisible by two, then we are currently not active
		if (dateCount % 2 == 0)
			return false;
		else
			return true;
	}
	
	
	
	/**
	 * Grabs the first event in links as long as the link is valid and has been established before.
	 * If link is invalid, throw an exception.
	 * @return Date - Date that the link was first established, null if not established
	 * @throws UninitializedObjectException
	 */
	public Date firstEvent() throws UninitializedObjectException {
		this.checkLinkIsValid("Error: Link is invalid", "Cannot get an event from an invalid link");
			
		if (this.links.size() == 0)
			return null;
		else
			return this.links.get(0);
	}
	
	/**
	 * Grab the event that occurs after the param date. If there is no event, return null, and if the
	 * link is invalid, throw an exception
	 * @param date - date prior to return event
	 * @return Date - date of next event, null if no event
	 * @throws UninitializedObjectException
	 * @throws NullPointerException
	 */
	public Date nextEvent(Date date) throws UninitializedObjectException {
		Date returnDate = null;
		this.checkNull(date);
		this.checkLinkIsValid("Error: Link is invalid", "Cannot get an event from of an invalid link");
			
		if (!this.links.isEmpty()){
			//If the list only has one date that is after date
			if (this.links.size() == 1 && date.before(this.links.get(0)))
				returnDate =  this.links.get(0);
			else if (date.before(this.links.get(this.links.size() - 1)))
				returnDate =  this.links.get(this.findSizeForDate(date));
		}
		
		return returnDate;
	}
	
	/**
	 * Helper method to grab the other user besides the one with the specified id
	 * @param id - id of the undesired user
	 * @return User - user object from the link that doesn't have the id, null if id not in link
	 * @throws UninitializedObjectException
	 */
	public User getOtherUser(String id) throws UninitializedObjectException {
		User[] userArray = new User[this.users.size()];
		
		this.checkNull(id);
		this.checkLinkIsValid("Link is invalid", "Can't find a user of an invalid link");
		
		//Sort the users because sets are dumb and like to mess things up
		userArray = User.twoUserSort(this.users.toArray(new User[this.users.size()]));
		
		//If the id matches one of our users, return the other user
		if (userArray[0].getID().compareTo(id) == 0)
			return userArray[1];
		else if (userArray[1].getID().compareTo(id) == 0)
			return userArray[0];
		else
			return null;
	}
	
	/**
	 * Returns a human-friendly string showing the two users in the link and
	 * every event of establishing or tearing down. If the link is invalid,
	 * special text is returned.
	 * @return String - string form of link
	 */
	@Override
	public String toString() {
		StringBuilder returnString = new StringBuilder();
		Object[] genericSet = this.users.toArray();
		String user1;
		String user2;
		
		if (!this.isValid) {
			returnString.append("Invalid Link: Uninitialized IDs");
		} else {
			//Sort so the lowest ID is displayed in front consistently
			if (genericSet[0].toString().compareTo(genericSet[1].toString()) <= 0) {
				 user1 = this.users.toArray()[0].toString();
				 user2 = this.users.toArray()[1].toString();
			} else {
				user1 = this.users.toArray()[1].toString();
				user2 = this.users.toArray()[0].toString();
			}
			returnString.append("Link between " + user1 + " and " + user2 + "\n");
			for (int i = 0; i < this.links.size(); i++) {
				//If we have an odd number of elements then the link was established, but
				//because we index at 0, odd is divisible by 2
				if(i % 2 == 0) 
					returnString.append("Link established on " + this.links.get(i).toString() + "\n");
				else
					returnString.append("Link torn down on " + this.links.get(i).toString() + "\n");
			}
		}
		
		return returnString.toString();
	}
	
	/**
	 * Overridden equals method to compare the ids of the users in the link
	 * @param object - item to compare against
	 * @return boolean - true if user ids match
	 */
	@Override
	public boolean equals(Object object) {
		User[] ourUsers = this.users.toArray(new User[this.users.size()]);
		ourUsers = User.twoUserSort(ourUsers);
		boolean returnBool = false;
		
		this.checkNull(object);
		
		if (object instanceof Link) {
			Link compLink = (Link) object;
			User[] compUsers = null;
			try {
				//Grab our users from the input object. Catch an exception if we have no users.
				compUsers = compLink.getUsers().toArray(new User[compLink.getUsers().size()]);
				compUsers = User.twoUserSort(compUsers);

				if(ourUsers[0].equals(compUsers[0]) && ourUsers[1].equals(compUsers[1]))
					returnBool = true;

			} catch (UninitializedObjectException e) {
				//Do nothing, wait for false returnBool to go through
				System.err.println(e);
			}	
		}
		
		return returnBool;
	}
	
	//Private Methods
	
	/**
	 * Private helper method that will throw a NullPointerException if the input is null
	 * @param input - any input objects to be checked
	 * @throws NullPointerException
	 */
	private void checkNull(Object... input) throws NullPointerException {
		for(Object obj: input) {
			if (obj == null)
				throw new NullPointerException("Input parameter is null");
		}
	}
	
	/**
	 * Private helper that checks if the link is currently valid. If not, throw an
	 * UninitializedObjectException with the given messages
	 * @param errMsg
	 * @param throwMsg
	 * @throws UninitializedObjectException
	 */
	private void checkLinkIsValid(String errMsg, String throwMsg) throws UninitializedObjectException {
		if (!this.isValid)
			throw new UninitializedObjectException(errMsg, new Throwable(throwMsg));
	}
	
	/**
	 * Helper method to determine if a set has only two unique users.
	 * @param set - The set in question
	 * @return boolean - true if a unique user set, false if not
	 */
	private boolean setIsLegal(Set<User> set) {
		Iterator<User> usrIterator;
		User user1;
		User user2;
		
		this.checkNull(set);
		//If our set has more than two elements, we can skip checking it
		if (set.size() == 2) {
			//Use an iterator to get the two elements in the set
			usrIterator = set.iterator();
			user1 = usrIterator.next();
			user2 = usrIterator.next();
			
			if (!user1.getID().equals(user2.getID())) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Private helper to compare the date against an event list that is one
	 * element in size.
	 * @param date - date to query list with
	 * @return int - Number of elements in list at the date given
	 */
	private int binaryDateComparison(Date date) {
		Date compDate = this.links.get(0);
		this.checkNull(date);
		
		if (date.before(compDate))
			//At that time, there were 0 elements in the list
			return 0;
		else
			//At date, we have one event in the list
			return 1;
		
	}
	
	/**
	 * Private helper method that finds where a date that doesn't exist within links should be
	 * and returns the index of the element that comes before it.
	 * @param date - date in question
	 * @return int - Index of previous element
	 * @throws NullPointerException
	 */
	private int findSizeForDate(Date date) {
		int returnIdx = this.links.lastIndexOf(date) + 1;

		this.checkNull(date);
		
		for (int i = 0; i < this.links.size() - 1; i++) {
			
			if (returnIdx > 0) 
				return returnIdx;
			
			if(this.dateIsBetween(this.links.get(i), date, this.links.get(i + 1))) 
				returnIdx = i + 1;
		}
		
		if(returnIdx <= 0) 
			returnIdx = this.dateFitsAtEnd(date);
		
		return returnIdx;
	}
	
	/**
	 * Private helper that compares two dates against a third date to see if the third
	 * date fits between the two
	 * @param before - Date that should be before the middle date
	 * @param now - The date that is trying to be placed
	 * @param after - Date that should be after the middle date
	 * @return boolean - true if between, false otherwise
	 */
	private boolean dateIsBetween(Date before, Date now, Date after) {
		this.checkNull(before);
		this.checkNull(now);
		this.checkNull(after);
		
		//If it's either before or equal to the prior date and the following date actually comes after
		if ((before.before(now) || before.equals(now)) && after.after(now) && !after.equals(now))
			return true;
		return false;
	}
	
	/**
	 * Private helper that checks if the date comes before or after the last
	 * event in the list and returns the total number of events that occurred
	 * at the given date
	 * @param date - Date being compared
	 * @return int - number of events at the input date
	 */
	private int dateFitsAtEnd(Date date) {
		this.checkNull(date);
		
		Date compDate = this.links.get(this.links.size() - 1);
		//If the input date happened before the last event
		if (compDate.after(date))
			//Then don't count the last event
			return this.links.size() - 1;
		else
			return this.links.size();
	}
	
	

}
