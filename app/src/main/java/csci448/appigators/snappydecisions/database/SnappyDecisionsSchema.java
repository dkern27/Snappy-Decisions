package csci448.appigators.snappydecisions.database;

/**
 * Created by dkern on 3/22/17.
 */

public class SnappyDecisionsSchema
{
    public static final class RandomDecisionTable
    {
        public static final String NAME = "RandomDecision";
        public static final class Cols
        {
            public static final String NAME = "name";
        }
    }

    public static final class RandomDecisionOptionTable
    {
        public static final String NAME = "RandomDecisionOption";
        public static final class Cols
        {
            public static final String OPTION = "option";
            public static final String WEIGHT = "weight";
            public static final String DECISION = "decision";
        }
    }

    public static final class FoodDecisionTable
    {
        public static final String NAME = "FoodDecision";
        public static final class Cols
        {

        }
    }

    public static final class ProductDecisionTable
    {
        public static final String NAME = "ProductDecision";
        public static final class Cols
        {

        }
    }
}
