package com.bimii.mobile.api.models.based;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "thumbnails")
public final class Thumbnail {

    @DatabaseField(dataType = DataType.INTEGER, id = true, canBeNull = false)
    public int id;

    @DatabaseField(dataType = DataType.INTEGER)
    public int image_id;

    @DatabaseField(dataType = DataType.INTEGER)
    public int game_id;

    @DatabaseField(dataType = DataType.STRING)
    public String created_at;

    @DatabaseField(dataType = DataType.STRING)
    public String updated_at;

    @DatabaseField(foreign = true, columnName = "image_foreign_id", foreignAutoRefresh = true)
    public Image image;

    public Thumbnail() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImage_id() {
        return image_id;
    }

    public void setImage_id(int image_id) {
        this.image_id = image_id;
    }

    public int getGame_id() {
        return game_id;
    }

    public void setGame_id(int game_id) {
        this.game_id = game_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getUpdated_at() {
        return updated_at;
    }

    public void setUpdated_at(String updated_at) {
        this.updated_at = updated_at;
    }

    public Image getImage() {
        return image;
    }

    public void setImage(Image image) {
        this.image = image;
    }
}
