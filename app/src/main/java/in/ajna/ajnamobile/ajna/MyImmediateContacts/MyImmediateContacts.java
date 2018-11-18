package in.ajna.ajnamobile.ajna.MyImmediateContacts;

public class MyImmediateContacts {

    public static final String FIELD_NAME = "name";
    public static final String FIELD_CONTACT = "contact";

    private String nameOfImmediateContact;
    private String contactNumberOfImmediateContact;

    public MyImmediateContacts() {
    }

    public MyImmediateContacts(String name, String contact) {
        this.nameOfImmediateContact = name;
        this.contactNumberOfImmediateContact = contact;
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
