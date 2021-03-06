package com.apaza.moises.visitsucre.database;

import android.database.sqlite.SQLiteDatabase;

import java.util.Map;

import de.greenrobot.dao.AbstractDao;
import de.greenrobot.dao.AbstractDaoSession;
import de.greenrobot.dao.identityscope.IdentityScopeType;
import de.greenrobot.dao.internal.DaoConfig;

import com.apaza.moises.visitsucre.database.User;
import com.apaza.moises.visitsucre.database.Category;
import com.apaza.moises.visitsucre.database.Place;
import com.apaza.moises.visitsucre.database.Image;

import com.apaza.moises.visitsucre.database.UserDao;
import com.apaza.moises.visitsucre.database.CategoryDao;
import com.apaza.moises.visitsucre.database.PlaceDao;
import com.apaza.moises.visitsucre.database.ImageDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see de.greenrobot.dao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig userDaoConfig;
    private final DaoConfig categoryDaoConfig;
    private final DaoConfig placeDaoConfig;
    private final DaoConfig imageDaoConfig;

    private final UserDao userDao;
    private final CategoryDao categoryDao;
    private final PlaceDao placeDao;
    private final ImageDao imageDao;

    public DaoSession(SQLiteDatabase db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        userDaoConfig = daoConfigMap.get(UserDao.class).clone();
        userDaoConfig.initIdentityScope(type);

        categoryDaoConfig = daoConfigMap.get(CategoryDao.class).clone();
        categoryDaoConfig.initIdentityScope(type);

        placeDaoConfig = daoConfigMap.get(PlaceDao.class).clone();
        placeDaoConfig.initIdentityScope(type);

        imageDaoConfig = daoConfigMap.get(ImageDao.class).clone();
        imageDaoConfig.initIdentityScope(type);

        userDao = new UserDao(userDaoConfig, this);
        categoryDao = new CategoryDao(categoryDaoConfig, this);
        placeDao = new PlaceDao(placeDaoConfig, this);
        imageDao = new ImageDao(imageDaoConfig, this);

        registerDao(User.class, userDao);
        registerDao(Category.class, categoryDao);
        registerDao(Place.class, placeDao);
        registerDao(Image.class, imageDao);
    }
    
    public void clear() {
        userDaoConfig.getIdentityScope().clear();
        categoryDaoConfig.getIdentityScope().clear();
        placeDaoConfig.getIdentityScope().clear();
        imageDaoConfig.getIdentityScope().clear();
    }

    public UserDao getUserDao() {
        return userDao;
    }

    public CategoryDao getCategoryDao() {
        return categoryDao;
    }

    public PlaceDao getPlaceDao() {
        return placeDao;
    }

    public ImageDao getImageDao() {
        return imageDao;
    }

}
