package com.app.discovergolf.Model;

import com.google.gson.annotations.SerializedName;

public class Coach  {

    @SerializedName("id")
    public String CoachID;

    @SerializedName("name")
    public String FullName;

    @SerializedName("email")
    public String Email;

    @SerializedName("profile_img")
    public String ProfilePhoto;

    @SerializedName("first_name")
    public String FirstName;

    @SerializedName("last_name")
    public String LastName;

    @SerializedName("city")
    public String City;



}
