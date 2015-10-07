package com.bimii.mobile.games;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bimii.mobile.R;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.utils.TextCropper;
import com.squareup.picasso.Picasso;

import java.io.File;

import butterknife.Bind;
import butterknife.ButterKnife;

public final class GameViewHolder{

    @Bind(R.id.ivGameIcon_IG)
    public ImageView iconGame;

    @Bind(R.id.tvGameTitle_IG)
    public TextView titleGame;

    public GameViewHolder(View itemView) {
        ButterKnife.bind(this, itemView);
    }

    public void updateGame(Context context, Game _game){
        titleGame.setText(TextCropper.getNameIgnoreApk(_game.filename));
        File f = new File(_game.thumbnail_img_url);
        if (f.exists())
            Picasso
                    .with(context)
                    .load(f)
                    .placeholder(R.drawable.bg_item_menu)
                    .into(iconGame);
    }

}
