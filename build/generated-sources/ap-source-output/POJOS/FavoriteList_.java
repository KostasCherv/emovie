package POJOS;

import POJOS.Movie;
import javax.annotation.Generated;
import javax.persistence.metamodel.CollectionAttribute;
import javax.persistence.metamodel.SingularAttribute;
import javax.persistence.metamodel.StaticMetamodel;

@Generated(value="EclipseLink-2.5.2.v20140319-rNA", date="2019-02-27T19:27:16")
@StaticMetamodel(FavoriteList.class)
public class FavoriteList_ { 

    public static volatile CollectionAttribute<FavoriteList, Movie> movieCollection;
    public static volatile SingularAttribute<FavoriteList, String> name;
    public static volatile SingularAttribute<FavoriteList, Integer> id;

}