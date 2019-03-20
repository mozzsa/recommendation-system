
import java.util.*;
import org.apache.commons.csv.*;

public class LoadData {
    //This method should process every record from the CSV file whose name is filename, 
    // a file of movie information, and return an ArrayList of type Movie with all of the movie data from the file.
    public List<Movie> loadMovies(String filename){
        ArrayList<Movie> movies = new ArrayList<>();
        ReadData fr = new ReadData("data/" + filename);
        CSVParser movieParser = fr.getCSVParser();
        for (CSVRecord currentRow: movieParser){
            String currId = currentRow.get("id");
            String currTitle = currentRow.get("title");
            String currYear = currentRow.get("year");
            String currGenres = currentRow.get("genre");
            String currDirector = currentRow.get("director");
            String currCountry = currentRow.get("country");
            String currPoster = currentRow.get("poster");
            int currMinutes = Integer.parseInt(currentRow.get("minutes"));
            
            movies.add(new Movie(currId, currTitle, currYear, currGenres, 
                                currDirector, currCountry, currPoster, currMinutes));
        }
        return movies;
    }
    
    public List<Rater> loadRaters(String filename){
        ArrayList<Rater> raters = new ArrayList<>();
        ReadData fr = new ReadData("data/" + filename);
        CSVParser raterParser = fr.getCSVParser();
        for (CSVRecord currentRow: raterParser){
            String raterId = currentRow.get("rater_id");
            String movieId = currentRow.get("movie_id");
            double rating = Double.parseDouble(currentRow.get("rating"));
            
            //Ckeck if the current rater is existed in the ArrayList "raters" or not.
            boolean exist = false;
            for (Rater r: raters){
                if (r.getID().equals(raterId)){
                    exist = true;
                }
            }
            
            if (!exist){
                Rater currRater = new Rater(raterId);
                currRater.addRating(movieId, rating);
                raters.add(currRater);
            } else {
                for (Rater r: raters){
                    if (r.getID().equals(raterId)){
                        r.addRating(movieId, rating);
                    }
                }
            }
        }
        return raters;
    }


    public void findNumOfRatingsByRater(String filename, String raterID){
        List<Rater> raters = loadRaters(filename);
        Rater result = null;
        int num =0;
        for (Rater currRater: raters){
            if (currRater.getID().equals(raterID)){
                result = currRater;
            }
        }
        if(result!=null){
            num = result.numRatings();
            System.out.println("There are " + num + " ratings of " + "ID " + raterID);
        }
        else {
            System.out.println("There is no ratings for this user" );
        }}

    
    public void findMaxNumOfRatingsByRater(String filename){
        List<Rater> raters = loadRaters(filename);
        int max  = Collections.max(raters).numRatings();
        System.out.println("The maximum number of ratings of the rater(s) is " + max + ". Their IDs are:");
        StringBuilder s = new StringBuilder();
        for (Rater currRater: raters){
            if (currRater.numRatings() == max){
                s.append(currRater.getID()+ ", ");
            }
        }
        System.out.println(s);
    }
    
    public void findNumOfRatersOfMovie(String filename, String movieID){
        List<Rater> raters = loadRaters(filename);
        int num = 0;
        for (Rater currRater: raters){
            List<String> movies = currRater.getItemsRated();
            if (movies.contains(movieID)){
                num += 1;
            }
        }
        System.out.println("Movie with ID " + movieID + " was rated by " + num + " raters.");
    }
    
    public void countRatedMovies(String filename){
        List<Rater> raters = loadRaters(filename);
        ArrayList<String> movies = new ArrayList<>();
        for (Rater currRater: raters){
            List<String> currMovies = currRater.getItemsRated();
            for (String s: currMovies){
                if (!movies.contains(s)){
                    movies.add(s);
                }
            }
        }
        System.out.println("There are " + movies.size() + " movies rated.");
    }
    

    public void testLoadMovies(String filename){

            System.out.println("Processing file: " + filename);
            List<Movie> movies = loadMovies(filename);
            System.out.println("There are " + movies.size() + " records.");

            int numComedy = 0;
            for (Movie currMovie: movies){
                if (currMovie.getGenres().indexOf("Comedy") != -1){
                    numComedy += 1;
                }
            }
            System.out.println("There are " + numComedy + " comedy movies in the file.");
            
            int numLength150 = 0;
            for (Movie currMovie: movies){
                if (currMovie.getMinutes() > 150){
                    numLength150 += 1;
                }
            }
            System.out.println("There are " + numLength150 + " movies which their lengths are more than 150 min.\n");
            
            // Remember that some movies may have more than one director.
            HashMap<String, ArrayList<String>> map = new HashMap<>();
            for (Movie currMovie: movies){
                String director = currMovie.getDirector().trim();
                if (director.indexOf(',') == -1){
                    if (!map.containsKey(director)){
                        map.put(director, new ArrayList<String>());
                    }
                    String title = currMovie.getTitle();
                    map.get(director).add(title);
                  
                } else {
                    while (director.indexOf(',') != -1){
                        int idxComma = director.indexOf(',');
                        String currDirector = director.substring(0, idxComma);
                        
                        if (!map.containsKey(currDirector)){
                            map.put(currDirector, new ArrayList<String>());
                        }
                        String title = currMovie.getTitle();
                        map.get(currDirector).add(title);
                        
                        director = director.substring(idxComma+1).trim();
                    }
                }
            }
            
            int maxNumOfMoviesByDirector = 0;
            for (Map.Entry<String,ArrayList<String>> entry: map.entrySet()){
                ArrayList<String> value = entry.getValue();
                if (value.size() > maxNumOfMoviesByDirector){
                    maxNumOfMoviesByDirector = value.size();
                }
            }
            System.out.println("The maximum number of films directed by one director is " + maxNumOfMoviesByDirector);
            
            StringBuilder directorWithMaxMovies = new StringBuilder();
            for (Map.Entry<String,ArrayList<String>> entry: map.entrySet()){
                String key = entry.getKey();
                ArrayList<String> value = entry.getValue();
                if (value.size() == maxNumOfMoviesByDirector){
                    directorWithMaxMovies.append(key+",");
                }
            }
            System.out.println("Names of the directors who directed the maximum number of movies " +
                                directorWithMaxMovies.substring(0, directorWithMaxMovies.length()-2));
    }
    
    public void testLoadRaters(String filename){
        List<Rater> raters = loadRaters(filename);
        System.out.println("There are " + raters.size() + " raters.");
        System.out.println(" ");
        for (Rater currRater: raters){
            System.out.println("Rater ID " + currRater.getID() + ": " + currRater.numRatings() + "number of ratings.");
            List<String> items = currRater.getItemsRated();
            for (String item: items){
                double rating = currRater.getRating(item);
                System.out.print(item + " " + rating + "; ");
            }
            System.out.println("\n");
        }
    }

    public static void main(String[] args) {
        LoadData fr = new LoadData();
        String ratedMovies = "ratedmovies_short.csv";
        String ratings = "ratings_short.csv";
        fr.testLoadMovies(ratedMovies);
        fr.testLoadRaters(ratings);
        fr.findNumOfRatingsByRater(ratings,"2");
        fr.findMaxNumOfRatingsByRater(ratings);
        fr.findNumOfRatersOfMovie(ratings,"0068646");
        fr.countRatedMovies(ratings);
    }
    
    
}
