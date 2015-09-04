package com.bimii.mobile.settings;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bimii.mobile.R;
import com.bimii.mobile.api.models.based.Game;

import java.util.List;

public class GamesSettingsAdapter extends BaseAdapter {

    private List<Game> mGames;
    private Context mContext;

    public GamesSettingsAdapter(Context _context, List<Game> _games) {
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
        GameSettingsViewHolder gameSettingsViewHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(mContext).inflate(R.layout.settings_item_table_games, parent, false);
            gameSettingsViewHolder = new GameSettingsViewHolder(convertView);
            convertView.setTag(gameSettingsViewHolder);
        } else gameSettingsViewHolder = (GameSettingsViewHolder) convertView.getTag();

        gameSettingsViewHolder.updateGame(mContext, getItem(position));
        return convertView;
    }
}
