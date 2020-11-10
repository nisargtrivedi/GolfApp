package com.app.discovergolf.Model;

import com.google.gson.annotations.SerializedName;

public class Student {

    @SerializedName("id")
    public String StudentID;

    @SerializedName("name")
    public String FullName;

    @SerializedName("first_name")
    public String FirstName;

    @SerializedName("last_name")
    public String LastName;


    @SerializedName("profile_img")
    public String ProfileImage;


    @SerializedName("is_fav")
    public String IsFavourite;


    @SerializedName("academy_id")
    public String AcademyID;

    @SerializedName("city")
    public String StudentCity;

    @SerializedName("team_id")
    public int team_id;

    @SerializedName("music_id")
    public int music_id;

    @SerializedName("music_file")
    public String music_file;

}

//      {
//              "id": 20,
//              "name": "Std Fname Lname",
//              "email": "fname@test.com",
//              "password": "$2y$10$192CmEOq/3F9pxmDtNDw8.Y71VgA1i6hvNPIyQvMlDBV.FrBGHUT2",
//              "remember_token": null,
//              "created_at": "2018-12-09 07:42:05",
//              "updated_at": "2018-12-09 07:42:05",
//              "is_admin": "0",
//              "api_token": null,
//              "roles": "student",
//              "pin": null,
//              "account_number": null,
//              "comapny_name": null,
//              "phone_no": "87566688558",
//              "status": "1",
//              "first_name": "Std Fname",
//              "last_name": "Lname",
//              "profile_img": "http://solutioncode.in/admin-asset/images/profile/1544341325-iStock_000018030606Medium.jpg",
//              "city": "Ahmedabad",
//              "bod": "",
//              "academy_id": "3",
//              "about_me": "",
//              "coch_id": "0",
//              "class_id": "14",
//              "course_id": "0",
//              "student_id": null,
//              "mobile_password": "ad6a280417a0f533d8b670c61667e1a0",
//              "is_fav": 1
//              }
