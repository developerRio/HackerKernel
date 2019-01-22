package com.originalstocks.demotestone;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ProfileAdapter extends RecyclerView.Adapter<ProfileAdapter.MyViewHolder> {

    Context context;
    private List<Profile> profileList ;

    public ProfileAdapter(Context context, List<Profile> profileList) {
        this.context = context;
        this.profileList = profileList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.profile_items, viewGroup, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int i) {

        Profile profile = profileList.get(i);

        holder.status.setText("Status is : " + profile.getStatus());
        holder.file.setText("File is : " + profile.getFile());
        holder.pitch.setText("Pitch is : " + profile.getPitch());
        holder.lips.setText("Lips is : " + profile.getLips());
        holder.maleConfidence.setText("Male Confidence level is : " + profile.getMaleConfidence());

    }

    @Override
    public int getItemCount() {
        return profileList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView status, file, pitch, lips, maleConfidence;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            status = itemView.findViewById(R.id.status_Text);
            file = itemView.findViewById(R.id.file_Text);
            pitch = itemView.findViewById(R.id.pitch_Text);
            lips = itemView.findViewById(R.id.lips_Text);
            maleConfidence = itemView.findViewById(R.id.maleConfidence_Text);
        }
    }
}
