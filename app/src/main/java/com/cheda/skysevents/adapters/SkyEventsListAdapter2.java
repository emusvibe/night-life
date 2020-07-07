package com.cheda.skysevents.adapters;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.cheda.skysevents.R;
import com.cheda.skysevents.fragments.PopularFragment;
import com.cheda.skysevents.fragments.UpComingFragment;
import com.cheda.skysevents.model.SkyEvent;
import com.cheda.skysevents.service.SharedPreference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class SkyEventsListAdapter2 extends RecyclerView.Adapter<SkyEventsListAdapter2.ViewHolder> implements Filterable {

    private final PopularFragment.SkyListCallback listCallb;
    private boolean mTwoPane;
    private LayoutInflater mLayoutInflater;
    private Context context;
    private ArrayList<SkyEvent> mArrayList;
    private ArrayList<SkyEvent> mFilteredList;
    SharedPreference sharedPreference;
    private FavListener mFavlistener;
    public interface FavListener{
        void onClickFavItem(SkyEvent event);
    }

    public SkyEventsListAdapter2(Context context, ArrayList<SkyEvent> arrayList, PopularFragment.SkyListCallback listCallb,FavListener mFavlistener ) {
        this.context = context;
        this.mArrayList = arrayList;
        this.mFilteredList = arrayList;
        sharedPreference = new SharedPreference();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.listCallb = listCallb;
        this.mFavlistener = mFavlistener;
    }

    @Override
    public SkyEventsListAdapter2.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.event_list_row, viewGroup, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final SkyEventsListAdapter2.ViewHolder viewHolder, int i) {
        final SkyEvent events = mFilteredList.get(i);
        viewHolder.events = mFilteredList.get(i);
        viewHolder.tv_name.setText(mFilteredList.get(i).getName());
        viewHolder.tv_venue.setText(mFilteredList.get(i).getVenue());
        viewHolder.tv_date.setText(mFilteredList.get(i).getDate());
        viewHolder.tv_admission.setText(mFilteredList.get(i).getAdmission());

        //load album cover using picasso
        Picasso.with(context)
                .load(mFilteredList.get(i).getPoster())
                .placeholder(R.drawable.sky_logo)
                .into(viewHolder.poster);

        viewHolder.itemView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                mFavlistener.onClickFavItem(events);
                listCallb.onSkyEvent2Selected(events);

            }

        });

        viewHolder.poster.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mFavlistener.onClickFavItem(events);
                listCallb.onSkyEvent2Selected(events);
            }
        });

        // set favorites code /*If a event exists in shared preferences then set heart_red drawable * and set a tag*/
        if (checkFavoriteItem(events)) {
            viewHolder.favoriteImg.setImageResource(R.drawable.heart_red);
            viewHolder.favoriteImg.setTag("red");
            sharedPreference.saveFavorites(context, mFilteredList);
            notifyDataSetChanged();
            viewHolder.mListItem.setBackgroundColor(
                    context.getResources().getColor(R.color.colorGrey));
        } else {
            viewHolder.favoriteImg.setImageResource(R.drawable.heart_grey);
            viewHolder.favoriteImg.setTag("grey");
            viewHolder.mListItem.setBackgroundColor(
                    context.getResources().getColor(R.color.colorWhite));
        }

    }

    /*Checks whether a particular event exists in SharedPreferences*/
    public boolean checkFavoriteItem(SkyEvent checkEvent) {
        boolean check = false;
//        ArrayList<SkyEvent> favorites = sharedPreference.getFavorites(context);
        List<SkyEvent> favorites = sharedPreference.getFavorites(context);
        if (favorites != null) {
            for (SkyEvent events : favorites) {
                if (events.equals(checkEvent)) {
                    check = true;

//                    notifyDataSetChanged();
                    break;
                }
            }
        }
        return check;
    }


    @Override
    public int getItemCount() {
//        if (null == mFilteredList) return 0;
        return mFilteredList.size();
    }

    @Override
    public Filter getFilter() {

        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {

                String charString = charSequence.toString();

                if (charString.isEmpty()) {

                    mFilteredList = mArrayList;
                } else {

                    ArrayList<SkyEvent> filteredList = new ArrayList<>();

                    for (SkyEvent events : mArrayList) {

                        if (events.getVenue().toLowerCase().contains(charString) || events.getName().toLowerCase().contains(charString) || events.getDate().toLowerCase().contains(charString)) {

                            filteredList.add(events);
                        }
                    }

                    mFilteredList = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = mFilteredList;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                mFilteredList = (ArrayList<SkyEvent>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public void setFilter(ArrayList<SkyEvent> mFilteredList) {

        mArrayList = new ArrayList<>();
        mArrayList.addAll(mFilteredList);
        notifyDataSetChanged();

    }



    public class ViewHolder extends RecyclerView.ViewHolder{
        private TextView tv_name,tv_venue,tv_date,tv_admission;
        private ImageView poster, favoriteImg, shareImageView;
        private CardView mCardView;
        private RelativeLayout mListItem;
        public SkyEvent events;


        public ViewHolder(final View view) {
            super(view);

            tv_name = (TextView)view.findViewById(R.id.tv_name);
            tv_venue = (TextView)view.findViewById(R.id.tv_venue);
            tv_date = (TextView)view.findViewById(R.id.tv_date);
            tv_admission = (TextView)view.findViewById(R.id.tv_admission);
            poster = (ImageView) view.findViewById(R.id.poster);
            mCardView = (CardView) view.findViewById(R.id.card_view);
            mListItem = (RelativeLayout) view.findViewById(R.id.list_item_layout);
            favoriteImg = (ImageView)view.findViewById(R.id.fav);
            shareImageView = (ImageView)view.findViewById(R.id.send);

//            view.setOnClickListener(this);
            // added - symantic change in clicking starting at API 23
//            mListItem.setOnClickListener(this);

            /*
             * NOTE: have to set the onClickListener to the CardView - to
             *    accurately change selection color for group
             */
//            mCardView.setOnClickListener(this);

            favoriteImg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    int posit = getAdapterPosition();
                    String pos = favoriteImg.getTag().toString();
                    if (pos.equalsIgnoreCase("grey") && posit != RecyclerView.NO_POSITION){

                        sharedPreference.addFavorite(context,mFilteredList.get(getAdapterPosition()));
                        Toast.makeText(context,context.getString(R.string.add_favr), Toast.LENGTH_SHORT).show();
                        favoriteImg.setTag("red");
                        favoriteImg.setImageResource(R.drawable.heart_red);
                      //  sharedPreference.saveFavorites(context,mFilteredList);
//                        notifyItemInserted(posit);
                    }
                    else {
                        sharedPreference.removeFavorite(context,mFilteredList.get(getAdapterPosition()));
                        favoriteImg.setTag("grey");
                        favoriteImg.setImageResource(R.drawable.heart_grey);
                        Toast.makeText(context,context.getString(R.string.remove_favr), Toast.LENGTH_SHORT).show();
                        sharedPreference.saveFavorites(context,mFilteredList);
//   notifyItemRemoved(posit);
                    }

                }
            });

        }


    }



}