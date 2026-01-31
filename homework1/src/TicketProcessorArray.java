package homework1.src;

import java.util.ArrayList;
import java.util.List;

public class TicketProcessorArray {
    public static void main(String[] args) {
        processTicketsArrayList();

    }

    public static void processTicketsArrayList() {

        ArrayList<String> ticketQueue = new ArrayList<>();

        // Uncomment the queue length you want to test with
        createShortQueue(ticketQueue);
        // createLongQueue(ticketQueue);

        
        while (!ticketQueue.isEmpty()) {
            // grab the first item in the list
            int cur=ticketQueue.size();
            String tmp=ticketQueue.get(0);
            ticketQueue.set(0,ticketQueue.get(cur-1));
            ticketQueue.set(cur-1,tmp);
            String currentTicket = ticketQueue.remove(cur-1); 
            
            System.out.println("Processing: " + currentTicket);

            System.out.println("Finished! Remaining in line: " + ticketQueue.size());
            System.out.println("---------------------------");
        }
    }

    public static void createShortQueue(List<String> queue) {
        // feel free to change the number of tickets here to test different queue sizes
        for (int i = 1; i <= 50; i++) {
            queue.add("Ticket #" + i);
        }
    }

    public static void createLongQueue(List<String> queue) {
        // feel free to change the number of tickets here to test different queue sizes
        for (int i = 1; i <= 20000; i++) {
            queue.add("Ticket #" + i);
        }
    }
}