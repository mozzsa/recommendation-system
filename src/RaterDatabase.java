


import java.util.*;
import org.apache.commons.csv.*;

public class RaterDatabase {
    private static HashMap<String,Rater> ourRaters;
     
	private static void initialize() {
	    // this method is only called from addRatings 
		if (ourRaters == null) {
			ourRaters = new HashMap<>();
		}
	}

    public static void initialize(String filename) {
 		if (ourRaters == null) {
 			ourRaters= new HashMap<>();
 			addRatings(filename);
 		}
 	}	
 	
    public static void addRatings(String filename) {
        initialize(); 
        ReadData fr = new ReadData(filename);
        CSVParser csvp = fr.getCSVParser();
        for(CSVRecord rec : csvp) {
                String id = rec.get("rater_id");
                String item = rec.get("movie_id");
                String rating = rec.get("rating");
                addRaterRating(id,item,Double.parseDouble(rating));
        } 
    }
    
    public static void addRaterRating(String raterID, String movieID, double rating) {
        initialize(); 
        Rater rater =  null;
                if (ourRaters.containsKey(raterID)) {
                    rater = ourRaters.get(raterID); 
                } 
                else { 
                    rater = new Rater(raterID);
                    ourRaters.put(raterID,rater);
                 }
                 rater.addRating(movieID,rating);
    } 
	         
    public static Rater getRater(String id) {
    	initialize();
    	
    	return ourRaters.get(id);
    }
    
    public static List<Rater> getRaters() {
    	initialize();
    	return new ArrayList<>(ourRaters.values());
    }
 
    public static int size() {
	    return ourRaters.size();
    }
    
    
        
}
