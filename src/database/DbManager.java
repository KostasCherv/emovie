/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package database;


import java.sql.Statement;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

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

        Query query = em.createNativeQuery("DELETE FROM MOVIE");

        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
            

        query = em.createNativeQuery("DELETE FROM GENRE");

        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();
        
        query = em.createNativeQuery("DELETE FROM FAVORITE_LIST");

        em.getTransaction().begin();
        query.executeUpdate();
        em.getTransaction().commit();

        System.out.println("Tables Data deleted");
        
        } catch (Exception ex) {
            System.err.println("Μη δυνατή η διαγραφη των πινάκων. error :" + ex.toString());
            System.exit(1);
        }
    }    
}
