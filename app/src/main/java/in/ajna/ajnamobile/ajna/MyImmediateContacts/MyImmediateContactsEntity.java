package in.ajna.ajnamobile.ajna.MyImmediateContacts;


import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "my_immediate_contacts_table")
public class MyImmediateContactsEntity {

    @PrimaryKey(autoGenerate = true)
    private int id;

    private String nameOfImmediateContact;

    private String contactNumberOfImmediateContact;

    public MyImmediateContactsEntity(String nameOfImmediateContact, String contactNumberOfImmediateContact) {
        this.nameOfImmediateContact=nameOfImmediateContact;
        this.contactNumberOfImmediateContact=contactNumberOfImmediateContact;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public String getNameOfImmediateContact() {
        return nameOfImmediateContact;
    }

    public String getContactNumberOfImmediateContact() {
        return contactNumberOfImmediateContact;
    }
}
