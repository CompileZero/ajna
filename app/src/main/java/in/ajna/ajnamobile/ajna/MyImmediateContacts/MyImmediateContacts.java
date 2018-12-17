package in.ajna.ajnamobile.ajna.MyImmediateContacts;

public class MyImmediateContacts {

    private String nameOfImmediateContact;
    private String contactNumberOfImmediateContact;


    public MyImmediateContacts() {
        //Required empty constructor
    }

    public MyImmediateContacts(String nameOfImmediateContact, String contactNumberOfImmediateContact) {
        this.nameOfImmediateContact = nameOfImmediateContact;
        this.contactNumberOfImmediateContact = contactNumberOfImmediateContact;

    }

    public String getNameOfImmediateContact() {
        return nameOfImmediateContact;
    }

    public void setNameOfImmediateContact(String nameOfImmediateContact) {
        this.nameOfImmediateContact = nameOfImmediateContact;
    }

    public String getContactNumberOfImmediateContact() {
        return contactNumberOfImmediateContact;
    }

    public void setContactNumberOfImmediateContact(String contactNumberOfImmediateContact) {
        this.contactNumberOfImmediateContact = contactNumberOfImmediateContact;
    }

}
