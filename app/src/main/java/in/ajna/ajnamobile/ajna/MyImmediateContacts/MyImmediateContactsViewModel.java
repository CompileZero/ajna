package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.app.Application;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;


public class MyImmediateContactsViewModel extends AndroidViewModel {
    private MyImmediateContactsRepository repository;
    private LiveData<List<MyImmediateContactsEntity>> allImmediateContacts;

    public MyImmediateContactsViewModel(@NonNull Application application) {
        super(application);
    }

}
