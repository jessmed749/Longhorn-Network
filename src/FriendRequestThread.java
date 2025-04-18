import java.util.concurrent.Semaphore;

public class FriendRequestThread implements Runnable {
    private UniversityStudent sender;
    private UniversityStudent receiver;

    private static final Semaphore semaphore = new Semaphore(1);

    public FriendRequestThread(UniversityStudent sender, UniversityStudent receiver) {
        // Constructor
        this.sender = sender;
        this.receiver = receiver;
    }

    @Override
    public void run() {
        // Method signature only
        try {
            semaphore.acquire();
            System.out.println("FriendRequest (Thread-Safe): " + sender.name + " sent a friend request to " + receiver.name);
        } catch (InterruptedException e) {
            // Handle exception
            Thread.currentThread().interrupt(); // Restore interrupted status
            System.err.println("Thread interrupted: " + e.getMessage());
        } finally {
            semaphore.release();
        }
    }
}
