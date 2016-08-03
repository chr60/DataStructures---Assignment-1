// Christen Reinbeck

import java.util.Arrays;

public class Set<T> implements SetInterface<T> {

	private T[] set;
	private int numberOfEntries;
	private static final int defaultCapacity = 25;

	
	// constructor that creates empty set of 25
	public Set(){
		this(defaultCapacity);
	}
	
	// constructor creates empty set of user given capacity
	// modeled after the array bag constructor in Hydra lab
	public Set(int givenCapacity){
	        @SuppressWarnings("unchecked")
			T[] tempSet = (T[]) new Object[givenCapacity];
			set = tempSet;
			numberOfEntries = 0;

	}
	
	@Override
	//returns number of entries (aka size) of the array so that it can be used in the add method to check if it is full
	public int getCurrentSize() {
		return numberOfEntries;
	}

	@Override
	//returns true if its empty, false if not empty
	public boolean isEmpty() {
		return (numberOfEntries == 0);
	}

	@Override
	// adds and entry as well as dynamically resizes the array if full
	// avoids duplicates
	// if newEntry null, throws IllegalArgumentExcpetion
	// if this set has reached maxCapacity, throws SetFullException
	public boolean add(T newEntry) throws SetFullException, IllegalArgumentException {
		boolean isFull = true;
		int currentSize = getCurrentSize();
		boolean place = contains(newEntry);		// is true if entry is in the set
		// checks if dynamic resize needed
		if(currentSize >= set.length){
			capacityResize();
		}
		
		// checks for null newEntry and throws exception
		if(newEntry == null){
			isFull = false;
			throw new IllegalArgumentException("Entry is null.");
		}
		// checks for duplicate entry in the set - if duplicate doesn't add it and return is false
		// place will equal -1 if not a duplicate in the set
		else if(place == true){
			isFull = false;
		}
		// checks again for if set is at capacity and throws corresponding exception - should never happen bc of dynamic resize
		// should never reach
		else if(currentSize >= set.length){
			isFull = false;
			throw new SetFullException();
		}else{
			// all conditions must be met in order to add newEntry to set
			set[numberOfEntries] = newEntry;
			numberOfEntries++;
		}
		
		return isFull;
	}

	@Override
	// checks to see if entry is in set, if it is then it is removed
	public boolean remove(T entry) throws IllegalArgumentException {
		boolean removed = false; //true if removed, false if not
		int place = getIndex(entry);
		// true if entry not in set
		// false if entry is in set, only will remove if this is false
		
		if(numberOfEntries == 0){
			removed = false;
		}else if(entry == null){
			removed = false;
			throw new IllegalArgumentException("Entry is null.");
		}
		//removes entry
		// place will equal -1 if not in set
		else if(place >= 0 && numberOfEntries != 0){
			set[place] = set[numberOfEntries - 1];	// this replaces the entry you want removed, with the last entry
			set[numberOfEntries - 1] = null;		// this removes the last entry
			removed = true;
		}
		
		return removed;
	}

	@Override
	// removes an unspecified entry from the set (chose the last entry)s
	public T remove() {
		T otherEntry = null;
		int place = numberOfEntries - 1;
		
		if(place >= 0 && numberOfEntries != 0){
			otherEntry = set[place];				// sets the return value to the removed entry
			set[place] = set[numberOfEntries - 1];	// this replaces the entry you want removed, with the last entry
			set[numberOfEntries - 1] = null;		// this removes the last entry
		}
		
		return otherEntry;
	}

	@Override
	// removes all entries from set
	public void clear() {
		while(numberOfEntries != 0){
			remove();
		}
	}

	@Override
	// boolean to see if a set contains this entry
	public boolean contains(T entry) throws IllegalArgumentException {
		boolean isInside = false;		// true if set contains this entry
		int index = getIndex(entry);
		if(index > -1){
			isInside = true;
		}
		return isInside;
	}

	@Override
	public T[] toArray() {
		@SuppressWarnings("unchecked")
		T[] newArr = (T[]) new Object[numberOfEntries];
		for(int index=0; index<numberOfEntries;index++){
			newArr[index] = set[index];
		}
		return newArr;
	}
	
	/* NOT CURRENTLY USED
	//used in add method to check for duplicates
	private boolean checkDuplicate(T newEntry){
		boolean notDuplicate = true;
		int currentSize = getCurrentSize();
		// checking for duplicate entries already in existence, if duplicate then returns false
		for(int index=0; index<currentSize; index++){
			if(newEntry.equals(set[index])){
				notDuplicate = false;
			}
		}
		return notDuplicate;
	}
	*/
	
	// used in add method to dynamically resize
	private void capacityResize(){
		int currentSize = getCurrentSize();
		// checks to see if the current number of entries is greater or equal to the length of the array
		// to determine if the array needs dynamically resized
		if(currentSize >= set.length){
			// by convention, this will double the size/capacity of the array
			// over writes current set array with new set array with double the capacity
			set = Arrays.copyOf(set, 2*set.length);
		}
	}
	
	// used to find index where/if an entry resides
	private int getIndex(T anEntry){
		// make place -1 so that if it isnt found, you get an easily recognized value
		int place = -1, index = 0;
		boolean looking = true;
		while(looking && (index < numberOfEntries)){
			for(int x=0;x<set.length;x++){
				if(anEntry.equals(set[index])){
				looking = false;
				place = index;
				}
			}
			index++;
		}
		return place;
	}

}
