/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 *
 * @author kosta
 */
public class MoviesResponse {
    @SerializedName("results")
    @Expose
    public model.MovieResponse[] results;
    
    @SerializedName("page")
    @Expose
    public int page;
    
    @SerializedName("total_pages")
    @Expose
    public int total_pages;
}




