package csci448.appigators.snappydecisions;

/**
 * Created by dkern on 3/3/17.
 */

public class RandomDecisionOption
{
    private String mOption;
    private int mWeight;

    /**
     * Default constructor
     */
    public RandomDecisionOption()
    {
        this.mOption = "";
        this.mWeight = 0;
    }

    /**
     * Non-default constructor
     * @param option
     * @param weight
     */
    public RandomDecisionOption(String option, int weight)
    {
        this.mOption = option;
        this.mWeight = weight;
    }

    public String getOption()
    {
        return mOption;
    }

    public void setOption(String option)
    {
        mOption = option;
    }

    public int getWeight()
    {
        return mWeight;
    }

    public void setWeight(int weight)
    {
        mWeight = weight;
    }
}
