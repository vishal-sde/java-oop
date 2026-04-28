package SimpleProjects;

import java.util.ArrayList;
import java.util.Scanner;

public class MovieTicketBooking {
    public static void main(String[] args){
        Theatre theatre = new Theatre();
        Scanner scan = new Scanner(System.in);
        int choice;

        //add movies
        theatre.addMovie(new Movie("Mankatha",240,45));
        theatre.addMovie(new Movie("Billa",210,30));
        theatre.addMovie(new Movie("Avengers",500,20));

        do {
            System.out.println("\n ---Movie Ticket Booking System----");
            System.out.println("1.Show movies");
            System.out.println("2.Book Ticket");
            System.out.println("3.Cancel Ticket");
            System.out.println("4.Exit");
            System.out.println("Enter Choice: ");
            choice = scan.nextInt();
            scan.nextLine();

            switch (choice){
                case 1: theatre.showMovies(); break;
                case 2:
                    System.out.println("Enter movie name to book: ");
                    theatre.bookTicket(scan.nextLine());
                    break;
                case 3:
                    System.out.println("Enter movie name to cancel: ");
                    theatre.cancelTicket(scan.nextLine());
                    break;
                case 4:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice!");
            }
        }while(choice != 4);

        scan.close();


    }
}

class Theatre{
    private ArrayList<Movie> movies;

    Theatre(){
        movies = new ArrayList<>();
    }

    void addMovie(Movie shows){
        movies.add(shows);
        System.out.println(shows.getName() + "added to shows.");
    }

    void showMovies(){
        System.out.println("\n----Movies----");
        for(Movie movies: movies){
            System.out.println(movies);
        }
    }

    void bookTicket(String movieName){
        for(Movie movie : movies){
            if(movie.getName().equalsIgnoreCase(movieName)){
                movie.bookTicket();
                return;
            }
        }
        System.out.println("Movie not found!");
    }

    void cancelTicket(String movieName){
        for(Movie movie: movies){
            if(movie.getName().equalsIgnoreCase(movieName)){
                movie.cancelTicket();
                return;
            }
        }
        System.out.println("Movie not found!");
    }


}

class Movie {
    protected String movieName;
    protected int price;
    private int seats;

    Movie(String name, int price,int seats){
        this.movieName = name;
        this.price = price;
        this.seats = seats;
    }

    public String getName(){
        return movieName;
    }

    void bookTicket(){
        if(seats > 0){
            seats --;
            System.out.println("Ticket booked for" + movieName);
        }else{
            System.out.println(movieName + " is Housefull");
        }
    }
    void cancelTicket(){
        seats ++;
        System.out.println("Ticket cancelled for " + movieName);
    }
    @Override
    public String toString(){
        String status = (seats > 0) ? "Available" : "HouseFull";
        return movieName + " | price: " + price + " | seats " + seats + " | " + status;
    }
}

class Viewer {
    private String name;
    private int viewId;

    Viewer(String name, int viewId){
        this.viewId = viewId;
        this.name = name;
    }
}
