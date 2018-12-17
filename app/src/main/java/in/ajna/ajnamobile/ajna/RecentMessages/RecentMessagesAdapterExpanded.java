package in.ajna.ajnamobile.ajna.RecentMessages;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.vipulasri.timelineview.TimelineView;

import java.sql.Time;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Locale;


import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import in.ajna.ajnamobile.ajna.R;

public class RecentMessagesAdapterExpanded extends FirestoreRecyclerAdapter<RecentMessages,RecentMessagesAdapterExpanded.RecentMessagesHolder> {


    public RecentMessagesAdapterExpanded(@NonNull FirestoreRecyclerOptions<RecentMessages> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecentMessagesHolder recentMessagesHolder, int i, @NonNull RecentMessages recentMessages) {
        recentMessagesHolder.tvMessage.setText(recentMessages.getMessage());
        recentMessagesHolder.tvTime.setText(getDate(recentMessages.getTime()));
    }

    @NonNull
    @Override
    public RecentMessagesHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v=LayoutInflater.from(parent.getContext()).inflate(R.layout.recent_messages_list_item_expanded
        ,parent,false);
        return new RecentMessagesHolder(v,viewType);
    }

    class RecentMessagesHolder extends RecyclerView.ViewHolder{
        TimelineView timelineView;
        TextView tvTime;
        TextView tvMessage;

        public RecentMessagesHolder(@NonNull View itemView,int viewType) {
            super(itemView);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvMessage=itemView.findViewById(R.id.tvMessage);

            timelineView=itemView.findViewById(R.id.timeMarker);
            timelineView.initLine(viewType);

        }
    }

    @Override
    public int getItemViewType(int position) {
        return TimelineView.getTimeLineViewType(position,getItemCount());
    }

    private String getDate(Long timestamp){
        Calendar cal=Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp*1000);
        String date= android.text.format.DateFormat.format("dd-MM-yyyy hh:mm",cal).toString();

        return date;
    }
}
