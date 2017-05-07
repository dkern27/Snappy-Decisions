package csci448.appigators.snappydecisions.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import java.util.ArrayList;
import java.util.Collections;

import csci448.appigators.snappydecisions.FoodFiltersActivity;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.RandomDecisionOptionTable;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.RandomDecisionTable;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.FoodDecisionTable;

/**
 * Created by dkern on 3/22/17.
 */

public class SnappyDecisionsCursorWrapper extends CursorWrapper
{
    /**
     * Creates a cursor wrapper.
     *
     * @param cursor The underlying cursor to wrap.
     */
    public SnappyDecisionsCursorWrapper(Cursor cursor)
    {
        super(cursor);
    }

    //This could probably be one function for all our types of decisions
    //Then we would just make a single NAME constant in the schema
    public String getRandomDecisionName()
    {
        String name = getString(getColumnIndex(RandomDecisionTable.Cols.NAME));
        return name;
    }

    public String getFoodDecisionName()
    {
        String name = getString(getColumnIndex(FoodDecisionTable.Cols.NAME));
        return name;
    }

    public int getFoodDecisionRadius()
    {
        int radius = getInt(getColumnIndex(FoodDecisionTable.Cols.RADIUS));
        return radius;
    }

    public ArrayList<Integer> getFoodDecisionFilters()
    {
        ArrayList<Integer> filter_info = new ArrayList<>(Collections.nCopies(FoodFiltersActivity.Filter.values().length, 0));
        filter_info.set(FoodFiltersActivity.Filter.AMERICAN.ordinal(), getInt(getColumnIndex(FoodDecisionTable.Cols.AMERICAN_FILTER)));
        filter_info.set(FoodFiltersActivity.Filter.ASIAN.ordinal(), getInt(getColumnIndex(FoodDecisionTable.Cols.ASIAN_FILTER)));
        filter_info.set(FoodFiltersActivity.Filter.MEXICAN.ordinal(), getInt(getColumnIndex(FoodDecisionTable.Cols.MEXICAN_FILTER)));
        filter_info.set(FoodFiltersActivity.Filter.$.ordinal(), getInt(getColumnIndex(FoodDecisionTable.Cols.$_FILTER)));
        filter_info.set(FoodFiltersActivity.Filter.$$.ordinal(), getInt(getColumnIndex(FoodDecisionTable.Cols.$$_FILTER)));
        filter_info.set(FoodFiltersActivity.Filter.$$$.ordinal(), getInt(getColumnIndex(FoodDecisionTable.Cols.$$$_FILTER)));
        filter_info.set(FoodFiltersActivity.Filter.$$$$.ordinal(), getInt(getColumnIndex(FoodDecisionTable.Cols.$$$$_FILTER)));
        return filter_info;
    }

    public String getRandomDecisionOptionText()
    {
        String option = getString(getColumnIndex(RandomDecisionOptionTable.Cols.OPTION));
        return option;
    }

    public String getRandomDecisionOptionWeight()
    {
        int weight = getInt(getColumnIndex(RandomDecisionOptionTable.Cols.WEIGHT));
        return String.valueOf(weight);
    }
}
