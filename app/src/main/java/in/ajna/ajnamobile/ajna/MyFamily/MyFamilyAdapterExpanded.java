package in.ajna.ajnamobile.ajna.MyFamily;

import android.util.Log;
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

public class MyFamilyAdapterExpanded extends FirestoreRecyclerAdapter<User,MyFamilyAdapterExpanded.MyFamilyHolder> {

    public MyFamilyAdapterExpanded(@NonNull FirestoreRecyclerOptions<User> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull MyFamilyHolder myFamilyHolder, int i, @NonNull User user) {

    }


    @NonNull
    @Override
    public MyFamilyAdapterExpanded.MyFamilyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_family_list_item_collapsed,parent,false);

        return new MyFamilyAdapterExpanded.MyFamilyHolder(v);
    }
    class MyFamilyHolder extends RecyclerView.ViewHolder{

        TextView tvIconFam;

        public MyFamilyHolder(@NonNull View itemView) {
            super(itemView);
            tvIconFam=itemView.findViewById(R.id.tvIconFam);
            Log.d("Hello","hello");
        }
    }
}
