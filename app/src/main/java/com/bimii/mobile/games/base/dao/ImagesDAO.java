package com.bimii.mobile.games.base.dao;

import com.bimii.mobile.api.models.based.Image;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.support.ConnectionSource;

import java.sql.SQLException;

public class ImagesDAO extends BaseDaoImpl<Image, Integer>{

    public ImagesDAO(ConnectionSource connectionSource, Class<Image> dataClass) throws SQLException {
        super(connectionSource, dataClass);
    }

}
