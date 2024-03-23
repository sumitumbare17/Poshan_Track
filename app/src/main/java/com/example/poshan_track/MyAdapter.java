package com.example.poshan_track;

import static android.content.ContentValues.TAG;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {

    Context context;
    ArrayList<Dataholder> list;

    private SelectListener selectListener;

    String Dname;

    int pos;
    public MyAdapter(Context context, ArrayList<Dataholder> list, SelectListener selectListener) {
        this.context = context;
        this.list = list;
        this.selectListener = selectListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(context).inflate(R.layout.stu_data,parent,false);
        return new MyViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        Dataholder dh = list.get(position);

        holder.bindData(dh);

        holder.name.setText(dh.getName());
        holder.age.setText(dh.getAge());
        holder.clas.setText(dh.getCls());


    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public void searchDataList(ArrayList<Dataholder> searchlist){
        list = searchlist;
        notifyDataSetChanged();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView name,age,clas;

        ImageView more;

        private Dataholder currentDataholder;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.tv_name);
            age = itemView.findViewById(R.id.tv_age);
            clas =itemView.findViewById(R.id.tv_cls);
            more = itemView.findViewById(R.id.img_more);

            more.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    showDialog(currentDataholder);
                }
            });

            try {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectListener.onItemClick(getAdapterPosition());

                        call_profile(currentDataholder);
                    }

                });

            }catch (Exception e){
                Log.d(TAG,"Exception : "+e);
            }

        }
        public void bindData(Dataholder dataholder) {
            currentDataholder = dataholder;
            name.setText(dataholder.getName());
            age.setText(dataholder.getAge());
            clas.setText(dataholder.getCls());
        }

        public void call_profile(Dataholder dh){

            Intent intent = new Intent(itemView.getContext(),stu_profile.class);
            intent.putExtra("Bname",dh.getName());
            intent.putExtra("Bage",dh.getAge());
            intent.putExtra("Bclass",dh.getCls());
            intent.putExtra("Bphone",dh.getPhone());
            Log.d(TAG,"uname:"+dh.getName());

            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            itemView.getContext().startActivity(intent);

        }




        private void showDialog(Dataholder dataholder){

            String name = dataholder.getName();
            String age = dataholder.getAge();
            String cls = dataholder.getCls();
            String phone = dataholder.getPhone();
            String addr = dataholder.getAddr();

            final Dialog dialog = new Dialog(more.getContext());
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.bootomsheetlayout);

            LinearLayout layout_edit = dialog.findViewById(R.id.layout_edit);
            LinearLayout layout_monitor = dialog.findViewById(R.id.layout_monitor);

            layout_edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //call monitor page

                    Intent intent = new Intent(layout_edit.getContext(),AddStudentActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("ename",name);
                    intent.putExtra("eage",age);
                    intent.putExtra("ecls",cls);
                    intent.putExtra("ephone",phone);
                    intent.putExtra("eaddr",addr);
                    layout_edit.getContext().startActivity(intent);
                    Log.d(TAG, "onClick: editor code");
                }

            });
            layout_monitor.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    //call monitor page

                    Intent intent = new Intent(layout_monitor.getContext(),GrowthMonitorActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    intent.putExtra("studentName",name);
                    intent.putExtra("className",cls);
                    layout_monitor.getContext().startActivity(intent);
                    Log.d(TAG, "onClick: monitor code");
                }

            });

            dialog.show();
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
            dialog.getWindow().setGravity(Gravity.BOTTOM);

        }
    }
}
