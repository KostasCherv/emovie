/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import java.util.List;

/**
 *
 * @author kosta
 */
public class GenresResponse {
    @SerializedName("genres")
    @Expose
    public model.Genre[] genres = null;
}




