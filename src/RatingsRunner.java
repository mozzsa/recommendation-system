import java.util.*;

public class RatingsRunner {
    
    public RatingsRunner() {
        // default constructor
        this("ratings.csv");
    }
    
    public RatingsRunner(String ratingsfile) {
        RaterDatabase.initialize(ratingsfile);
    }
    
    public int getRaterSize(){
        // return the number of raters.
        return RaterDatabase.size();
    }

    //returns a double representing the average movie rating for this ID
    //If there are not minimalRaters ratings, then it returns 0.0
    private double getAverageByID(String movieID, int minimalRaters){

        int numRatings = 0;
        double totalScore = 0;
        for (Rater currRater: RaterDatabase.getRaters()){
            List<String> currMovies = currRater.getItemsRated();
            for (String s: currMovies){
                if (s.equals(movieID)){
                    numRatings += 1;
                    totalScore += currRater.getRating(movieID);
                }
            }
        }
        if(numRatings==0||numRatings < minimalRaters){
            return 0.0;
        }
        else {
            return totalScore/numRatings;
        }
    }

    //finds the average rating for every movie that has been rated by at least minimalRaters raters
    public List<Rating> getAverageRatings(int minimalRaters){

        List<String> movies = MovieDatabase.filterBy(new TrueFilter());
        ArrayList<Rating> allAverageRatings = new ArrayList<>();
        for (String currMovieID: movies){
            double averageRating = getAverageByID(currMovieID, minimalRaters);
            allAverageRatings.add(new Rating(currMovieID, averageRating));
        }
        return allAverageRatings;
    }

    //create and return an ArrayList of type Rating of all the movies
    //that have at least minimalRaters ratings and satisfies the filter criteria
    public List<Rating> getAverageRatingsByFilter(int minimalRaters, Filter filterCriteria){

        List<String> movieIDs = MovieDatabase.filterBy(filterCriteria);
        ArrayList<Rating> averageRatings = new ArrayList<>();
        for (String s: movieIDs){
            double ratingValue = getAverageByID(s, minimalRaters);
            averageRatings.add(new Rating(s, ratingValue));
        }
        return averageRatings;
    }

    //translate a rating from the scale 0 to 10 to the scale -5 to 5
    //and return the dot product of the ratings of movies that they both rated.
    private double dotProduct(Rater me, Rater r){

        double result = 0.0;
        List<String> myMovieIDs = me.getItemsRated();
        List<String> otherMovieIDs = r.getItemsRated();
        for (String s: myMovieIDs){
            if (otherMovieIDs.contains(s)){
                double myValue = me.getRating(s) - 5;
                double otherValue = r.getRating(s) - 5;
                result += myValue*otherValue;
            }
        }
        return result;
    }
    //this method computes a similarity rating for each rater in the RaterDatabase
    private ArrayList<Rating> getSimilarities(String id){

        ArrayList<Rating> list = new ArrayList<>();
        Rater me = RaterDatabase.getRater(id);
        for (Rater r: RaterDatabase.getRaters()){
            String currOtherID = r.getID();
            if (!currOtherID.equals(id)){
                double currDotProduct = dotProduct(me, r);
                if (currDotProduct > 0.0){
                    list.add(new Rating(currOtherID, currDotProduct));
                }
            }
        }
        Collections.sort(list, Collections.reverseOrder());
        // The instance variables in every object of the arraylist are ID(String) and dotProduct(double).
        return list; 
    }

    //This method should return an ArrayList of type Rating, of movie s and their weighted average ratings
    // using only the top numSimilarRaters with positive ratings and including only those movies
    //that have at least minimalRaters ratings from those most similar
    public List<Rating> getSimilarRatings(String id, int numSimilarRaters, int minimalRaters){
        List<Rating> similarList = getSimilarities(id);
        // Key: Movies' IDs.  Value: RaterID and ratring value.
        HashMap<String, HashMap<String, Double>> recMap = new HashMap<>();
        for (int k=0; k<numSimilarRaters; k++){
            String currRaterID = similarList.get(k).getItem();
            Rater currRater = RaterDatabase.getRater(currRaterID);
            Rater me = RaterDatabase.getRater(id);
            List<String> myMovies = me.getItemsRated();
            List<String> ratedMovies = currRater.getItemsRated();
            for (String currMovie: ratedMovies){
                if(!myMovies.contains(currMovie)){
                if (!recMap.containsKey(currMovie)){
                    HashMap<String, Double> first = new HashMap<>();
                    first.put(currRaterID, currRater.getRating(currMovie));
                    recMap.put(currMovie, first);
                } else {
                    recMap.get(currMovie).put(currRaterID, currRater.getRating(currMovie));
                }}
            }
        }
        
        ArrayList<Rating> result = new ArrayList<>();
        for (Map.Entry<String ,HashMap<String,Double>> entry: recMap.entrySet()){
            String currMovie = entry.getKey();
            HashMap<String, Double> currValueMap = entry.getValue();
            if (currValueMap.size() >= minimalRaters){
                double total = 0.0;
                for (Map.Entry<String,Double> ent: currValueMap.entrySet()){
                    String currRaterID = ent.getKey();
                    double currSimilarRating = 0.0;
                    // Find similar rating for the currRater.
                    for (Rating r: similarList){
                        if (r.getItem().equals(currRaterID)){
                            currSimilarRating = r.getValue();
                        }
                    }
                    total += currValueMap.get(currRaterID)*currSimilarRating;
                }
                double weightedAverage = total/currValueMap.size();
                result.add(new Rating(currMovie, weightedAverage));
            }
        }
        Collections.sort(result, Collections.reverseOrder());
        return result;
    }
    
}
