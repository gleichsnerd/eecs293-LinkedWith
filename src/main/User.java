package main;

/**
 * User class for LinkedWith networking program
 * @author Adam Gleichsner (amg188@case.edu)
 */
public class User {

	private String 		id;			//Unique identifiers
	private String		firstName;
	private String		middleName;
	private String		lastName;
	private String		email;
	private String		phoneNumber;
	private boolean		isValid;	//If User has been initialized and set
	
	/**
	 * Class constructor
	 * Creates an invalid User object with no id.
	 */
	public User() {
		this.id = null;
		this.firstName = null;
		this.middleName = null;
		this.lastName = null;
		this.email = null;
		this.phoneNumber = null;
		this.isValid = false;
	}
	
	//Getter Methods
	/**
	 * Grabs and returns the target field
	 * @return String - first name of the user 
	 */
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * Grabs and returns the target field
	 * @return String - middle name of the user 
	 */
	public String getMiddleName() {
		return this.middleName;
	}
	
	/**
	 * Grabs and returns the target field
	 * @return String - last name of the user 
	 */
	public String getLastName() {
		return this.lastName;
	}
	
	/**
	 * Grabs and returns the target field
	 * @return String - email address of the user 
	 */
	public String getEmail() {
		return this.email;
	}
	
	/**
	 * Grabs and returns the target field
	 * @return String - phone number of the user 
	 */
	public String getPhoneNumber() {
		return this.phoneNumber;
	}
	
	/**
	 * Returns the current id of the user, or null if the user isn't valid yet,
	 * as set when User is created.
	 * @return String - id of user, null if invalid user
	 */
	public String getID() {
		return this.id;
	}
	
	// Setter methods
	
	/**
	 * Sets first name of a valid user, throws exception if user is invalid
	 * @param firstName
	 * @return User - the newly modified user
	 * @throws UninitializedObjectException
	 */
	public User setFirstName(String firstName) throws UninitializedObjectException {
		this.checkUserIsValid("User has not been initialized", "Can't set personal field of an invalid user");
		this.checkNull(firstName, id);
		
		this.firstName = firstName;
			
		return this;
	}
	
	/**
	 * Sets middle name of a valid user, throws exception if invalid user
	 * @param middleName
	 * @return User - the newly modified user
	 * @throws UninitializedObjectException
	 */
	public User setMiddleName(String middleName) throws UninitializedObjectException {
		this.checkUserIsValid("User has not been initialized", "Can't set personal field of an invalid user");
		this.checkNull(middleName, id);
		
		this.middleName = middleName;
		
		return this;
	}
	
	/**
	 * Sets last name of valid user, throws exception if user is invalid
	 * @param lastName
	 * @return User - the newly modified user
	 * @throws UninitializedObjectException
	 */
	public User setLastName(String lastName) throws UninitializedObjectException {
		this.checkUserIsValid("User has not been initialized", "Can't set personal field of an invalid user");
		this.checkNull(lastName, id); 
		
		this.lastName = lastName;

		return this;
	}
	
	/**
	 * Sets email of valid user, throws exception if user is invalid
	 * @param email
	 * @return User - the newly modified user
	 * @throws UninitializedObjectException
	 */
	public User setEmail(String email) throws UninitializedObjectException {
		this.checkUserIsValid("User has not been initialized", "Can't set personal field of an invalid user");
		this.checkNull(email, id);
		
		this.email = email;
		
		return this;
	}
	
	/**
	 * Sets phone number of valid user, throws exception if user is invalid
	 * @param phoneNumber
	 * @return User - the newly modified user
	 * @throws UninitializedObjectException
	 */
	public User setPhoneNumber(String phoneNumber) throws UninitializedObjectException {
		this.checkUserIsValid("User has not been initialized", "Can't set personal field of an invalid user");
		this.checkNull(phoneNumber, id);
		
		this.phoneNumber = phoneNumber;

		return this;
	}
	
	/**
	 * Sets the user id if the user is currently invalid and doesn't have an id yet.
	 * If the user is valid and has an id, then we do nothing. If the input id is null,
	 * empty, or we have a valid user with no id, we throw a NullPointerException.
	 * @param id - Unique string identifier for the User
	 * @return boolean - true if id is set, false if not
	 * @throws NullPointerException
	 */
	public boolean setID(String id) throws NullPointerException {
		this.checkNull(id);
		//If our string is not null but empty
		if (id.isEmpty()) {
			throw new NullPointerException("Error: id is an empty string");
		} else if (!this.isValid) {
			//If it's a new user, set him/her up
			this.id = id;
			this.isValid = true;
			return true;
		} else { //Our user is already valid and has an id
			return false;
		}
	}
	
	/**
	 * Returns whether or not the User has been set with an id yet and marked valid
	 * @return Boolean - true if valid, false if not
	 */
	public boolean isValid() {
		return this.isValid;
	}
	
	/**
	 * Static helper that will sort a two user array so that the lower id is first
	 * @param users
	 * @return User array with lowest id first
	 */
	public static User[] twoUserSort (User[] users) {
		User[] returnArray = null;
		//If we have an array greater than two users, do not sort and return null
		if (users.length == 2) {
			if (users[0].getID().compareTo(users[1].getID()) <= 0)
				returnArray = users;
			else
				returnArray = new User[]{users[1], users[0]};
		}
		return returnArray;
	}
	
	/**
	 * Overrides toString method to return a human-readable string about the user.
	 * If the user isn't valid or if he's somehow corrupted and has no id, we return
	 * "Invalid User: Uninitialized ID".
	 * @return String - Special string if invalid or corrupt, id if valid
	 */
	@Override 
	public String toString() {
		//If our user is invalid or somehow corrupted and without an id
		if (!this.isValid() || this.id == null) {
			return "Invalid User: Uninitialized ID";
		}
		return new String("User ID: " + id);
	}
	
	/**
	 * Overrides equals() method to return true if two users share the same id
	 * @param user - User to compare ids against
	 */
	@Override
	public boolean equals(Object user) {
		if (user instanceof User) {
			User objUser = (User) user;
			if (this.id.equals(objUser.getID()))
				return true;
		}
		return false;
	}
	
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
	 * Private helper method that throws exception if the user is invalid
	 * @param errMsg - General error message
	 * @param throwMsg - Message specific to the local error
	 * @throws UninitializedObjectException
	 */
	private void checkUserIsValid(String errMsg, String throwMsg) throws UninitializedObjectException {
		if (!this.isValid)
			throw new UninitializedObjectException(errMsg, new Throwable(throwMsg));
	}
	
	
}
