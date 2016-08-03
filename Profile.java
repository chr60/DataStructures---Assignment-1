// Christen Reinbeck

public class Profile implements ProfileInterface {
	
	private String newName;
	private String newAbout;
	Set<Profile> followingSet;
	private int maxFollowers = 1000;
	
	//constructor 1
	public Profile(){
		this.newName = "";
		this.newAbout = "";
		followingSet = new Set<Profile>(maxFollowers);
	}
	
	//constructor 2
	public Profile(String name, String about){
		this.newName = name;
		this.newAbout = about;
		
		//checks for null values for newName and newAbout - if null, initializes them to empty string
		if(this.newName == null){
			this.newName = "";
		}
		if(this.newAbout == null){
			this.newAbout = "";
		}
		followingSet = new Set<Profile>(maxFollowers);
	}
	
	@Override
	public void setName(String newName) throws IllegalArgumentException {
		if(newName == null){
			throw new IllegalArgumentException("Name entered is not valid.");
		}else{
			this.newName = newName;
		}
	}

	@Override
	public String getName() {
		return this.newName;
	}

	@Override
	public void setAbout(String newAbout) throws IllegalArgumentException {
		if(newAbout == null){
			throw new IllegalArgumentException("About me section is not valid.");
		}else{
			this.newAbout = newAbout;
		}
	}

	@Override
	public String getAbout() {
		return this.newAbout;
	}

	@Override
	public boolean follow(ProfileInterface other) {
		boolean result = true;
		try{
			result = this.followingSet.add((Profile)other);
		}catch(SetFullException e){
			System.out.println("SetFullException caught.");
		}
				
		return result;
	}

	@Override
	public boolean unfollow(ProfileInterface other) {
		boolean result = false;
	
		result = this.followingSet.remove((Profile)other);	
		
		return result;
	}

	@Override
	public ProfileInterface[] following(int howMany) {
		// checking to see if howMany is bigger than the array and if so, only print the number of values available in array
		if(howMany>this.followingSet.getCurrentSize()){
			howMany = this.followingSet.getCurrentSize();
		}
		Object[] followingArr = new Object[howMany];
		followingArr = this.followingSet.toArray();
		ProfileInterface[] returnArr = new ProfileInterface[howMany];

		// cast each item of following set to type ProfileInterface
		for(int x=0; x<howMany;x++){
			returnArr[x] = (ProfileInterface)followingArr[x];
		}
		
		return returnArr;
	}

	@Override
	public ProfileInterface recommend() {
		ProfileInterface suggestedFriend = null;
		// creates array of profiles of friends that one of your friends follows
		ProfileInterface [] friendsOfFriends = new ProfileInterface[100];
		// creates an array of profiles that you already follow to compare against
		ProfileInterface [] currentFriends = this.following(followingSet.getCurrentSize());
		friendsOfFriends = currentFriends[0].following(maxFollowers);
		
		for(int i=0;i<currentFriends.length;i++){
			friendsOfFriends = currentFriends[i].following(followingSet.getCurrentSize());
		}
		
		for(int i=0;i<friendsOfFriends.length;i++){
			// checking to see if you are already friends with this other persons friend
			// if you arent, suggests you add this person
			if(this.followingSet.contains((Profile)friendsOfFriends[i])==false && this.equals((Profile)friendsOfFriends[i])==false){
				suggestedFriend = friendsOfFriends[i];
				break;
			}
		}
		
		
		return suggestedFriend;
	}

}
