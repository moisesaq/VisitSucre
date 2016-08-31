package com.moises;

import de.greenrobot.daogenerator.DaoGenerator;
import de.greenrobot.daogenerator.Entity;
import de.greenrobot.daogenerator.Property;
import de.greenrobot.daogenerator.Schema;
import de.greenrobot.daogenerator.ToMany;

public class MainGenerator extends UtilDB{

    private static Entity category, place;

    public static void main(String[] args){
        Schema schema = new Schema(1, "com.apaza.moises.visitsucre.database");
        generateCategory(schema);
        generatePlace(schema);
        generateImage(schema);
        try{
            DaoGenerator daoGenerator = new DaoGenerator();
            daoGenerator.generateAll(schema, "./app/src/main/java");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    private static void generateCategory(Schema schema){
        category = schema.addEntity(Table.CATEGORY);
        category.addIdProperty();
        category.addStringProperty(ColumnCategory.NAME);
        category.addStringProperty(ColumnCategory.LOGO);
        category.addDateProperty(ColumnCategory.CREATED_AT);
        category.addStringProperty(ColumnCategory.DESCRIPTION);
        category.addContentProvider();
    }

    private static void generatePlace(Schema schema){
        place = schema.addEntity(Table.PLACE);
        place.addIdProperty();
        place.addStringProperty(ColumnPlace.NAME);
        place.addStringProperty(ColumnPlace.ADDRESS);
        place.addDoubleProperty(ColumnPlace.LATITUDE);
        place.addDoubleProperty(ColumnPlace.LONGITUDE);
        place.addStringProperty(ColumnPlace.DESCRIPTION);
        place.addBooleanProperty(ColumnPlace.FAVORITE);
        Property createdAt = place.addDateProperty(ColumnPlace.CREATED_AT).getProperty();
        Property idCategory = place.addLongProperty(ColumnPlace.ID_CATEGORY).getProperty();
        place.addToOne(category, idCategory);

        ToMany categoryToPlace = category.addToMany(place, idCategory);
        categoryToPlace.setName("CategoryPlace");
        categoryToPlace.orderAsc(createdAt);

        place.addContentProvider();
    }

    private static void generateImage(Schema schema){
        Entity image = schema.addEntity(Table.IMAGE);
        image.addIdProperty();
        image.addStringProperty(ColumnImage.PATH);
        image.addStringProperty(ColumnImage.DESCRIPTION);
        Property idPlace = image.addLongProperty(ColumnImage.ID_PLACE).getProperty();
        image.addToOne(place, idPlace);

        ToMany placeToImage = place.addToMany(image, idPlace);
        placeToImage.setName("PlaceImage");

        image.addContentProvider();
    }
}
