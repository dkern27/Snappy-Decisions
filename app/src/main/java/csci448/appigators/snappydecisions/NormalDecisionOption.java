package csci448.appigators.snappydecisions;

/**
 * Created by dkern on 3/3/17.
 */

public class NormalDecisionOption
{
    private String mOption;
    private double mWeight;

    public NormalDecisionOption(String option, double weight)
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

    public double getWeight()
    {
        return mWeight;
    }

    public void setWeight(double weight)
    {
        mWeight = weight;
    }
}
