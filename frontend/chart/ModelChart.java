package chart;

public class ModelChart {
	  private double value;
	    private String label1;

    public String getLabel() {
        return label1;
    }

    public void setLabel(String label) {
        this.label1 = label;
    }

    public double[] getValues() {
        return values;
    }

    public void setValues(double[] values) {
        this.values = values;
    }

    public ModelChart(String label, double[] values) {
        this.label1 = label;
        this.values = values;
    }


	public ModelChart(double value2, String string) {
		// TODO Auto-generated constructor stub
	}


	private String label;
    private double values[];

    public double getMaxValues() {
        double max = 0;
        for (double v : values) {
            if (v > max) {
                max = v;
            }
        }
        return max;
    }
}
