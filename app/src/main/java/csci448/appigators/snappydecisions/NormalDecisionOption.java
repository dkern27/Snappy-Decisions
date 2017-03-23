package csci448.appigators.snappydecisions;

/**
 * Created by dkern on 3/3/17.
 */

public class NormalDecisionOption
{
    private String mOption;
    private int mWeight;

    public NormalDecisionOption()
    {
        this.mOption = "";
        this.mWeight = 0;
    }

    public NormalDecisionOption(String option, int weight)
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
