import java.util.*;

public class Rater implements Comparable<Rater> {
    private String myID;
    private HashMap<String, Rating> myRatings;

    public Rater(String id) {
        myID = id;
        myRatings = new HashMap<>();
    }

    public void addRating(String item, double rating) {
        myRatings.put(item, new Rating(item,rating));
    }

    public boolean hasRating(String item) {
        return myRatings.containsKey(item);
    }

    public String getID() {
        return myID;
    }

    public double getRating(String item) {
        
        for (Map.Entry<String,Rating> entry: myRatings.entrySet()){
            String key =entry.getKey();
            Rating rating =entry.getValue();
            if (key.equals(item)){
                return rating.getValue();
            }
        }
        
        return -1;
    }

    public int numRatings() {
        return myRatings.size();
    }

    public List<String> getItemsRated() {
        ArrayList<String> list = new ArrayList<>();
        for (String s: myRatings.keySet()){
            list.add(s);
        }
        return list;
    }

    @Override
    public int compareTo(Rater r) {
        if(numRatings()<r.numRatings()){
            return -1;
        }
        if(numRatings()>r.numRatings()){
            return 1;
        }
        return 0;
    }



}

