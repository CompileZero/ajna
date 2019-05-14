package in.ajna.ajnamobile.ajna.Activity;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.github.vipulasri.timelineview.TimelineView;

import java.util.Calendar;
import java.util.Locale;

import in.ajna.ajnamobile.ajna.R;

public class RecentMessagesAdapterExpanded extends FirestoreRecyclerAdapter<RecentMessages,RecentMessagesAdapterExpanded.RecentMessagesHolder>{


    public RecentMessagesAdapterExpanded(@NonNull FirestoreRecyclerOptions<RecentMessages> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull RecentMessagesHolder recentMessagesHolder, int i, @NonNull RecentMessages recentMessages) {
        recentMessagesHolder.tvMessage.setText(recentMessages.getMessage());
        recentMessagesHolder.tvDate.setText(getDate(recentMessages.getTime()));
        recentMessagesHolder.tvMember.setText(recentMessages.getMember());
        recentMessagesHolder.tvTime.setText(getTime(recentMessages.getTime()));

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
        TextView tvTime,tvDate;
        TextView tvMessage,tvMember;

        Context context;
        public RecentMessagesHolder(@NonNull View itemView,int viewType) {
            super(itemView);

            context = itemView.getContext();
            tvDate=itemView.findViewById(R.id.tvDate);
            tvTime=itemView.findViewById(R.id.tvTime);
            tvMessage=itemView.findViewById(R.id.tvMessage);
            tvMember=itemView.findViewById(R.id.tvMember);

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
        cal.setTimeInMillis(timestamp);
        String date= android.text.format.DateFormat.format("dd-MMM",cal).toString();

        return date;
    }
    private String getTime(Long timestamp){
        Calendar cal=Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(timestamp);
        String date= android.text.format.DateFormat.format("hh:mm a",cal).toString();

        return date;
    }
}
