package com.bimii.mobile.games.base;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;

import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.api.models.based.Image;
import com.bimii.mobile.api.models.based.Thumbnail;
import com.bimii.mobile.games.base.dao.GameDAO;
import com.bimii.mobile.games.base.dao.ImagesDAO;
import com.bimii.mobile.games.base.dao.ThumbsDAO;
import com.bimii.mobile.utils.Loh;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper{

    private static final String DATABASE_NAME = "games.db";
    private static final int DATABASE_VERSION = 1;

    private GameDAO gameDAO = null;
    private ThumbsDAO thumbsDAO = null;
    private ImagesDAO imagesDAO = null;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        Loh.i("DataBaseHelper - constructor");
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        Loh.i("DataBaseHelper - onCreate");
        try
        {
            TableUtils.createTable(connectionSource, Image.class);
            TableUtils.createTable(connectionSource, Thumbnail.class);
            TableUtils.createTable(connectionSource, Game.class);
        }
        catch (SQLException e){
            Loh.e("Error creating DB " + DATABASE_NAME + ": " + e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {
        Loh.i("DataBaseHelper - onUpgrade");
    }

    public GameDAO getGameDAO() throws SQLException {
        if (gameDAO == null)
            gameDAO = new GameDAO(getConnectionSource(), Game.class);
        return gameDAO;
    }

    public ThumbsDAO getThumbsDAO() throws SQLException {
        if (thumbsDAO == null)
            thumbsDAO = new ThumbsDAO(getConnectionSource(), Thumbnail.class);
        return thumbsDAO;
    }

    public ImagesDAO getImagesDAO() throws SQLException {
        if (imagesDAO == null)
            imagesDAO = new ImagesDAO(getConnectionSource(), Image.class);
        return imagesDAO;
    }

    @Override
    public void close() {
        super.close();
        gameDAO = null;
    }


    public void updateGames(List<Game> games) throws SQLException {
        for (Game game: games) {
            updateGame(game);
        }
    }

    public void addGame(Game game) throws SQLException {
        getImagesDAO().create(game.thumbnail.image);
        getThumbsDAO().create(game.thumbnail);
        getGameDAO().create(game);
        Loh.i("Added in base: " + game.filename);
    }

    public void removeGame(Game game) throws SQLException {
        getImagesDAO().delete(game.thumbnail.image);
        getThumbsDAO().delete(game.thumbnail);
        getGameDAO().delete(game);
        Loh.i("Deleted from base: " + game.filename);
    }

    public void updateGame(Game game) throws SQLException {
        getImagesDAO().createOrUpdate(game.thumbnail.image);
        getThumbsDAO().createOrUpdate(game.thumbnail);
        getGameDAO().createOrUpdate(game);
        Loh.i("Inserted or Updated in base: " + game.filename);
    }
}
