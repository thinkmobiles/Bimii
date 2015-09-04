package com.bimii.mobile.games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;

import com.bimii.mobile.LoginActivity;
import com.bimii.mobile.R;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.Loh;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GameActivity extends Activity {

    @Bind(R.id.gvGames_AG)
    protected GridView gridViewGames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        ButterKnife.bind(this);

        FontHelper.init(getApplicationContext());

        loadGames();
    }

    @OnClick(R.id.ibSettings_AG)
    protected void clickSettings(View viewClicked) {
        openLogin();
    }

    private void initGridGame(final List<Game> _games) {
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
//        _games.add(_games.get(0));
        int columnCount = _games.size() < 3 ? _games.size() : 3;
        gridViewGames.setNumColumns(columnCount);
        gridViewGames.setAdapter(new GamesAdapter(this, _games));
    }

    private void loadGames() {
        try {
            initGridGame(BaseHelperFactory.getHelper().getGameDAO().getAllGames());
        } catch (SQLException e) {
            Loh.e("Error when reading games from base: " + e.getMessage());
        }
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        //TODO fix animation
//        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }
}
