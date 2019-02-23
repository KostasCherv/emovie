/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import static view.mainUI.url;

/**
 *
 * @author rodius
 */
public class Methods {
 
  public static String readFromURL(String webPage) {
        StringBuffer sb = new StringBuffer();

        try {
            //δημιουργία url από string
            URL uri = new URL(webPage);
            //σύνδεση με το url
            URLConnection urlConnection = uri.openConnection();
            //γέφυρα μετατροπής από bytes σε chars του αποτελέσματος
            InputStreamReader isr;
            try (InputStream is = urlConnection.getInputStream()) {
                isr = new InputStreamReader(is);
                int numCharsRead;
                char[] charArray = new char[1024];
                while ((numCharsRead = isr.read(charArray)) > 0) {
                    sb.append(charArray, 0, numCharsRead);
                }
            }
            //κλείσιμο της γέφυρας
            isr.close();
        } catch (IOException e) { //αν δεν μπορεί να κάνει την μετατροπή
            System.err.println(e.toString());
        }
        //επιστροφή του αποτελέσματος σε string
        return sb.toString();
    }
 
    //μέθοδος άντλησης 
    public static void getMovieGenres() {
        try {
            EntityManager em;
            em = mainUI.em;
            String json;   
            Gson gson = new GsonBuilder().create();
            model.GenresResponse jsonResponse;
            List<Integer> genreIds = new ArrayList<>(Arrays.asList(28, 10749, 878));
            //σύνδεση με το api και άντληση όλων των ειδών ταινιών
            String webPage = url + "genre/movie/list?";
            webPage += mainUI.apiKey;
            json = readFromURL(webPage);

            //η αποκωδικοποίηση της μορφής του json
            jsonResponse = gson.fromJson(json, model.GenresResponse.class);
            // Καταχώσηση δεδομένων για κάθε είδος
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin(); //ξεκινάω μια καινούργια 
                //συναλλαγή για να αποθηκεύσω στη βάση δεδομένων τα 
            }
            //για κάθε στοιχείο λίστα του json άνληση δεδομένων 
            //και αποθήκευση στην Β.Δ.
            for (model.Genre element : jsonResponse.genres){
                if(genreIds.contains(element.getId())){
                    // δημιουργία αντικειμένου κάθε είδους
                    model.Genre genre = new model.Genre();  
                    genre.setId(element.getId());
                    genre.setName(element.getName());
                   
                    em.merge(genre);// δημιουργώ τo query εισαγωγής/μεταβολής για το cw        
                    
                }   
            };
            em.flush();
            em.getTransaction().commit();// τέλος συναλλαγής
            System.out.println("Genders Fetched and Saved");
        } catch (Exception ex) {
            System.err.println("Αδυναμία αποθήκευσης είδη ταινιών. error :"
                    + ex.toString());
            System.exit(1);
        }
    }  
    
    //μέθοδος άντλησης τρέχοντων καιρικών συνθηκών από το api
    //για όλες τις πόλεις και καταχώρησή τους στη Β.Δ.
    public static void getMovies() {
        try {
            String json;   
            Gson gson = new GsonBuilder().create();
            model.MoviesResponse jsonResponse;
            //σύνδεση με το api και άντληση όλων των ειδών ταινιών
            String webPage = url + "discover/movie?with_genres=28,878,10749&primary_release_date.gte=2000-01-01";
            webPage += mainUI.apiKey;
            json = readFromURL(webPage + "&page=1");
            //η αποκωδικοποίηση της μορφής του json
            jsonResponse = gson.fromJson(json, model.MoviesResponse.class);
            
            saveMoviesOnDb(jsonResponse);
            for(int i = 2; i <= jsonResponse.total_pages; i++){
                json = readFromURL(webPage + "&page=" + i);
                jsonResponse = gson.fromJson(json, model.MoviesResponse.class);
                saveMoviesOnDb(jsonResponse);
            }    
            System.out.println("Movies Fetched and Saved");
        } catch (Exception ex) {
            System.err.println("Αδυμανία αποθήκευσης ταινιών. error :"
                    + ex.toString());
            System.exit(1);
        }
    }
    
    //για κάθε στοιχείο λίστα του json άνληση δεδομένων 
    //και αποθήκευση στην Β.Δ.
    public static void saveMoviesOnDb(model.MoviesResponse movies){
        List<Integer> genreIds = new ArrayList<>(Arrays.asList(28, 10749, 878));
        
        EntityManager em;
        em = mainUI.em;
    // Καταχώσηση δεδομένων
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin(); //ξεκινάω μια καινούργια 
            //συναλλαγή για να αποθηκεύσω στη βάση δεδομένων τα δεδομένα
        }
        for (model.MovieResponse element : movies.results){
            // δημιουργία αντικειμένου κάθε ταινίας
            model.Movie movie = new model.Movie();  
            movie.setId(element.id);
            movie.setTitle(element.title);
            movie.setReleaseDate(element.release_date);
            movie.setRating(element.rating);
            movie.setOverview(element.overview.substring(0, Math.min(element.overview.length(), 500))); // shrink string

            for (int id : element.genre_ids) {
               if(genreIds.contains(id)){
                   movie.setGenreId(em.getReference(model.Genre.class, id));
                   break;
               }
            }
            
            em.merge(movie);// δημιουργώ τo query εισαγωγής/μεταβολής   
        }  
        em.flush();
        em.getTransaction().commit();// τέλος συναλλαγής

  }
    
  //μέθοδος διαφραφής δεδομένων πινάκων 
    public static void deleteDataFromTables() {
        try {
            EntityManager em;
            em = mainUI.em;

            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin(); //ξεκινάω μια καινούργια 
                //συναλλαγή για να αποθηκεύσω στη βάση δεδομένων τα δεδομένα
            }
            
            // Διαγραγή των ταινιών από τη βάση και από τον Entity Manager
            TypedQuery<model.Movie> movieQuery = em.createNamedQuery("Movie.findAll", model.Movie.class);
            List<model.Movie> movieResults = movieQuery.getResultList();
            for(model.Movie t: movieResults){
                em.remove(t);
            }
            
            // Διαγραγή των ειδών από τη βάση και από τον Entity Manager
            TypedQuery<model.Genre> genreQuery = em.createNamedQuery("Genre.findAll", model.Genre.class);
            List<model.Genre> genreResults = genreQuery.getResultList();
            for(model.Genre t: genreResults){
                em.remove(t);
            }
            
            // Διαγραγή των αγαπημένων λιστών από τη βάση και από τον Entity Manager
            TypedQuery<model.FavoriteList> favListQuery = em.createNamedQuery("FavoriteList.findAll", model.FavoriteList.class);
            List<model.FavoriteList> favListResults = favListQuery.getResultList();
            for(model.FavoriteList t: favListResults){
                em.remove(t);
            }
            
            em.getTransaction().commit();// τέλος συναλλαγής

            System.out.println("Tables Data deleted");
        } catch (Exception ex) {
            System.err.println("Μη δυνατή η διαγραφη των πινάκων. error :" + ex.toString());
            System.exit(1);
        }
    }    
    
    
}
