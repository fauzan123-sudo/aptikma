package com.example.aptikma;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {
    private Context mContext;
    private ArrayList<ExampleItem> mExampleList;

    public ExampleAdapter(Context context, ArrayList<ExampleItem> exampleList) {
        mContext = context;
        mExampleList = exampleList;
    }
    @Override
    public ExampleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.example_item, parent, false);
        return new ExampleViewHolder(v);
    }
    @Override
    public void onBindViewHolder(ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);
        String creatorName = currentItem.getCreator();
        String likeCount   = currentItem.getLikeCount();
        String potongan    = currentItem.getPotongan();
        holder.mTextViewCreator.setText("gaji pokok: " +creatorName);
        holder.mTextViewLikes.setText("asuransi: " + likeCount);
        holder.mPotongan.setText("potongan: " + potongan);
    }





    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
    public class ExampleViewHolder extends RecyclerView.ViewHolder {
        public TextView mTextViewCreator;
        public TextView mTextViewLikes;
        public TextView mPotongan;

        public void addItem(ExampleItem item){
//        ArrayList<String> exampleItems = new ArrayList<>();
            mExampleList.add(0,item);
            notifyDataSetChanged();
        }

        public ExampleViewHolder(View itemView) {
            super(itemView);
            mTextViewCreator = itemView.findViewById(R.id.text_view_creator);
            mTextViewLikes = itemView.findViewById(R.id.text_view_likes);
            mPotongan = itemView.findViewById(R.id.text_view_potongan);
        }
    }
}