/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package controller;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import emovie.eMovie;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javax.persistence.EntityManager;

/**
 *
 * @author kosta
 */
public class ApiController {
   //Το prefix της συνδεσης με το API
    public static String url = "https://api.themoviedb.org/3/";
    //to API key
    public final static String apiKey = "&api_key=5b0b2dc0ebd5b3f8f87d1d5222304db2";
    public static EntityManager em = eMovie.em;
    
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
            String json;   
            Gson gson = new GsonBuilder().create();
            POJOS.GenresResponse jsonResponse;
            List<Integer> genreIds = new ArrayList<>(Arrays.asList(28, 10749, 878));
            //σύνδεση με το api και άντληση όλων των ειδών ταινιών
            String webPage = url + "genre/movie/list?";
            webPage += apiKey;
            json = readFromURL(webPage);

            //η αποκωδικοποίηση της μορφής του json
            jsonResponse = gson.fromJson(json, POJOS.GenresResponse.class);
            // Καταχώσηση δεδομένων για κάθε είδος
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin(); //ξεκινάω μια καινούργια 
                //συναλλαγή για να αποθηκεύσω στη βάση δεδομένων τα 
            }
            //για κάθε στοιχείο λίστα του json άνληση δεδομένων 
            //και αποθήκευση στην Β.Δ.
            for (POJOS.Genre element : jsonResponse.genres){
                if(genreIds.contains(element.getId())){
                    // δημιουργία αντικειμένου κάθε είδους
                    POJOS.Genre genre = new POJOS.Genre();  
                    genre.setId(element.getId());
                    genre.setName(element.getName());
                   
                    em.persist(genre);// δημιουργώ τo query εισαγωγής       
                    
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
    

    public static void getMovies() {
        try {
            String json;   
            Gson gson = new GsonBuilder().create();
            POJOS.MoviesResponse jsonResponse;
            //σύνδεση με το api και άντληση όλων των ειδών ταινιών
            String webPage = url + "discover/movie?with_genres=28|878|10749&primary_release_date.gte=2000-01-01&api_key=5b0b2dc0ebd5b3f8f87d1d5222304db2";
           // webPage += apiKey;
            
            
            json = readFromURL(webPage + "&page=1");
            //η αποκωδικοποίηση της μορφής του json
            jsonResponse = gson.fromJson(json, POJOS.MoviesResponse.class);
            
            saveMoviesOnDb(jsonResponse);
            for(int i = 2; i < 40; i++){
                json = readFromURL(webPage + "&page=" + i);
                jsonResponse = gson.fromJson(json, POJOS.MoviesResponse.class);
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
    public static void saveMoviesOnDb(POJOS.MoviesResponse movies){
        List<Integer> genreIds = new ArrayList<>(Arrays.asList(28, 10749, 878));
        
        // Καταχώσηση δεδομένων
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin(); //ξεκινάω μια καινούργια 
            //συναλλαγή για να αποθηκεύσω στη βάση δεδομένων τα δεδομένα
        }
        for (POJOS.MovieResponse element : movies.results){
            // δημιουργία αντικειμένου κάθε ταινίας
            POJOS.Movie movie = new POJOS.Movie();  
            movie.setId(element.id);
            movie.setTitle(element.title);
            movie.setReleaseDate(element.release_date);
            movie.setRating(element.rating);
            movie.setOverview(element.overview.substring(0, Math.min(element.overview.length(), 500))); // shrink string

            for (int id : element.genre_ids) {
               if(genreIds.contains(id)){
                  movie.setGenreId(em.getReference(POJOS.Genre.class, id));
                  break;
               }
            }
            
            em.persist(movie);// δημιουργώ τo query εισαγωγής   
        }  
        em.flush();
        em.getTransaction().commit();// τέλος συναλλαγής

  }
}
