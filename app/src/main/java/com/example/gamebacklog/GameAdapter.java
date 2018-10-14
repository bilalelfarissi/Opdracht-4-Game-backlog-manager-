package com.example.gamebacklog;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class GameAdapter extends RecyclerView.Adapter<GameAdapter.GameViewHolder> {

    private List<Game> mGames;
    final private GameClickListener mGameClickListener;

    public GameAdapter(List<Game> mGames, GameClickListener mGameClickListener) {
        this.mGames = mGames;
        this.mGameClickListener = mGameClickListener;
    }

    @NonNull
    @Override
    public GameAdapter.GameViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater= LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_game, null);

        // Return a new holder instance
        return new GameAdapter.GameViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GameAdapter.GameViewHolder GameViewHolder, int position) {
        Game game = mGames.get(position);
        GameViewHolder.vTitel.setText(game.getTitel());
        GameViewHolder.vPlatform.setText(game.getPlatform());
        GameViewHolder.vStatus.setText(game.getStatus());
        GameViewHolder.vDatum.setText(game.getDatum());
    }

    @Override
    public int getItemCount() {
        return mGames.size();
    }

    public class GameViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView vTitel;
        private TextView vPlatform;
        private TextView vStatus;
        private TextView vDatum;

        private GameViewHolder(View v) {
            super(v);
            vTitel = v.findViewById(R.id.cardViewTitelTextView);
            vPlatform = v.findViewById(R.id.cardViewPlatformTextView);
            vStatus = v.findViewById(R.id.cardViewStatusTextView);
            vDatum = v.findViewById(R.id.cardViewDateTextView);
            v.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int clickedPosition = getAdapterPosition();
            mGameClickListener.GameOnClick(clickedPosition);

        }
    }

    public interface GameClickListener{
        void GameOnClick (int i);
    }

    public void swapList (List<Game> newList) {
        mGames = newList;
        if (newList != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }
}
