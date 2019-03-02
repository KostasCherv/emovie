/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;

import emovie.eMovie;
import java.sql.Statement;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

/**
 *
 * @author kosta
 */
public class DbManager {
     // jdbc Connection
    private static Statement stmt = null;
    private EntityManagerFactory emf; // Το EntityManagerFactory
    public static EntityManager em; // Ο EntityManager 
    
    public DbManager(){
        connectToDb();
    }
    
    public void connectToDb() {
        try {
            // Δημιουργία ενός EntityManagerFactory το οποίο συνδέεται στο
            // Persistence Unit που αντιστοιχεί στην Βάση Δεδομένων μας
            emf = Persistence.createEntityManagerFactory("eMoviePU");
            // Δημιουργία ενός EntityManager
            em = emf.createEntityManager();
        } catch (Exception ex) {
            System.err.println("dbNotConnect");
            System.exit(1);
        }
    }
    
    //μέθοδος διαφραφής δεδομένων πινάκων 
    public static void deleteDataFromTables() {
        try {
            
            if (!em.getTransaction().isActive()) {
                em.getTransaction().begin(); //ξεκινάω μια καινούργια 
                //συναλλαγή για να αποθηκεύσω στη βάση δεδομένων τα δεδομένα
            }
            
            // Διαγραγή των ταινιών από τη βάση και από τον Entity Manager
            List<POJOS.Movie> movieResults = em.createNamedQuery("Movie.findAll", POJOS.Movie.class).getResultList();;
            for(POJOS.Movie t: movieResults){
                em.remove(t);
            }
            
            // Διαγραγή των ειδών από τη βάση και από τον Entity Manager
            List<POJOS.Genre> genreResults = em.createNamedQuery("Genre.findAll", POJOS.Genre.class).getResultList();;
            for(POJOS.Genre t: genreResults){
                em.remove(t);
            }
            
            // Διαγραγή των αγαπημένων λιστών από τη βάση και από τον Entity Manager
            List<POJOS.FavoriteList> favListResults  = em.createNamedQuery("FavoriteList.findAll", POJOS.FavoriteList.class).getResultList();;
            for(POJOS.FavoriteList t: favListResults){
                em.remove(t);
            }
            em.flush();
            em.getTransaction().commit();// τέλος συναλλαγής

            System.out.println("Tables Data deleted");
        } catch (Exception ex) {
            System.err.println("Μη δυνατή η διαγραφη των πινάκων. error :" + ex.toString());
            System.exit(1);
        }
    }    
}
