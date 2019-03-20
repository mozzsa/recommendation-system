import java.util.*;

public class MovieRunner {
    private RatingsRunner fourth;
    
    public MovieRunner(){
        fourth = new RatingsRunner("ratings.csv");
        MovieDatabase.initialize("ratedmoviesfull.csv");
    }
    
    public void printAverageRatings(){

        System.out.println("There are " + MovieDatabase.size() + " movies in the file.");
        System.out.println("There are " + fourth.getRaterSize() + " raters in the file.");
        
        int numRating = 35;
        List<Rating> ratings = fourth.getAverageRatings(numRating);
        Collections.sort(ratings);
        
        int num = 0;
        System.out.println("Rating values of Movies with at least " + numRating + " ratings:");
        for (Rating currRating: ratings){
            double currValue = currRating.getValue();
            if(currValue != 0.0){
                num += 1;
                String currMovieID = currRating.getItem();
                System.out.println(currValue + "  " + MovieDatabase.getTitle(currMovieID));
            }
        }
        System.out.println("There are " + num + " movies have at least " + numRating + " ratings.");
    }

    
    public void printSimilarRatings(){
        System.out.println("There are " + MovieDatabase.size() + " movies in the file.");
        System.out.println("There are " + fourth.getRaterSize() + " raters in the file.\n");
        List<Rating> list = fourth.getSimilarRatings("337", 10, 3);
        for (Rating r: list){
            System.out.println(MovieDatabase.getTitle(r.getItem()) + " : " + r.getValue());
        }
        System.out.println("\nThere are " + list.size() + " recommended movies were found.");
    }
    
    public void printSimilarRatingsByGenre(){
        System.out.println("There are " + MovieDatabase.size() + " movies in the file.");
        System.out.println("There are " + fourth.getRaterSize() + " raters in the file.\n");
        Filter genreFilter = new GenreFilter("Mystery");
        List<String> movieIDs = MovieDatabase.filterBy(genreFilter);
        List<Rating> list = fourth.getSimilarRatings("964", 20, 5);
        int num = 0;
        for (Rating r: list){
            if (movieIDs.contains(r.getItem())){
                System.out.println(MovieDatabase.getTitle(r.getItem()) + " : " + r.getValue());
                System.out.println("    " + MovieDatabase.getGenres(r.getItem()));
                num += 1;
            }
        }
        System.out.println("\nThere are " + num + " recommended movies were found.");
    }
    
    public void printSimilarRatingsByDirector(){
        System.out.println("There are " + MovieDatabase.size() + " movies in the file.");
        System.out.println("There are " + fourth.getRaterSize() + " raters in the file.\n");
        Filter directorFilter = new DirectorsFilter("Clint Eastwood,J.J. Abrams,Alfred Hitchcock,Sydney Pollack,David Cronenberg,Oliver Stone,Mike Leigh");
        List<String> movieIDs = MovieDatabase.filterBy(directorFilter);
        List<Rating> list = fourth.getSimilarRatings("120", 10, 2);
        int num = 0;
        for (Rating r: list){
            if (movieIDs.contains(r.getItem())){
                System.out.println(MovieDatabase.getTitle(r.getItem()) + " : " + r.getValue());
                System.out.println("    " + MovieDatabase.getDirector(r.getItem()));
                num += 1;
            }
        }
        System.out.println("\nThere are " + num + " recommended movies were found.");
    }
    
    public void printSimilarRatingsByGenreAndMinutes(){
        System.out.println("There are " + MovieDatabase.size() + " movies in the file.");
        System.out.println("There are " + fourth.getRaterSize() + " raters in the file.\n");
        Filter genreFilter = new GenreFilter("Drama");
        Filter minutesFilter = new MinutesFilter(80, 160);
        AllFilters all = new AllFilters();
        all.addFilter(genreFilter);
        all.addFilter(minutesFilter);
        List<String> movieIDs = MovieDatabase.filterBy(all);
        
        List<Rating> list = fourth.getSimilarRatings("168", 10, 3);
        int num = 0;
        for (Rating r: list){
            if (movieIDs.contains(r.getItem())){
                System.out.println(MovieDatabase.getTitle(r.getItem()) + " : " + "Duration-" + MovieDatabase.getMinutes(r.getItem()) 
                                   + " Rating-" + r.getValue());
                System.out.println("    " + MovieDatabase.getGenres(r.getItem()));
                num += 1;
            }
        }
        System.out.println("\nThere are " + num + " recommended movies were found.");
    }
    
    public void printSimilarRatingsByYearAfterAndMinutes(){
        System.out.println("There are " + MovieDatabase.size() + " movies in the file.");
        System.out.println("There are " + fourth.getRaterSize() + " raters in the file.\n");
        Filter yearAfterFilter = new YearAfterFilter(1975);
        Filter minutesFilter = new MinutesFilter(70, 200);
        AllFilters all = new AllFilters();
        all.addFilter(yearAfterFilter);
        all.addFilter(minutesFilter);
        List<String> movieIDs = MovieDatabase.filterBy(all);
        
        List<Rating> list = fourth.getSimilarRatings("314", 10, 5);
        int num = 0;
        for (Rating r: list){
            if (movieIDs.contains(r.getItem())){
                System.out.println(MovieDatabase.getTitle(r.getItem()) + " : " + "Duration-" + MovieDatabase.getMinutes(r.getItem()) 
                                   + " Rating-" + r.getValue());
                System.out.println("    " + MovieDatabase.getYear(r.getItem()));
                num += 1;
            }
        }
        System.out.println("\nThere are " + num + " recommended movies were found.");
    }
}
