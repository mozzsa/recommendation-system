
/**
 * Write a description of RecommendationRunner here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */

import java.util.*;

public class RecommendationRunner {
    private Random myRandom;
    private int toRateNum;
    private int numSimilarRaters;
    private int minimalRaters;
    private int maxRecNum;
    
    public RecommendationRunner(){
        myRandom = new Random();
        toRateNum = 10;
        numSimilarRaters = 10;
        minimalRaters = 3;
        maxRecNum = 20;
    }
    
    public List<String> getItemsToRate(){
        MovieDatabase.initialize("ratedmoviesfull.csv");
        ArrayList<String> toRate = new ArrayList<>();
        Filter f = new TrueFilter();
        List<String> allMovies = MovieDatabase.filterBy(f);
        for (int k=0; k<toRateNum; k++){
            int currIdx = myRandom.nextInt(MovieDatabase.size());
            String currMovieID = allMovies.get(currIdx);
            toRate.add(currMovieID);
        }

        return toRate;
    }
    
    public void printRecommendationsFor (String webRaterID){
        MovieDatabase.initialize("ratedmoviesfull.csv");
        RatingsRunner fourth = new RatingsRunner();
        List<Rating> result = fourth.getSimilarRatings(webRaterID, numSimilarRaters, minimalRaters);
        int num = result.size();
        if (num == 0){
            System.out.println("Recommendation List:");
            System.out.println("Not Found");
        } else {
            if (num > maxRecNum){
                num = maxRecNum;
            }
            String header = ("<table> <tr> <th>Title</th> <th>Rating Value</th> </tr>");
            StringBuilder body = new StringBuilder();
            for (int k=0; k<num; k++){
                Rating currRating = result.get(k);
                String currMovieID = currRating.getItem();
                String currMovieTitle = MovieDatabase.getTitle(currMovieID);
                double currRatingValue = currRating.getValue();
                String currGenre = MovieDatabase.getGenres(currMovieID);
                body.append(printOut(currMovieTitle, currRatingValue, currGenre));
            }
            System.out.println(header + body + "</table>");
        }
    }
    
    private String printOut(String title, double value,String genre){
        return ("<tr> <td>" + title + "</td> <td>" + Double.toString(value) + "</td> <td>" + genre +"</td></tr>");
    }
}
