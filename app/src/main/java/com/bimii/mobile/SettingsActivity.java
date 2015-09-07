package com.bimii.mobile;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.dialogs.DownloadDialog;
import com.bimii.mobile.dialogs.ProgressDialog;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.settings.GamesSettingsAdapter;
import com.bimii.mobile.utils.Loh;

import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsActivity extends Activity implements Callback<List<Game>> {

    private ProgressDialog pdProgressView;

    @Bind(R.id.lvGames_AS)
    protected ListView listGames;
    private GamesSettingsAdapter mGamesSettingsAdapter;
    private Game gameDownload = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        pdProgressView = new ProgressDialog(this);
        loadGames();
    }

    @OnItemClick(R.id.lvGames_AS)
    protected void clickOnItemGames(AdapterView<?> parent, View view, int position, long id){
        gameDownload = mGamesSettingsAdapter.getItem(position);
        new DownloadDialog(this, gameDownload).show();
    }

    @OnClick(R.id.ivCancelSettings_AS)
    protected void closeSettings(){
        finish();
    }


    @OnClick({R.id.btnWiFi_AS, R.id.btnBluetooth_AS, R.id.btnLauncherSwitch_AS})
    protected void clickButtons(View viewClicked){
        switch (viewClicked.getId()){
            case R.id.btnWiFi_AS:
                break;
            case R.id.btnBluetooth_AS:
                break;
            case R.id.btnLauncherSwitch_AS:
                break;
        }
    }

    private void loadGames() {
        pdProgressView.show();
        ApiHelper.getInstance().getBimiiService().gameLibrary(ApiHelper.getInstance().getToken().token, this);
    }

    @Override
    public void success(List<Game> games, Response response) {
        pdProgressView.dismiss();
        Loh.i("Success GET_GAMES: " + games.size() + " games in response");
        listGames.setAdapter(mGamesSettingsAdapter = new GamesSettingsAdapter(this, games));
    }

    @Override
    public void failure(RetrofitError error) {
        pdProgressView.dismiss();
        Loh.i("Error GET_GAMES: " + error.getMessage());
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DownloadDialog.REQUEST_INSTALL_CODE){
            if (resultCode == RESULT_OK && gameDownload != null)
                try {
                    Loh.i("COMPLETE INSTALLING - GAME WRITED INTO BASE !!!");
                    BaseHelperFactory.getHelper().updateGame(gameDownload);
                } catch (SQLException e) {
                    e.printStackTrace();
                }
        }
    }
}
