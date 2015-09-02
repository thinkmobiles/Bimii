package com.bimii.mobile.api.models.based;

import com.j256.ormlite.field.DataType;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "images")
public final class Image {

    @DatabaseField(dataType = DataType.INTEGER, id = true, canBeNull = false)
    public int id;

    @DatabaseField(dataType = DataType.STRING)
    public String path;

    @DatabaseField(dataType = DataType.STRING)
    public String created_at;

    @DatabaseField(dataType = DataType.STRING)
    public String updated_at;

    public Image() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
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
}
