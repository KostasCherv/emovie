package POJOS;

import POJOS.Movie;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-03-02T00:41:39")
@StaticMetamodel(Genre.class)
public class Genre_ { 

    public static volatile CollectionAttribute<Genre, Movie> movieCollection;
    public static volatile SingularAttribute<Genre, String> name;
    public static volatile SingularAttribute<Genre, Integer> id;

}