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
            public static final String NAME = "name";
            public static final String RADIUS = "radius";
            public static final String AMERICAN_FILTER = "american_filter";
            public static final String ASIAN_FILTER = "asian_filter";
            public static final String MEXICAN_FILTER = "mexican_filter";
            public static final String $_FILTER = "price_1_filter";
            public static final String $$_FILTER = "price_2_filter";
            public static final String $$$_FILTER = "price_3_filter";
            public static final String $$$$_FILTER = "price_4_filter";
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
