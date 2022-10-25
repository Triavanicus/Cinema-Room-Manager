package cinema;

import java.util.Scanner;

class Main {

    public static void main(String[] args) {
        Cinema cinema = new Cinema();
        cinema.run();
    }
}

public class Cinema {
    public static final int NORMAL_TICKET_PRICE = 10;
    public static final int LOW_TICKET_PRICE = 8;
    public static final int SMALL_CINEMA_CUTOFF = 60;
    private static final String[] MENU = {"Exit", "Show the seats",
            "Buy a ticket", "Statistics"};
    private final Scanner in = new Scanner(System.in);
    private int numRows;
    private int numCols;
    private boolean[][] seats;

    public void run() {
        inputNumSeats();

        while (true) {
            switch (inputMenu()) {
            case 0:
                return;
            case 1:
                printSeats();
                break;
            case 2:
                inputSeatSelection();
                break;
            case 3:
                printStatistics();
                break;
            default:
                System.out.println("Wrong input!");
                System.out.println();
            }
        }
    }

    private int inputInt(String request) {
        if (request != null) {
            System.out.printf("%s:%n", request);
        }
        System.out.print("> ");
        return in.nextInt();
    }

    private void inputNumSeats() {
        while(true){
            numRows = inputInt("Enter the number of rows");
            numCols = inputInt("Enter the number of seats in each row");

            if(numRows > 0 && numCols > 0) {
                break;
            }

            System.out.println("Wrong input!");
        }

        System.out.println();

        seats = new boolean[numRows][numCols];
    }

    private int inputMenu() {
        int result;

        printMenu();
        result = inputInt(null);
        System.out.println();

        return result;
    }

    private void inputSeatSelection() {
        int row;
        int col;
        while (true) {
            row = inputInt("Enter a row number");
            col = inputInt("Enter a seat number in that row");

            System.out.println();

            if(row <= 0 || row > numRows || col <= 0 || col > numCols) {
                System.out.println("Wrong input!");
                System.out.println();
                continue;
            }

            if(seats[row - 1][col - 1]) {
                System.out.println("That ticket has already been purchased!");
                System.out.println();
                continue;
            }

            break;
        }

        seats[row - 1][col - 1] = true;
        printTicketPrice(row);
    }

    private void printTicketPrice(int row) {
        int seatPrice = getSeatPrice(row);

        System.out.printf("Ticket price: $%d%n%n", seatPrice);
    }

    private int getSeatPrice(int row) {
        int seatPrice = NORMAL_TICKET_PRICE;
        if (getTotalSeats() > SMALL_CINEMA_CUTOFF) {
            if (row > numRows / 2) {
                seatPrice = LOW_TICKET_PRICE;
            }
        }
        return seatPrice;
    }

    private void printMenu() {
        for (int i = 1; i < MENU.length; i++) {
            System.out.printf("%d. %s%n", i, MENU[i]);
        }
        System.out.printf("%d. %s%n", 0, MENU[0]);
    }

    private void printSeats() {
        System.out.println("Cinema:");
        System.out.print(" ");
        for (int i = 1; i <= numCols; i++) {
            System.out.printf(" %d", i);
        }
        System.out.println();

        for (int row = 1; row <= numRows; row++) {
            System.out.print(row);
            for (int col = 1; col <= numCols; col++) {
                if (seats[row - 1][col - 1]) {
                    System.out.print(" B");
                } else {
                    System.out.print(" S");
                }
            }
            System.out.println();
        }
        System.out.println();
    }

    private void printStatistics(){
        int purchasedTickets = getNumPurchasedTickets();
        float percentageSold = (float) purchasedTickets / (float) getTotalSeats() * 100.f;
        int currentIncome = getCurrentIncome();
        int totalIncome = getTotalIncome();

        System.out.printf("Number of purchased tickets: %d%n",purchasedTickets);
        System.out.printf("Percentage: %3.2f%%%n", percentageSold);
        System.out.printf("Current income: $%d%n", currentIncome);
        System.out.printf("Total income: $%d%n", totalIncome);
        System.out.println();
    }

    private int getCurrentIncome() {
        int n = 0;

        for (int row = 0; row < numRows; row ++) {
            for (boolean seat : seats[row]) {
                n += seat ? getSeatPrice(row + 1) : 0;
            }
        }

        return n;
    }

    private int getNumPurchasedTickets() {
        int n = 0;
        for (boolean[] row : seats) {
            for (boolean seat : row){
                n += seat ? 1 : 0;
            }
        }

        return n;
    }

    private int getTotalIncome() {
        int totalPrice;
        if (getTotalSeats() <= SMALL_CINEMA_CUTOFF) {
            totalPrice = NORMAL_TICKET_PRICE * getTotalSeats();
        } else {
            int halfRows = numRows / 2;
            int frontPrice = NORMAL_TICKET_PRICE * halfRows * numCols;
            int backPrice = LOW_TICKET_PRICE * (numRows - halfRows) * numCols;
            totalPrice = frontPrice + backPrice;
        }

        return totalPrice;
    }

    private int getTotalSeats() {
        return numRows * numCols;
    }
}