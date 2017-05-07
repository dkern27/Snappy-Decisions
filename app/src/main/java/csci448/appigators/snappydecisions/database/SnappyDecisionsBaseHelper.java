package csci448.appigators.snappydecisions.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.RandomDecisionOptionTable;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.RandomDecisionTable;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.FoodDecisionTable;

/**
 * Created by dkern on 3/22/17.
 */

public class SnappyDecisionsBaseHelper extends SQLiteOpenHelper
{
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "SnappyDecisions.db";

    public SnappyDecisionsBaseHelper(Context context)
    {
        super(context, DATABASE_NAME, null, VERSION);

    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE " + RandomDecisionTable.NAME + "(" +
                RandomDecisionTable.Cols.NAME + " PRIMARY KEY" +
                ")"
        );

        db.execSQL("CREATE TABLE " + RandomDecisionOptionTable.NAME + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                RandomDecisionOptionTable.Cols.OPTION + ", " +
                RandomDecisionOptionTable.Cols.WEIGHT +  ", " +
                RandomDecisionOptionTable.Cols.DECISION + ", " +
                "FOREIGN KEY(" + RandomDecisionOptionTable.Cols.DECISION + ") REFERENCES " + RandomDecisionTable.NAME + "(" + RandomDecisionTable.Cols.NAME + ")" +
                ")"
        );

        db.execSQL("CREATE TABLE " + FoodDecisionTable.NAME + "(" +
                "_id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                FoodDecisionTable.Cols.NAME + ", " +
                FoodDecisionTable.Cols.RADIUS +  ", " +
                FoodDecisionTable.Cols.AMERICAN_FILTER +  ", " +
                FoodDecisionTable.Cols.ASIAN_FILTER +  ", " +
                FoodDecisionTable.Cols.MEXICAN_FILTER +  ", " +
                FoodDecisionTable.Cols.$_FILTER +  ", " +
                FoodDecisionTable.Cols.$$_FILTER +  ", " +
                FoodDecisionTable.Cols.$$$_FILTER +  ", " +
                FoodDecisionTable.Cols.$$$$_FILTER +
                ")"
        );

        //Make other tables
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {

    }
}
