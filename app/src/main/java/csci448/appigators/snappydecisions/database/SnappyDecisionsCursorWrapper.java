package csci448.appigators.snappydecisions.database;

import android.database.Cursor;
import android.database.CursorWrapper;

import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.RandomDecisionOptionTable;
import csci448.appigators.snappydecisions.database.SnappyDecisionsSchema.RandomDecisionTable;

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
