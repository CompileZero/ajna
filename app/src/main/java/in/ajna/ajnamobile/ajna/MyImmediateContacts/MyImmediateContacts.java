package in.ajna.ajnamobile.ajna.MyImmediateContacts;

public class MyImmediateContacts {

    private String nameOfImmediateContact;
    private String contactNumberOfImmediateContact;
    private int idOfImmediateContact;

    public MyImmediateContacts() {
        //Required empty constructor
    }

    public MyImmediateContacts(String nameOfImmediateContact, String contactNumberOfImmediateContact, int idOfImmediateContact) {
        this.nameOfImmediateContact = nameOfImmediateContact;
        this.contactNumberOfImmediateContact = contactNumberOfImmediateContact;
        this.idOfImmediateContact=idOfImmediateContact;
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
    public int getIdOfImmediateContact() {
        return idOfImmediateContact;
    }

    public void setIdOfImmediateContact(int idOfImmediateContact) {
        this.idOfImmediateContact = idOfImmediateContact;
    }
}
