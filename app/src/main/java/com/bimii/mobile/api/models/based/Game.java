package com.bimii.mobile.api.models.based;

import com.bimii.mobile.settings.ActionGame;
import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "games")
public final class Game {

    @DatabaseField(dataType = DataType.INTEGER, id = true, canBeNull = false)
    public int id;

    @DatabaseField(dataType = DataType.STRING)
    public String filename;

    @DatabaseField(dataType = DataType.INTEGER)
    public int version;

    @DatabaseField(dataType = DataType.STRING)
    public String created_at;

    @DatabaseField(dataType = DataType.STRING)
    public String updated_at;

    @DatabaseField(dataType = DataType.STRING)
    public String thumbnail_img_url;

    @DatabaseField(dataType = DataType.BOOLEAN)
    public boolean unlock_status;

    @DatabaseField(dataType = DataType.STRING)
    public String packageName;

    @DatabaseField(foreign = true, foreignAutoRefresh = true)
    public Thumbnail thumbnail;

    public ActionGame actionGame = ActionGame.NONE;

    public Game() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
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

    public String getThumbnail_img_url() {
        return thumbnail_img_url;
    }

    public void setThumbnail_img_url(String thumbnail_img_url) {
        this.thumbnail_img_url = thumbnail_img_url;
    }

    public boolean getUnlock_status() {
        return unlock_status;
    }

    public void setUnlock_status(boolean unlock_status) {
        this.unlock_status = unlock_status;
    }

    public Thumbnail getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(Thumbnail thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }
}
