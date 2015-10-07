package com.bimii.mobile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.bimii.mobile.api.ApiHelper;
import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.cache.CacheConstants;
import com.bimii.mobile.cache.CacheHelper;
import com.bimii.mobile.dialogs.DownloadDialog;
import com.bimii.mobile.dialogs.DownloadDialog.InstallGameEvent;
import com.bimii.mobile.dialogs.LauncherSwitchDialog;
import com.bimii.mobile.dialogs.ProgressDialog;
import com.bimii.mobile.dialogs.bluetooth.BtDialog;
import com.bimii.mobile.dialogs.wifi.WifiDialog;
import com.bimii.mobile.games.base.BaseHelperFactory;
import com.bimii.mobile.settings.ActionGame;
import com.bimii.mobile.settings.GamesSettingsAdapter;
import com.bimii.mobile.utils.Loh;
import com.bimii.mobile.utils.SecureProvider;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.OnItemClick;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

public class SettingsActivity extends BaseActivity implements Callback<List<Game>>, InstallGameEvent {

    private ProgressDialog pdProgressView;

    @Bind(R.id.lvGames_AS)
    protected ListView listGames;
    private GamesSettingsAdapter mGamesSettingsAdapter;
    private Game gameDownload = null;
    private String lastInstalledPackageName = "";
    private String lastInstalledImagePath = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        ButterKnife.bind(this);
        pdProgressView = new ProgressDialog(this);
        loadGames();
    }

    @OnItemClick(R.id.lvGames_AS)
    protected void clickOnItemGames(AdapterView<?> parent, View view, int position, long id) {
        gameDownload = mGamesSettingsAdapter.getItem(position);

        if (gameDownload.actionGame.equals(ActionGame.DOWNLOAD) || gameDownload.actionGame.equals(ActionGame.UPDATE))
            new DownloadDialog(this, gameDownload, this).show();
        else if (gameDownload.actionGame.equals(ActionGame.DELETE))
            DownloadDialog.uninstallApplication(this, gameDownload.packageName);
    }

    @OnClick(R.id.ivCancelSettings_AS)
    protected void closeSettings() {
        finish();
    }


    @OnClick({R.id.btnWiFi_AS, R.id.btnBluetooth_AS, R.id.btnLauncherSwitch_AS})
    protected void clickButtons(View viewClicked) {
        switch (viewClicked.getId()) {
            case R.id.btnWiFi_AS:
                new WifiDialog(this).show();
                break;
            case R.id.btnBluetooth_AS:
                new BtDialog(this).show();
                break;
            case R.id.btnLauncherSwitch_AS:
                new LauncherSwitchDialog(this).show();

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
        listGames.setAdapter(mGamesSettingsAdapter = new GamesSettingsAdapter(this, getSynchronizedGames(games)));
    }

    @Override
    public void failure(RetrofitError error) {
        pdProgressView.dismiss();
        Loh.i("Error GET_GAMES: " + error.getMessage());
        checkStatus(error.getResponse() != null ? error.getResponse().getStatus() : 400);
    }

    private List<Game> getSynchronizedGames(List<Game> serverGames) {
        try {
            for (Game game : serverGames) {
                Object[] lotInBase = BaseHelperFactory.getHelper().getGameDAO().isInstalled(game);
                game.actionGame = (ActionGame) lotInBase[0];
                game.packageName = (String) lotInBase[1];
            }
            serverGames.addAll(BaseHelperFactory.getHelper().getGameDAO().getGameOnlyLocalInstalled(serverGames));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return serverGames;
    }

    private void checkStatus(final int status) {
        if (status == 401) {
            Toast.makeText(this, R.string.error_token, Toast.LENGTH_LONG).show();
            CacheHelper.saveValue(getApplicationContext(), CacheConstants.CACHE_TOKEN, null);
            startActivity(new Intent(this, LoginActivity.class));
            finish();
        } else Toast.makeText(this, R.string.error_info, Toast.LENGTH_LONG).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == DownloadDialog.REQUEST_INSTALL_CODE && gameDownload != null) {
            if ((resultCode == RESULT_OK || resultCode == RESULT_CANCELED) && DownloadDialog.isContainsGameOnDevice(getApplicationContext(), lastInstalledPackageName)) {
                try {
                    Loh.i("COMPLETE INSTALLING - GAME WRITED INTO BASE !!!");
                    gameDownload.actionGame = ActionGame.DELETE;
                    gameDownload.packageName = lastInstalledPackageName;
                    gameDownload.thumbnail_img_url = lastInstalledImagePath;
                    BaseHelperFactory.getHelper().updateGame(gameDownload);
                    mGamesSettingsAdapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            } else {
                try {
                    SecureProvider.getGameDirectoryFile(this, gameDownload.getFilename(), false);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        } else if (requestCode == DownloadDialog.REQUEST_UNINSTALL_CODE && gameDownload != null) {
            if ((resultCode == RESULT_OK || resultCode == RESULT_CANCELED))
                try {
                    Loh.i("COMPLETE DELETING - GAME DELETED FROM BASE !!!");
                    gameDownload.actionGame = ActionGame.DOWNLOAD;
                    BaseHelperFactory.getHelper().removeGame(gameDownload);
                    SecureProvider.getGameDirectoryFile(this, gameDownload.getFilename(), false);
                    mGamesSettingsAdapter.notifyDataSetChanged();
                } catch (SQLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
        }

    }

    @Override
    public void onStartedInstallPackage(String packageName, String imagePath) {
        lastInstalledPackageName = packageName;
        lastInstalledImagePath = imagePath;
    }
}
