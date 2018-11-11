package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

@Entity(tableName = "immediate_contacts_table")
public class MyImmediateContactsEntity {

    private String name;

    private String contactNumber;

    public MyImmediateContactsEntity(String name, String contactNumber) {
        this.name = name;
        this.contactNumber = contactNumber;
    }
}
