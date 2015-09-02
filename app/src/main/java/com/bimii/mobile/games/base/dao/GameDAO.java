package com.bimii.mobile.games.base.dao;

import com.bimii.mobile.api.models.based.Game;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;
import java.util.List;

public class GameDAO extends BaseDaoImpl<Game, Integer>{

    public GameDAO(ConnectionSource connectionSource, Class<Game> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

    public List<Game> getAllGames() throws SQLException {
        return this.queryForAll();
    }

}
