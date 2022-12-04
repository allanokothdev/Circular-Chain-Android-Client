package com.circularchained.android.adapters;

import android.content.Context;
import android.text.format.DateFormat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.circularchained.android.R;
import com.circularchained.android.models.Stage;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class StageAdapter extends RecyclerView.Adapter<StageAdapter.ViewHolder>{

    private final String TAG = this.getClass().getSimpleName();
    private Context mContext;
    private List<Stage> stringList;

    public StageAdapter(Context mContext, List<Stage> stringList){
        this.mContext = mContext;
        this.stringList = stringList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.chain_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        ((ViewHolder) holder).bind(position);
    }

    @Override
    public int getItemCount() {
        return stringList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private final ImageView imageView;
        private final TextView textView;
        private final TextView subTextView;
        private final TextView subItemTextView;
        private final TextView dateTextView;

        private final RatingBar natureRatingBar;
        private final RatingBar climateRatingBar;
        private final RatingBar labourRatingBar;
        private final RatingBar communityRatingBar;
        private final RatingBar wasteRatingBar;

        private ViewHolder(@NonNull View itemView) {
            super(itemView);

            imageView = itemView.findViewById(R.id.imageView);
            textView = itemView.findViewById(R.id.textView);
            subTextView = itemView.findViewById(R.id.subTextView);
            subItemTextView = itemView.findViewById(R.id.subItemTextView);
            dateTextView = itemView.findViewById(R.id.dateTextView);
            natureRatingBar = itemView.findViewById(R.id.natureRatingBar);
            climateRatingBar = itemView.findViewById(R.id.climateRatingBar);
            labourRatingBar = itemView.findViewById(R.id.labourRatingBar);
            communityRatingBar = itemView.findViewById(R.id.communityRatingBar);
            wasteRatingBar = itemView.findViewById(R.id.wasteRatingBar);
        }

        void bind(int position) {

            Stage stage = stringList.get(position);

            textView.setText(stage.getStep());
            subTextView.setText(stage.getTitle());
            subItemTextView.setText(stage.getLocation());
            natureRatingBar.setRating(stage.getNature());
            climateRatingBar.setRating(stage.getClimate());
            labourRatingBar.setRating(stage.getLabour());
            communityRatingBar.setRating(stage.getCommunity());
            wasteRatingBar.setRating(stage.getWaste());
            dateTextView.setText(getTime(stage.getDate()));

        }
    }

    private String getTime(long time){
        Calendar cal = Calendar.getInstance(Locale.getDefault());
        cal.setTimeInMillis(time);
        return DateFormat.format("HH:mm:aa ~ dd MMMM yy", cal).toString();
    }
}
