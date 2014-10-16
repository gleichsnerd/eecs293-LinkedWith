package main;

/**
 * @author Adam Gleichsner (amg188@case.edu)
 *
 * Friend class that contains a user and a distance of the friendship from the user
 */
public class Friend {

	//Elements of a friend
	private User user;
	private int distance;
	private boolean isValid;
	
	/**
	 * Constructor for friend class, makes an empty friend. If only it were this easy to make real friends.
	 */
	public Friend() {
		this.user = new User();
		this.distance = 0;
		this.isValid = false;
	}
	
	/**
	 * Sets a friend with a user and distance (or degree of friendship) if the friend is invalid and the user is valid
	 * @param user - Person you want to befriend
	 * @param distance - degree of friendship
	 * @return boolean - true if new params set, false if otherwise
	 */
	public boolean set(User user, int distance) {
		this.checkNullInput(user, distance);
		
		//If our friendship is empty but the user is valid, make it
		if (!this.isValid && user.isValid()) {
			this.user = user;
			this.distance = distance;
			this.isValid = true;
			return true;
		} else
			return false;
	}
	
	/**
	 * Getter method to grab the user in the friendship if the friendship is valid, throws an
	 * exception if invalid
	 * @return User - the user param of the friend object
	 * @throws UninitializedObjectException
	 */
	public User getUser() throws UninitializedObjectException {
		this.checkFriendIsValid("Friend is invalid", "Cannot get the user of an invalid friend object");
		
		return this.user;	
	}
	
	/**
	 * Getter for the distance, throws an exception if the friendship is invalid
	 * @return int - distance of friend
	 * @throws UninitializedObjectException
	 */
	public int getDistance() throws UninitializedObjectException {
		this.checkFriendIsValid("Friend is invalid", "Cannot get distance of an invalid friend object");
		
		return this.distance;
	}
	
	/**
	 * Overridden toString method that prints out a human friendly representation of 
	 * the object, returns a special string if the friend is invalid
	 * @return String = string representation of friend
	 */
	@Override
	public String toString() {
		String returnString = null;
		//If our friend is invalid, return a special string, but otherwise print out this formatted stringg
		if (this.isValid) {
			returnString = String.format("Friend %s\nDistance: %d", this.user, this.distance);
		} else
			returnString = "Invalid Friend";
		
		return returnString;
	}
	
	/**
	 * Overridden equals method. Friends are considered equal if they have the same user,
	 * regardless of distance.
	 * @return boolean - True if users are equal, false if otherwise
	 */
	@Override
	public boolean equals(Object object) {
		boolean returnBool = false;
		if (object instanceof Friend) {
			//Create a friend object we can compare against
			Friend friendObj = (Friend) object;
			if (friendObj.user == this.user)
				returnBool = true;
		}
		
		return returnBool;
		
	}
	
	
	//Private Methods
	
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
	 * Private helper method that throws exception if friend is invalid
	 * @param errMsg - General error message
	 * @param throwMsg - Message specific to the local error
	 * @throws UninitializedObjectException
	 */
	private void checkFriendIsValid(String errMsg, String throwMsg) throws UninitializedObjectException {
		if (!this.isValid)
			throw new UninitializedObjectException(errMsg, new Throwable(throwMsg));
	}


}
