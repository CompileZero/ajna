package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

import android.os.Bundle;
import android.widget.Toast;

import java.util.List;

public class MyImmediateContactsActivity2 extends AppCompatActivity {
    private MyImmediateContactsViewModel myImmediateContactsViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_immediate_contacts2);


        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.rvMyImmediateContacts);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        final MyImmediateContactsAdapter adapter = new MyImmediateContactsAdapter();

        recyclerView.setAdapter(adapter);


        myImmediateContactsViewModel = ViewModelProviders.of(this).get(MyImmediateContactsViewModel.class);
        myImmediateContactsViewModel.getAllImmediateContacts().observe(this, new Observer<List<MyImmediateContactsEntity>>() {
            @Override
            public void onChanged(List<MyImmediateContactsEntity> myImmediateContactsEntities) {

                adapter.setImmediateContacts(myImmediateContactsEntities);

                }
        });
    }
}
