package main;

/**
 * @author Adam Gleichsner (amg188@case.edu)
 * Helper class to allow us to return the status codes from our
 * establish/tear down methods without actually having to return
 * the code itself.
 * This is necessary because Java is dumb and doesn't allow explicit parameter
 * passing by reference.
 */
public class SocialNetworkStatus {

	public enum StatusCode {
		SUCCESS, 
		ALREADY_VALID, ALREADY_ACTIVE, ALREADY_INACTIVE,
		INVALID_USERS, INVALID_DATE, INVALID_DISTANCE
		
	}
	
	SocialNetworkStatus.StatusCode status;		//Keep track of our current status
	
	/**
	 * Create a new wrapper class and initiate status as null
	 */
	public SocialNetworkStatus() {
		this.status = null;
	}
	
	/**
	 * Grab the current status and return it
	 * @return SocialNetworkStatus - Status result from an operation
	 */
	public SocialNetworkStatus.StatusCode getStatus() {
		return this.status;
	}
	
	/**
	 * Sets the status given a desired input
	 * @param status - Status we want to keep track of
	 * @return boolean - true if set, false otherwise
	 */
	public boolean setStatus(SocialNetworkStatus.StatusCode status) {	
		this.status = status;
		return true;
	}
	
	

}
