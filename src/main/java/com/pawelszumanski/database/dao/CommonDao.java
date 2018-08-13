/*
 * Created by Paweł Szumański
 */

package com.pawelszumanski.database.dao;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.DaoManager;
import com.j256.ormlite.logger.Logger;
import com.j256.ormlite.logger.LoggerFactory;
import com.j256.ormlite.stmt.DeleteBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.pawelszumanski.database.dbutils.DbManager;
import com.pawelszumanski.database.models.BaseModel;
import com.pawelszumanski.utils.FxmlUtils;
import com.pawelszumanski.utils.exceptions.ApplicationExceptions;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

public abstract class CommonDao {
    public static final Logger LOGGER = LoggerFactory.getLogger(CommonDao.class);
    private final ConnectionSource connectionSource;

    public CommonDao(){
        this.connectionSource = DbManager.getConnectionSource();
    }

    public <T extends BaseModel, I> void createOrUpdate(BaseModel baseModel) throws ApplicationExceptions {
        Dao<T, I> dao = getDao((Class<T>) baseModel.getClass());
        try {
            dao.createOrUpdate((T) baseModel);
        } catch (SQLException e) {
            LOGGER.warn(e.getCause().getMessage());
            if(e.getCause().getMessage().contains("UNIQUE")){
                throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.data.database"));
            } else {
                throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.create.update"));
            }
        }finally {
            this.closeDbConnection();
        }
    }

    public <T extends BaseModel, I> void refresh(BaseModel baseModel) throws ApplicationExceptions {
        Dao<T, I> dao = getDao((Class<T>) baseModel.getClass());
        try {
            dao.refresh((T) baseModel);
        } catch (SQLException e) {
            LOGGER.warn(e.getCause().getMessage());
            throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.refresh"));
        }finally {
            this.closeDbConnection();
        }
    }

    public <T extends BaseModel, I> void delete(BaseModel baseModel) throws ApplicationExceptions {
        Dao<T, I> dao = getDao((Class<T>) baseModel.getClass());
        try {
            dao.delete((T) baseModel);
        } catch (SQLException e) {
            LOGGER.warn(e.getCause().getMessage());
            throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.delete"));
        } finally {
            this.closeDbConnection();
        }
    }

    public <T extends BaseModel, I> void deleteById(Class<T> cls, Integer id) throws ApplicationExceptions {
        Dao<T, I> dao = getDao(cls);
        try {
            dao.deleteById((I) id);
        } catch (SQLException e) {
            LOGGER.warn(e.getCause().getMessage());
            throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.delete"));
        } finally {
            this.closeDbConnection();
        }
    }

    public <T extends BaseModel, I> T findByID(Class<T> cls, Integer id) throws ApplicationExceptions {
        Dao<T, I> dao = getDao(cls);
        try {
            return dao.queryForId((I) id);
        } catch (SQLException e) {
            LOGGER.warn(e.getCause().getMessage());
            throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.not.found"));
        }finally {
            this.closeDbConnection();
        }
    }

    public<T extends BaseModel> void deleteByColumnName(Class<T> cls, String columnName, int id) throws ApplicationExceptions, SQLException {
        Dao<T, Object> dao = getDao(cls);
        DeleteBuilder<T, Object> deleteBuilder = dao.deleteBuilder();
        deleteBuilder.where().eq(columnName, id);
        dao.delete(deleteBuilder.prepare());
    }

    public <T extends BaseModel, I> List<T> queryForAll(Class<T> cls) throws ApplicationExceptions {
        Dao<T, I> dao = getDao(cls);
        try {

            return dao.queryForAll();
        } catch (SQLException e) {
            LOGGER.warn(e.getCause().getMessage());
            throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.not.found.all"));
        }finally {
            this.closeDbConnection();
        }
    }

//    public <T extends BaseModel, I> QueryBuilder<T, I> getQuerBuilder(Class<T> cls) throws ApplicationExceptions {
//        Dao<T, I> dao = getDao(cls);
//        return dao.queryBuilder();
//    }


    public <T extends BaseModel, I> Dao<T, I> getDao(Class<T> cls) throws ApplicationExceptions{
        try {
            return DaoManager.createDao(connectionSource, cls);
        } catch (SQLException e) {
            LOGGER.warn(e.getCause().getMessage());
            throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.get.dao"));
        } finally {
            this.closeDbConnection();
        }
    }

    private void closeDbConnection() throws ApplicationExceptions {
        try {
            this.connectionSource.close();
        } catch (IOException e) {
            LOGGER.warn(e.getCause().getMessage());
            throw new ApplicationExceptions(FxmlUtils.getResourceBundle().getString("error.get.dao"));
        }
    }
}
