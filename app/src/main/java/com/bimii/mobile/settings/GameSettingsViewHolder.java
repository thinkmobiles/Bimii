package com.bimii.mobile.settings;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bimii.mobile.R;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.utils.TextCropper;
import com.squareup.picasso.Picasso;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

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
        titleGame.setText(TextCropper.getNameIgnoreApk(_game.getFilename()));
        versionGame.setText(String.valueOf(_game.getVersion()));
        availableGame.setText(_game.getUnlock_status() ? "Yes" : "No");
        actionGame.setText(_game.actionGame.name());
        actionGame.setTag(_game.actionGame);
        Picasso
                .with(context)
                .load(_game.thumbnail_img_url)
                .placeholder(R.drawable.bg_black_edit_rect)
                .into(iconGame);
    }

    @OnClick(R.id.tvAction_SITG)
    protected void clickAction(TextView viewClicked){
        switch ((ActionGame) viewClicked.getTag()){
            case DOWNLOAD:
                Toast.makeText(viewClicked.getContext(), "Click action - Download", Toast.LENGTH_SHORT).show();
                break;
            case DELETE:
                Toast.makeText(viewClicked.getContext(), "Click action - Delete", Toast.LENGTH_SHORT).show();
                break;
            case UPDATE:
                Toast.makeText(viewClicked.getContext(), "Click action - Update", Toast.LENGTH_SHORT).show();
                break;
        }
    }

}
