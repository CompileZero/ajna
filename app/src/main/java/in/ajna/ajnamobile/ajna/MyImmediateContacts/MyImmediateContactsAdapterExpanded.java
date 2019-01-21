package in.ajna.ajnamobile.ajna.MyImmediateContacts;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.shashank.sony.fancygifdialoglib.FancyGifDialog;
import com.shashank.sony.fancygifdialoglib.FancyGifDialogListener;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class MyImmediateContactsAdapterExpanded extends FirestoreRecyclerAdapter<MyImmediateContacts,MyImmediateContactsAdapterExpanded.MyImmediateContactsHolder> {

    Activity mActivity;


    public MyImmediateContactsAdapterExpanded(@NonNull FirestoreRecyclerOptions<MyImmediateContacts> options, Activity activity) {
        super(options);
        mActivity=activity;
    }

    @Override
    protected void onBindViewHolder(@NonNull MyImmediateContactsHolder myImmediateContactsHolder, int i, @NonNull MyImmediateContacts myImmediateContacts) {
        myImmediateContactsHolder.tvNameOfImmediateContact.setText(myImmediateContacts.getNameOfImmediateContact());
        myImmediateContactsHolder.tvContactNumberOfImmediateContact.setText(myImmediateContacts.getContactNumberOfImmediateContact());
        myImmediateContactsHolder.tvIconExpanded.setText(String.valueOf(myImmediateContacts.getNameOfImmediateContact().substring(0,3)));
    }

    @NonNull
    @Override
    public MyImmediateContactsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_immediate_contacts_list_item_expanded,parent,false);

        return new MyImmediateContactsHolder(v);
    }

    class MyImmediateContactsHolder extends RecyclerView.ViewHolder{

        TextView tvNameOfImmediateContact;
        TextView tvContactNumberOfImmediateContact;
        TextView tvIconExpanded;
        FloatingActionButton fabDeleteImmediateContact;



        public MyImmediateContactsHolder(@NonNull View itemView) {
            super(itemView);
            tvNameOfImmediateContact=itemView.findViewById(R.id.tvNameOfImmediateContact);
            tvContactNumberOfImmediateContact=itemView.findViewById(R.id.tvContactNumberOfImmediateContact);
            tvIconExpanded=itemView.findViewById(R.id.tvIconExpanded);

            fabDeleteImmediateContact=itemView.findViewById(R.id.fabDeleteImmediateContact);
            fabDeleteImmediateContact.setColorFilter(Color.WHITE);

            fabDeleteImmediateContact.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new FancyGifDialog.Builder(mActivity)
                            .setTitle("Confirm Delete?")
                            .setMessage("Do you want to delete the ")
                            .setGifResource(R.drawable.dialog1)
                            .setPositiveBtnText("Delete")
                            .setNegativeBtnText("Cancel")
                            .isCancellable(false)
                            .OnPositiveClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    deleteItem(getAdapterPosition());
                                }
                            })
                            .OnNegativeClicked(new FancyGifDialogListener() {
                                @Override
                                public void OnClick() {
                                    
                                }
                            })
                            .build();

                }
            });

        }
    }
    public void deleteItem(int position){
        getSnapshots().getSnapshot(position).getReference().delete();
    }
}
