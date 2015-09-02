package com.bimii.mobile.games.base.dao;

import com.bimii.mobile.api.models.based.Thumbnail;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class ThumbsDAO extends BaseDaoImpl<Thumbnail, Integer>{

    public ThumbsDAO(ConnectionSource connectionSource, Class<Thumbnail> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

}
