package jalal.ameer.statcalc2;
        //most of the information in this file is explained in CalcLike.java.
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class CalcMax extends Activity {
    //initialize variables
    int lines = 0;
    int count = 0;
    double mean=0;
    double stdev = 1;
    double increment = 0.11,threshold = 0.01;
    double[] D;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_max);
        Bundle b = getIntent().getExtras(); //accept variables from MainActivity
        if (b!=null){
            lines = b.getInt("lines");
            count = b.getInt("count");
            D = b.getDoubleArray("D");
        }
        userDev();  //Launch dialog boxes for user input
    }

    public void findMax(View v) {   //Perform operation on user command.
        StringBuilder max = new StringBuilder();
        max.append(calcMax(increment, mean, stdev, D, lines, threshold) + " is the most likely mean.");
        TextView maximum = (TextView)findViewById(R.id.maxView);
        maximum.setText(max);
    }


    public void userDev(){
        AlertDialog.Builder alert2 = new AlertDialog.Builder(this);

        alert2.setTitle("Standard Deviation");
        alert2.setMessage("Please enter the standard deviation of this set.");

        final EditText input2 = new EditText(this);
        alert2.setView(input2);

        alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                stdev = Double.parseDouble(input2.getText().toString());
            }
        });

        alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });
        alert2.show();
        userMean(); //launch next in sequence

    }

    public void userMean(){
        AlertDialog.Builder alert2 = new AlertDialog.Builder(this);

        alert2.setTitle("Mean");
        alert2.setMessage("Please enter the estimated Mean.");

        final EditText input2 = new EditText(this);
        alert2.setView(input2);

        alert2.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                mean = Double.parseDouble(input2.getText().toString());
            }
        });

        alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancel button
            }
        });

        alert2.show();
    }

    double calcLike(double mean, double stdev, double D[], int lines){
        //calculates the log likelihood of a given mean
        double density=0;
        for (int count = 0; count<lines; count++)		//loop for trying each value in text file for each mean
            density += Math.log((1/Math.sqrt(2*Math.PI*Math.pow(stdev,2)))*Math.exp(-(Math.pow(D[count]-mean,2)/(2*Math.pow(stdev,2)))));
        return density;
    }

    double derivative(double increment, double mean, double stdev, double D[], int lines){
        //approximates the derivative of a given value, given the data set
        double meanlo = mean - increment;								//for easier reading, meanhi is incremented
        double meanhi  = mean + increment;								//and meanlo is decremented
        return ((calcLike(meanhi, stdev, D, lines)	-calcLike(meanlo,stdev,D,lines))
                /(meanhi - meanlo));									//derivative formula and return
    }

    double derivative2(double increment, double mean, double stdev, double D[], int lines){
        //approximates the second derivative of a given value in a data set
        double meanlo = mean - increment;
        double meanhi = mean + increment;
        return ((derivative(increment,meanhi,stdev,D,lines)-derivative(increment,meanlo,stdev,D,lines))
                /(meanhi-meanlo));								//derivative2 formula and return
    }

    double calcMax(double increment, double mean, double stdev, double D[], int lines, double threshold){
        //this operation will repeatedly get closer to the actual mean in a data set as it loops
        double xn = mean;						//this is the current value given by user
        double xn1;								//this is the next value calculated below
        double improvement;						//this is how much better xn1 is than xn2
        int counter = 0;						//for counting how many times the operation runs
        do{
            xn1 = xn - (derivative(increment,xn,stdev,D,lines))/(derivative2(increment,xn,stdev,D,lines));
            improvement = xn1-xn;				//xn1 is defined, improvement is determined
            if (improvement>threshold)			//if the improvement is not acceptable
                xn = xn1;						//use xn1 as xn and continue loop
            counter++;
        }while(improvement>threshold);
        return xn1;								//return the maximum likelihood to the user
    }
}
