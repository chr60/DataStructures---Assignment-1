import java.util.Scanner;
import java.util.NoSuchElementException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class SocialClient {
    private static SetInterface<ProfileInterface> users = new Set<ProfileInterface>();
    private static Scanner input = new Scanner(System.in);

    public static void main(String args[]) {
        restore();
        int selection = -1;

        while (selection != 0) {
            System.out.println();
            System.out.println("Social Client Main Menu");
            System.out.println("1. List profiles");
            System.out.println("2. Create a profile");
            System.out.println("3. Show a profile");
            System.out.println("4. Edit a profile");
            System.out.println("5. Follow");
            System.out.println("6. Unfollow");
            System.out.println("7. Recommend someone to follow");
            System.out.println("0. Quit");
            System.out.print("Selection: ");

            try {
                selection = input.nextInt();
            } catch (NoSuchElementException e) {
                selection = -1;
            } catch (IllegalStateException e) {
                selection = -1;
            }
            input.nextLine();

            switch (selection) {
                case 1:
                    list();
                    break;
                case 2:
                    create();
                    break;
                case 3:
                    show();
                    break;
                case 4:
                    edit();
                    break;
                case 5:
                    follow();
                    break;
                case 6:
                    unfollow();
                    break;
                case 7:
                    recommend();
                    break;
                case 0:
                    save();
                    break;
                default:
                    // Invalid, just ignore and let loop again
                    break;
            }
        }
    }

    @SuppressWarnings("unchecked")
	static void restore() {
		try {
			ObjectInputStream restoreStream =
                    new ObjectInputStream(new FileInputStream("SocialClientData.bin"));
			users = (SetInterface<ProfileInterface>)restoreStream.readObject();
		}
		catch(FileNotFoundException e) {
			// File does not exist, no warning
		}
		catch(IOException e) {
			// Couldn't read file properly
            System.out.println("Warning: Could not restore from existing data");
		}
		catch(ClassNotFoundException e) {
			// Couldn't cast to correct type
            System.out.println("Warning: Could not restore from existing data");
		}
    }

    static void save() {
        try {
            ObjectOutputStream saveStream =
                    new ObjectOutputStream(new FileOutputStream("SocialClientData.bin"));
            saveStream.writeObject(users);
        }
        catch(IOException e) {
            // Couldn't save
            System.out.println("Error: Could not save data");
            e.printStackTrace();
        }
    }

    public static void list() {
        Object[] all = users.toArray();
        int i = 1;
        System.out.println();
        for (Object o : all) {
            ProfileInterface p = (ProfileInterface)o;
            System.out.println(i++ + ". " + p.getName());
        }
    }

    public static void create() {
        System.out.print("Name: ");
        String name = input.nextLine();
        System.out.print("About: ");
        String about = input.nextLine();
        try {
            users.add(new Profile(name, about));
        } catch (SetFullException e) {
            // Set should resize, should never be full
        }
    }

    public static void show() {
        ProfileInterface p = pick();
        if (p == null) return;
        System.out.println(p.getName());
        System.out.println("About: " + p.getAbout());
        System.out.println("Following:");
        ProfileInterface[] following = p.following(4);
        for (ProfileInterface p2 : following) {
            System.out.println(p2.getName());
        }
    }

    public static ProfileInterface pick() {
        return pick("Selection: ");
    }

    public static ProfileInterface pick(String theLine) {
        if (users.isEmpty()) return null;
        System.out.println();
        Object[] all = users.toArray();
        int i = 1;
        for (Object o : all) {
            ProfileInterface p = (ProfileInterface)o;
            System.out.println(i++ + ". " + p.getName());
        }
        System.out.print(theLine);

        int selection = i;
        try {
            selection = input.nextInt();
        } catch (NoSuchElementException e) {
            // Not an int, set selection out of range
            selection = i;
        } catch (IllegalStateException e) {
            // Should never happen
            // Set selection out of range just in case
            selection = i;
        }
        input.nextLine();

        if (selection > 0 && selection < i) {
            return (ProfileInterface)all[selection-1];
        } else {
            return null;
        }
    }

    public static void edit() {
        ProfileInterface p = pick();
        if (p == null) return;
        System.out.println(p.getName());
        System.out.println("About: " + p.getAbout());
        System.out.print("New About: ");
        p.setAbout(input.nextLine());
    }

    public static void follow() {
        ProfileInterface p1 = pick("Who does the following? ");
        if (p1 == null) return;
        ProfileInterface p2 = pick("Follow whom? ");
        if (p2 == null || p1 == p2) return;
        p1.follow(p2);
    }

    public static void unfollow() {
        ProfileInterface p1 = pick("Who does the unfollowing? ");
        if (p1 == null) return;
        ProfileInterface p2 = pick("Unfollow whom? ");
        if (p2 == null || p1 == p2) return;
        p1.unfollow(p2);
    }

    public static void recommend() {
        ProfileInterface p1 = pick("Recommend for whom? ");
        if (p1 == null) return;
        ProfileInterface p2 = p1.recommend();
        if (p2 == null) {
            System.out.println("No recommendation");
        } else {
            System.out.println("Recommend to follow " + p2.getName());
        }
    }
}

