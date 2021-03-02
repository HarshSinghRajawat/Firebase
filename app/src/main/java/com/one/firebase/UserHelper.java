package com.one.firebase;


public class UserHelper {
    private String user_name;
    private String city;
    private String age;
    private String gender;

    public UserHelper(){}

    public UserHelper(String user_name, String city, String age, String gender) {
        this.gender=gender;
        this.city = city;
        this.age = age;

        this.user_name = user_name;
    }

    public String getUser_name() {
        return user_name;
    }


    public String getCity() {
        return city;
    }


    public String getAge() {
        return age;
    }


    public String getGender() {
        return gender;
    }

}
