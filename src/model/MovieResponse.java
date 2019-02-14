/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.Date;

/**
 *
 * @author kosta
 */
public class MovieResponse {
    @SerializedName("id")
    @Expose
    public int id;
    
    @SerializedName("title")
    @Expose
    public String title;
    
    @SerializedName("genre_ids")
    @Expose
    public int[] genre_ids;
    
    @SerializedName("release_date")
    @Expose
    public Date release_date;
    
    @SerializedName("vote_average")
    @Expose
    public Double rating;
    
    @SerializedName("overview")
    @Expose
    public String overview;
}




