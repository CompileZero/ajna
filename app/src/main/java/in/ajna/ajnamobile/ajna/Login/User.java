package in.ajna.ajnamobile.ajna.Login;

import com.google.firebase.firestore.Exclude;

public class User {

    public String fullName,address,city,phoneNumber;

    public User(){

    }


    public User(String fullName, String address, String city, String phoneNumber) {
        this.fullName = fullName;
        this.address = address;
        this.city = city;
        this.phoneNumber=phoneNumber;
    }

    public String getFullName(){
        return fullName;
    }

}
