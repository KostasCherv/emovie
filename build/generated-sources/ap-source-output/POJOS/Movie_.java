package POJOS;

import POJOS.FavoriteList;
import POJOS.Genre;
import java.util.Date;
import javax.annotation.Generated;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-02-27T19:27:16")
@StaticMetamodel(Movie.class)
public class Movie_ { 

    public static volatile SingularAttribute<Movie, Genre> genreId;
    public static volatile SingularAttribute<Movie, String> overview;
    public static volatile SingularAttribute<Movie, FavoriteList> favoriteListId;
    public static volatile SingularAttribute<Movie, Date> releaseDate;
    public static volatile SingularAttribute<Movie, Double> rating;
    public static volatile SingularAttribute<Movie, Integer> id;
    public static volatile SingularAttribute<Movie, String> title;

}