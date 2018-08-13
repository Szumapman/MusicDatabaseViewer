/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.database.dbutils;

import com.j256.ormlite.jdbc.JdbcConnectionSource;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;
import com.pawelszumanski.database.models.Albums;
import com.pawelszumanski.database.models.Artists;
import com.pawelszumanski.database.models.Songs;

import java.io.IOException;
import java.sql.SQLException;

public class DbManager {

    private static final Logger LOGGER = LoggerFactory.getLogger(DbManager.class);

    public static final String JDBC_DRIVER_HD = "jdbc:sqlite:music.db";
    private static ConnectionSource connectionSource;

    public static void initDatabase() {
        createConnectionSource();
//        dropTable();
        createTable();
        closeConnectionSource();
    }

    private static void createConnectionSource() {
        try {
            connectionSource = new JdbcConnectionSource(JDBC_DRIVER_HD);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    private static void dropTable() {
        try {
            TableUtils.dropTable(connectionSource, Songs.class, true);
            TableUtils.dropTable(connectionSource, Albums.class, true);
            TableUtils.dropTable(connectionSource, Artists.class, true);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    private static void createTable() {
        try {
            TableUtils.createTableIfNotExists(connectionSource, Songs.class);
            TableUtils.createTableIfNotExists(connectionSource, Albums.class);
            TableUtils.createTableIfNotExists(connectionSource, Artists.class);
        } catch (SQLException e) {
            LOGGER.warn(e.getMessage());
        }
    }

    public static void closeConnectionSource() {
        if(connectionSource != null){
            try {
                connectionSource.close();
            } catch (IOException e) {
                LOGGER.warn(e.getMessage());
            }
        }
    }

    public static ConnectionSource getConnectionSource() {
        if(connectionSource == null){
            createConnectionSource();
        }
        return connectionSource;
    }
}
