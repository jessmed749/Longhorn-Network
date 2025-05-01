import java.util.concurrent.Semaphore;

public class ChatThread implements Runnable {
    private final UniversityStudent sender, receiver;
    private final String message;
    private static final Semaphore sem = new Semaphore(1);

    public ChatThread(UniversityStudent sender, UniversityStudent receiver, String message) {
        this.sender = sender;
        this.receiver = receiver;
        this.message = message;
    }

    @Override
    public void run() {
        try {
            sem.acquire();
            sender.addMessage(receiver, "You ➜ " + message);
            receiver.addMessage(sender, sender.getName() + " ➜ " + message);
            System.out.println("Chat: " + sender.getName() + " to " + receiver.getName() + " :: " + message);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        } finally {
            sem.release();
        }
    }
}
