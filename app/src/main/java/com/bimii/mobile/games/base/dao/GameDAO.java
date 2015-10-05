package com.bimii.mobile.games.base.dao;

import com.bimii.mobile.api.models.based.Game;
import com.bimii.mobile.settings.ActionGame;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class GameDAO extends BaseDaoImpl<Game, Integer>{

    public static final String COLUMN_ID = "id";

    public GameDAO(ConnectionSource connectionSource, Class<Game> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Game> getAllGames() throws SQLException {
        return this.queryForAll();
    }

    public Object[] isInstalled(final Game game){
        try {
            List<Game> results = queryBuilder().where().eq(COLUMN_ID, game.getId()).query();
            if (results == null || results.size() == 0)
                return new Object[]{ActionGame.DOWNLOAD, ""};
            else {
                final Game gameInBase = results.get(0);
                if (gameInBase.getVersion().equals(game.getVersion()))
                    return new Object[]{ActionGame.DELETE, gameInBase.packageName};
                else return new Object[]{ActionGame.UPDATE, gameInBase.packageName};
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return new Object[]{ActionGame.DOWNLOAD, ""};
    }

    // TODO But, maybe this moment is not have, I not know
    public List<Game> getGameOnlyLocalInstalled(final List<Game> gameInServer){
        final List<Integer> inData = new ArrayList<>();
        for (Game game: gameInServer)
            inData.add(game.getId());

        List<Game> result = new ArrayList<>();
        try {
            result = queryBuilder().where().notIn(COLUMN_ID, inData).query();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

}
