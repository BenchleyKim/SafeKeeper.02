package com.example.safekeeper;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CctvMarkerAdapter extends RecyclerView.Adapter<CctvMarkerAdapter.CustomViewHolder> {



    private ArrayList<CctvMarker> arrayList;

    private Context context;

    public CctvMarkerAdapter(ArrayList arrayList, Context context) {
        this.context = context;
        this.arrayList = arrayList;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cctvmarkerlist_item, parent,false);
        CustomViewHolder holder = new CustomViewHolder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        holder.iv_profile.setImageResource(R.drawable.ic_videocam_black_24dp);
        holder.tv_id.setText("위험지역이름 : "+arrayList.get(position).getCctv_title());
        holder.tv_pw.setText("위험지역설명 : "+arrayList.get(position).getCctv_settingdate());
        holder.tv_userName.setText("위험지역위치 : "+arrayList.get(position).getCctv_call());

    }

    @Override
    public int getItemCount() {
        //삼항 연산자
        return (arrayList != null ? arrayList.size() : 0);
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView iv_profile;
        TextView tv_id;
        TextView tv_pw;
        TextView tv_userName;



        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            this.iv_profile = itemView.findViewById(R.id.iv_profile);
            this.tv_id = itemView.findViewById(R.id.tv_safemarkername);
            this.tv_pw = itemView.findViewById(R.id.tv_makercallnum);
            this.tv_userName = itemView.findViewById(R.id.tv_settingdate);


        }
    }
}
