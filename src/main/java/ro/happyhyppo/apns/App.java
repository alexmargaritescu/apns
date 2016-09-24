package ro.happyhyppo.apns;

public class App {
    public static void main(String[] args) {
        new Thread(new APNService(7779)).start();
        new Thread(new FeedbackService(7778)).start();
    }
}
