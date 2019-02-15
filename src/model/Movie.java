/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author kosta
 */
@Entity
@Table(name = "MOVIE")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Movie.findAll", query = "SELECT m FROM Movie m"),
    @NamedQuery(name = "Movie.findById", query = "SELECT m FROM Movie m WHERE m.id = :id"),
    @NamedQuery(name = "Movie.findByOverview", query = "SELECT m FROM Movie m WHERE m.overview = :overview"),
    @NamedQuery(name = "Movie.findByRating", query = "SELECT m FROM Movie m WHERE m.rating = :rating"),
    @NamedQuery(name = "Movie.findByReleaseDate", query = "SELECT m FROM Movie m WHERE m.releaseDate = :releaseDate"),
    @NamedQuery(name = "Movie.findByTitle", query = "SELECT m FROM Movie m WHERE m.title = :title")})
public class Movie implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "ID")
    private Integer id;
    @Column(name = "OVERVIEW", length = 500)
    private String overview;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "RATING")
    private Double rating;
    @Column(name = "RELEASE_DATE")
    @Temporal(TemporalType.DATE)
    private Date releaseDate;
    @Column(name = "TITLE")
    private String title;
    @JoinColumn(name = "FAVORITE_LIST_ID", referencedColumnName = "ID")
    @ManyToOne
    private FavoriteList favoriteListId;
    @JoinColumn(name = "GENRE_ID", referencedColumnName = "ID")
    @ManyToOne
    private Genre genreId;

    public Movie() {
    }

    public Movie(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOverview() {
        return overview;
    }

    public void setOverview(String overview) {
        this.overview = overview;
    }

    public Double getRating() {
        return rating;
    }

    public void setRating(Double rating) {
        this.rating = rating;
    }

    public Date getReleaseDate() {
        return releaseDate;
    }

    public void setReleaseDate(Date releaseDate) {
        this.releaseDate = releaseDate;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public FavoriteList getFavoriteListId() {
        return favoriteListId;
    }

    public void setFavoriteListId(FavoriteList favoriteListId) {
        this.favoriteListId = favoriteListId;
    }

    public Genre getGenreId() {
        return genreId;
    }

    public void setGenreId(Genre genreId) {
        this.genreId = genreId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Movie)) {
            return false;
        }
        Movie other = (Movie) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "model.Movie[ id=" + id + " ]";
    }
    
}
