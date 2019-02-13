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
import static view.MainForm.url;

/**
 *
 * @author kosta
 */
public class Utils {
    
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
 
    //μέθοδος άντλησης τρέχοντων καιρικών συνθηκών από το api
    //για όλες τις πόλεις και καταχώρησή τους στη Β.Δ.
    public static void getMovieGenres() {
        try {
            EntityManager em;
            em = MainForm.em;
            String json;   
            Gson gson = new GsonBuilder().create();
            model.GenresResponse jsonResponse;
            List<Integer> genreIds = new ArrayList<>(Arrays.asList(28, 10749, 878));
            //σύνδεση με το api και άντληση όλων των ειδών ταινιών
            String webPage = url + "genre/movie/list?";
            webPage += MainForm.apiKey;
            json = readFromURL(webPage);
            
            //η αποκωδικοποίηση της μορφής του json
            jsonResponse = gson.fromJson(json, model.GenresResponse.class);

            //για κάθε στοιχείο λίστα του json άνληση δεδομένων 
            //και αποθήκευση στην Β.Δ.
            for (model.Genre element : jsonResponse.genres){
                if(genreIds.contains(element.getId())){
                    // δημιουργία αντικειμένου κάθε είδους
                    model.Genre genre = new model.Genre();  
                    genre.setId(element.getId());
                    genre.setName(element.getName());
                    // Καταχώσηση δεδομένων για κάθε είδος
                    if (!em.getTransaction().isActive()) {
                        em.getTransaction().begin(); //ξεκινάω μια καινούργια 
                        //συναλλαγή για να αποθηκεύσω στη βάση δεδομένων τα 
                        //αντικείμενα cw, cwPK
                    }
                    em.merge(genre);// δημιουργώ τo query εισαγωγής/μεταβολής για το cw        
                    em.flush();
                    em.getTransaction().commit();// τέλος συναλλαγής
                }   
            };
        } catch (Exception ex) {
            System.err.println("Μη δυνατή η σύνδεση με τo API. error :"
                    + ex.toString());
            System.exit(1);
        }
    }
}
