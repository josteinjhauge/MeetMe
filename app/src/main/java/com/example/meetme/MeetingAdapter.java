package com.example.meetme;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MeetingAdapter extends RecyclerView.Adapter<MeetingAdapter.MeetingViewHolder> {

    public interface OnMeetingListener {
        void onMeetingClick(Meeting meeting);
    }

    public void setOnMeetingListener(OnMeetingListener listener) {
        this.listener = listener;
    }

    class MeetingViewHolder extends RecyclerView.ViewHolder {
        private final TextView itemName;
        private final TextView itemDate;
        private final TextView itemStartTime;
        private final TextView itemEndTime;
        private OnMeetingListener listener;

        private MeetingViewHolder(View itemView, OnMeetingListener listener) {
            super(itemView);
            itemName = itemView.findViewById(R.id.meeting_name);
            itemDate = itemView.findViewById(R.id.meeting_date);
            itemStartTime = itemView.findViewById(R.id.meeting_startTime);
            itemEndTime = itemView.findViewById(R.id.meeting_endTime);
            this.listener = listener;

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = getAdapterPosition();
                    if (listener != null && position != RecyclerView.NO_POSITION) {
                        listener.onMeetingClick(meetingList.get(position));
                    }
                }
            });
        }
    }

    private final LayoutInflater mInflater;
    List<Meeting> meetingList;
    List<MeetingParti> meetingPartiList;
    private OnMeetingListener listener;

    MeetingAdapter(Context context, OnMeetingListener listener) {
        mInflater = LayoutInflater.from(context);
        this.listener = listener;
    }

    @Override
    public MeetingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = mInflater.inflate(R.layout.meeting_recycler_item, parent, false);
        return new MeetingViewHolder(itemView, listener);
    }

    @Override
    public void onBindViewHolder(MeetingViewHolder holder, int position) {
        if (meetingList != null) {
            Meeting current = meetingList.get(position);
            holder.itemName.setText(current.getMeetingName());
            holder.itemDate.setText(current.getDate());
            holder.itemStartTime.setText(current.getStartTime());
            holder.itemEndTime.setText(current.getEndTime());

        } else {
            holder.itemName.setText("No data");
            holder.itemDate.setText("No data");
            holder.itemStartTime.setText("No data");
            holder.itemEndTime.setText("No data");
        }
    }

    void setMeeting(List<Meeting> meetings) {
        meetingList = meetings;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        if (meetingList != null) {
            return meetingList.size();
        } else {
            return 0;
        }
    }

    public Meeting getMeetingPosition(int position) {
        return meetingList.get(position);
    }
}