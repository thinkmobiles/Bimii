package com.bimii.mobile.settings;

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

public final class GameSettingsViewHolder {

    @Bind(R.id.ivGame_SITG)
    public ImageView iconGame;

    @Bind(R.id.tvGame_SITG)
    public TextView titleGame;

    @Bind(R.id.tvVersion_SITG)
    public TextView versionGame;

    @Bind(R.id.tvAvailable_SITG)
    public TextView availableGame;

    @Bind(R.id.tvAction_SITG)
    public TextView actionGame;

    public GameSettingsViewHolder(View itemView) {
        ButterKnife.bind(this, itemView);
    }

    public void updateGame(Context context, Game _game){
        titleGame       .setText(TextCropper.getNameIgnoreApk(_game.getFilename()));
        versionGame     .setText(_game.getVersion());
        availableGame   .setText(_game.getUnlock_status() ? "Yes" : "No");
        actionGame      .setText(_game.actionGame.name());
        actionGame      .setTag(_game.actionGame);

        File f = new File(_game.thumbnail_img_url);
        if (f.exists())
            Picasso
                    .with(context)
                    .load(f)
                    .placeholder(R.drawable.bg_item_menu)
                    .error(R.drawable.bg_item_menu)
                    .into(iconGame);
        else
            Picasso
                    .with(context)
                    .load(_game.thumbnail_img_url)
                    .placeholder(R.drawable.bg_item_menu)
                    .error(R.drawable.bg_item_menu)
                    .into(iconGame);
    }

}
