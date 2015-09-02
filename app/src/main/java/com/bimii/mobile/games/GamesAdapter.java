package com.bimii.mobile.games;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bimii.mobile.R;
import com.bimii.mobile.api.models.based.Game;

import java.util.List;

public class GamesAdapter extends BaseAdapter {

    private List<Game> mGames;
    private Context mContext;

    public GamesAdapter(Context _context, List<Game> _games) {
        this.mContext = _context;
        this.mGames = _games;
    }

    @Override
    public int getCount() {
        return mGames.size();
    }

    @Override
    public Game getItem(int position) {
        return mGames.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        GameViewHolder gameViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_game, parent, false);
            gameViewHolder = new GameViewHolder(convertView);
            convertView.setTag(gameViewHolder);
        } else gameViewHolder = (GameViewHolder) convertView.getTag();

        gameViewHolder.updateGame(mContext, getItem(position));
        return convertView;
    }
}
