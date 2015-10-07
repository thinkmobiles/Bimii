package com.bimii.mobile.games;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.TextView;

import com.bimii.mobile.BaseActivity;
import com.bimii.mobile.LoginActivity;
import com.bimii.mobile.R;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.dialogs.DownloadDialog;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.Loh;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;

public class GameActivity extends BaseActivity {

    @Bind(R.id.gvGames_AG)
    protected GridView gridViewGames;

    @Bind(R.id.stvEmptyGames_AG)
    protected TextView epmtyGames;

    private GamesAdapter gamesAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        ButterKnife.bind(this);
    }

    @Override
    protected void onStart() {
        super.onStart();
        loadGames();
    }

    @OnClick(R.id.ibSettings_AG)
    protected void clickSettings(View viewClicked) {
        openLogin();
    }

    @OnItemClick(R.id.gvGames_AG)
    protected void gameItemClick(AdapterView<?> parent, View view, int position, long id){
        startActivity(getPackageManager().getLaunchIntentForPackage(gamesAdapter.getItem(position).packageName));
    }

    private void initGridGame(final List<Game> _games) {
        if (_games.size() == 0)
            epmtyGames.setVisibility(View.VISIBLE);
        else
            epmtyGames.setVisibility(View.GONE);

        int columnCount = _games.size() < 3 ? _games.size() : 3;

        gridViewGames.setNumColumns(columnCount);
        gridViewGames.setAdapter(gamesAdapter = new GamesAdapter(this, _games));
    }

    private void loadGames() {
        try {
            initGridGame(removeDeletingApps(BaseHelperFactory.getHelper().getGameDAO().getAllGames()));
        } catch (SQLException e) {
            Loh.e("Error when reading games from base: " + e.getMessage());
        }
    }

    private List<Game> removeDeletingApps(List<Game> games) throws SQLException {
        for(Game game: games){
            if (!DownloadDialog.isContainsGameOnDevice(getApplicationContext(), game.getPackageName()))
                BaseHelperFactory.getHelper().removeGame(game);
        }
        return BaseHelperFactory.getHelper().getGameDAO().getAllGames();
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
    }
}
