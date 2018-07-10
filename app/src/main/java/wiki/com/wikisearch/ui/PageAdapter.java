package wiki.com.wikisearch.ui;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import wiki.com.wikisearch.R;
import wiki.com.wikisearch.room.PageEntity;

class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageAdapterViewHolder>implements Filterable {

    private final Context mContext;
    private int rowLayout;
    private List<PageEntity> mPages,mFilteredPageList,unFilteredList;
    boolean isFilterActive = false;

    public PageAdapter( int rowLayout, Context context) {
        this.rowLayout = rowLayout;
        this.mContext = context;
    }
    @NonNull
    @Override
    public PageAdapter.PageAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(rowLayout, parent, false);
        return new PageAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PageAdapter.PageAdapterViewHolder holder, int position) {

    if(isFilterActive){
        if(mFilteredPageList.get(position).getTerms()!=null && mFilteredPageList.get(position).getTitle()!=null ) {
            holder.description.setText(mFilteredPageList.get(position).getTitle());
            holder.title.setText(mFilteredPageList.get(position).getTitle());
        }
       /* if( mFilteredPageList.get(position).getThumbnail()!=null){
            Glide.with(mContext).load(mFilteredPageList.get(position).getThumbnail()).placeholder(R.drawable.profile_icon).into(holder.thumbnailImage);
        }*/
    }else{
        if(mPages.get(position).getTerms()!=null && mPages.get(position).getTitle()!=null ) {
            holder.description.setText(mPages.get(position).getTitle());
            holder.title.setText(mPages.get(position).getTitle());
        }
       /* if( mPages.get(position).getThumbnail()!=null){
            Glide.with(mContext).load(mPages.get(position).getThumbnail()).placeholder(R.drawable.profile_icon).into(holder.thumbnailImage);
        }*/
    }
    }

    @Override
    public int getItemCount() {
        if(isFilterActive) {
            return mFilteredPageList.size();
        } else {
            if( mPages!=null)
            return mPages.size();
        }
        return 0;
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredPageList = mPages;
                    isFilterActive = false;
                }else {
                    ArrayList<PageEntity> filteredList = new ArrayList<>();

                    for (PageEntity search : mPages) {

                        if ( search.getTitle().toLowerCase().contains(charString) ) {

                            filteredList.add(search);
                        }
                    }

                    mFilteredPageList = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredPageList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(charSequence.toString().isEmpty()){
                    mPages = unFilteredList;
                }
                else {
                    mFilteredPageList = (List<PageEntity>) filterResults.values;
                }
                isFilterActive = true;
                notifyDataSetChanged();

            }

        };
    }

    class PageAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout newsLayout;
        TextView state, title, description;
        ImageView thumbnailImage;



        public PageAdapterViewHolder(View itemView) {
            super(itemView);
            newsLayout = (LinearLayout) itemView.findViewById(R.id.news_layout);
            title = (TextView) itemView.findViewById(R.id.title);
            description = (TextView) itemView.findViewById(R.id.description);
            thumbnailImage = (ImageView) itemView.findViewById(R.id.news_image);

            itemView.setOnClickListener(this);

        }


        @Override
        public void onClick(View view) {

        }
    }

    void swapVideos(final List<PageEntity> pages) {
        // If there was no forecast data, then recreate all of the list

        mPages = pages;
        mFilteredPageList=pages;



        notifyDataSetChanged();
    }
}
