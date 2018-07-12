package wiki.com.wikisearch.ui;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import wiki.com.wikisearch.R;
import wiki.com.wikisearch.room.PageEntity;
import wiki.com.wikisearch.utils.Utils;

class PageAdapter extends RecyclerView.Adapter<PageAdapter.PageAdapterViewHolder>implements Filterable {

    private final Context mContext;
    private int rowLayout;
    private List<PageEntity> mPages,mFilteredPageList,unFilteredList;
    boolean isFilterActive = false;
    URL url,domain;

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
            holder.description.setText(mFilteredPageList.get(position).getTerms().description.get(0));
            holder.title.setText(mFilteredPageList.get(position).getTitle());
        }
        if( mFilteredPageList.get(position).getThumbnail()!=null){
            Glide.with(mContext).load(mFilteredPageList.get(position).getThumbnail().getSource()).placeholder(R.drawable.profile_icon).into(holder.thumbnailImage);
        }
    }else{
        if(mPages.get(position).getTerms()!=null && mPages.get(position).getTitle()!=null ) {
            holder.description.setText(mPages.get(position).getTerms().description.get(0));
            holder.title.setText(mPages.get(position).getTitle());
        }
        if( mPages.get(position).getThumbnail()!=null){
           Glide.with(mContext).load(mPages.get(position).getThumbnail().getSource()).placeholder(R.drawable.profile_icon).into(holder.thumbnailImage);
        }
    }
        applyClickEvents(holder, position);
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
                    isFilterActive = true;
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

   public void swapVideos(final List<PageEntity> pages) {
        // If there was no forecast data, then recreate all of the list

        mPages = pages;
        mFilteredPageList=pages;
        unFilteredList=pages;
        notifyDataSetChanged();
    }

    private void applyClickEvents(final PageAdapterViewHolder holder, final int position) {


        holder.title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, news.get(position).getUrl() + " is selected!", Toast.LENGTH_SHORT).show();

                openWiki(position,view);


            }
        });


        holder.description.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(context, news.get(position).getUrl() + " is selected!", Toast.LENGTH_SHORT).show();
                openWiki(position,view);


            }
        });


    }

    private void openWiki(int position, View view) {
        if(Utils.isNetworkAvailable(mContext)){
            if(mPages.get(position).getTitle()!=null) {
                try {
                    domain = new URL("http://en.wikipedia.org/wiki/");
                    url = new URL(domain,mPages.get(position).getTitle());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(mContext, WbViewActivity.class);
                intent.putExtra("url_to_web",  url.toString());
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                mContext.startActivity(intent);
            }
        }
        else {
            Snackbar snackbar = Snackbar
                    .make(view, "Please check your internet connection", Snackbar.LENGTH_LONG);
            snackbar.getView().setBackgroundColor(ContextCompat.getColor(mContext,R.color.new_pink));
            snackbar.show();
        }

    }
}
