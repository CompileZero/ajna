package in.ajna.ajnamobile.ajna.Login;

public class User {

    public String fullName,phoneNumber,token;

    public User(){

    }

    public User(String fullName,String phoneNumber,String token) {
        this.fullName = fullName;
        this.phoneNumber=phoneNumber;
        this.token=token;
    }

    public String getFullName(){
        return fullName;
    }

    public String getPhoneNumber(){
        return phoneNumber;
    }

}
