package in.ajna.ajnamobile.ajna.MyFamily;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.Login.User;
import in.ajna.ajnamobile.ajna.R;

public class FamilyAdapter extends FirestoreRecyclerAdapter<User, FamilyAdapter.FamilyHolder> {

    String phoneNumber="";

    public FamilyAdapter(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull FamilyHolder familyHolder, int i, @NonNull User user) {
        familyHolder.tvFamilyMemberInitials.setText(String.valueOf(user.getFullName().substring(0,2)));
        familyHolder.tvFamilyMemberName.setText(user.getFullName());
        phoneNumber=user.getPhoneNumber().substring(0,3)+" "+user.getPhoneNumber().substring(3,8)+" "+user.getPhoneNumber().substring(8);
        familyHolder.tvFamilyMemberPhone.setText(phoneNumber);
    }


    @NonNull
    @Override
    public FamilyAdapter.FamilyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.family_list_item,parent,false);

        return new FamilyAdapter.FamilyHolder(v);
    }
    class FamilyHolder extends RecyclerView.ViewHolder{

        TextView tvFamilyMemberInitials,tvFamilyMemberName,tvFamilyMemberPhone;

        public FamilyHolder(@NonNull View itemView) {
            super(itemView);
            tvFamilyMemberInitials=itemView.findViewById(R.id.tvFamilyMemberInitials);
            tvFamilyMemberName=itemView.findViewById(R.id.tvFamilyMemberName);
            tvFamilyMemberPhone=itemView.findViewById(R.id.tvFamilyMemberPhone);
        }
    }
}
