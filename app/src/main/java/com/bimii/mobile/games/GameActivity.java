package com.bimii.mobile.games;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.GridView;

import com.bimii.mobile.LoginActivity;
import com.bimii.mobile.R;
import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.Game;
import com.bimii.mobile.dialogs.ProgressDialog;
import com.bimii.mobile.utils.FontHelper;
import com.bimii.mobile.utils.Loh;

import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class GameActivity extends Activity implements Callback<List<Game>> {

    @Bind(R.id.gvGames_AG)
    protected GridView gridViewGames;
    private ProgressDialog pdProgressView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_games);

        ButterKnife.bind(this);
        pdProgressView = new ProgressDialog(this);

        FontHelper.init(getApplicationContext());

        loadGames();
    }

    @OnClick(R.id.ivSettings_AG)
    protected void clickSettings(View viewClicked) {
        openLogin();
    }

    private void initGridGame(final List<Game> _games) {
        int columnCount = _games.size() < 3 ? _games.size() : 3;
        gridViewGames.setLayoutParams(getCalculatedLayoutParams(columnCount));
        gridViewGames.setNumColumns(columnCount);
        gridViewGames.setAdapter(new GamesAdapter(this, _games));
    }

    private ViewGroup.LayoutParams getCalculatedLayoutParams(final int _columnCount) {
        ViewGroup.LayoutParams lp = gridViewGames.getLayoutParams();
        lp.width = (int) (_columnCount * getResources().getDimension(R.dimen.columnGameWidth)    // Columns width
                        + (_columnCount - 1) * gridViewGames.getVerticalSpacing()               // Horizontal Spacing width
                        + gridViewGames.getPaddingLeft() + gridViewGames.getPaddingRight());   // Left/Right padding's
        return lp;
    }

    private void loadGames() {
        //TODO from data base
        pdProgressView.show();
        ApiHelper.getInstance().getBimiiService().gameLibrary("d7u1kRwMsc1RfknulN6nGz5ctj7UYff3UdSj6MJ3PtaehwGG2aabom24kuTfCq30JmpLyt7", this);
    }

    private void openLogin() {
        startActivity(new Intent(this, LoginActivity.class));
        overridePendingTransition(R.anim.slide_in, R.anim.slide_out);
    }

    @Override
    public void success(List<Game> games, Response response) {
        pdProgressView.dismiss();
        Loh.i("Response Games count: " + games.size());
        initGridGame(games);
    }

    @Override
    public void failure(RetrofitError error) {
        pdProgressView.dismiss();
        Loh.i("Error GET_GAMES: " + error.getMessage());
    }
}
