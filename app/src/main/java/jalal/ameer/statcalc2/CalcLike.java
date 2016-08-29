package jalal.ameer.statcalc2;
        //much of the information in this file is explained in MainActivity.java
import android.app.Activity;
import android.app.AlertDialog;                     //This is for pop-up alerts. Used for input.
import android.content.DialogInterface;             //allow the user to interface with dialogs
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod; //This is used for a nice scrolling text-box.
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;


public class CalcLike extends Activity {
    //variable initialization
    int lines = 0;  //number of lines in file
    int count = 0;  //from MainActivity
    double meanmin = 0, meanmax=0, density=0; //for log likelihood calculation
    double stdev = 1;
    double increment = 0.01;
    double[] D;     //data points from MainActivity

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calc_like);
        Bundle b = getIntent().getExtras(); //this receives the variables from the other activity
        if (b!=null){                       //if successful (not null) copy the variables over
            lines = b.getInt("lines");
            count = b.getInt("count");
            D = b.getDoubleArray("D");
        }
        userMeanmin();                      //This launches a series of dialog boxes asking for user
                                            //input
    }

    public void findLike(View v){
            //This method ties together this file and is launched by a button press to give the
            //final result.
        StringBuilder likelihood = new StringBuilder();                 //for output display
        for (double mean = meanmin; mean<=meanmax; mean+=increment){	//loop for trying each mean incremented from minMean to MaxMean
            density = calcLike(mean,stdev,D,lines);						//log likelihood method call
            likelihood.append("For mean " + mean + " the log likelihood is " + density + '\n');
        }
        TextView like = (TextView)findViewById(R.id.likeResult);
        like.setText(likelihood);
        like.setMovementMethod(new ScrollingMovementMethod());          //This allows for scrolling
    }

    public void userMeanmin(){
            //alert box for minimum mean input
           //these alert boxes are all the same, I will go in depth once.
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
            //create alert dialog box named "alert"

        alert.setTitle("Minimum Mean"); //set the title and message to be displayed to the user
        alert.setMessage("Please enter the minimum mean to test.");

        final EditText input = new EditText(this);  //accept user input
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {            //upon clicking "OK"
                meanmin = Double.parseDouble(input.getText().toString());   //take user input and store it
                StringBuilder meanViewer = new StringBuilder();             //then display it to the screen
                meanViewer.append(meanmin);
                TextView meanview = (TextView)findViewById(R.id.MeanGiven); //display on screen to the user
                meanview.setText(Double.toString(meanmin));
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {   //setup cancel button
            public void onClick(DialogInterface dialog, int whichButton) {
                // Cancel button.
            }
        });

        alert.show();
        userMeanmax();  //launch next method in this sequence
    }

    public void userMeanmax(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Maximum Mean");
        alert.setMessage("Please enter the maximum mean to test.");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                meanmax = Double.parseDouble(input.getText().toString());
                StringBuilder meanViewerMax = new StringBuilder();
                meanViewerMax.append(meanmax);
                TextView meanviewmax = (TextView)findViewById(R.id.maxmeanGiven);
                meanviewmax.setText(Double.toString(meanmax));
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
        userDev();
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
                StringBuilder stdevViewer = new StringBuilder();
                stdevViewer.append(stdev);
                TextView stdevview = (TextView)findViewById(R.id.stdevGiven);
                stdevview.setText(Double.toString(stdev));
            }
        });

        alert2.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert2.show();
        userInc();
    }

    public void userInc(){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);

        alert.setTitle("Mean Increment");
        alert.setMessage("Please enter your desired mean increment (smaller = more accuracy).");

        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                increment = Double.parseDouble(input.getText().toString());
                StringBuilder incViewer = new StringBuilder();
                incViewer.append(increment);
                TextView incview = (TextView)findViewById(R.id.IncGiven);
                incview.setText(Double.toString(increment));
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                // Canceled.
            }
        });

        alert.show();
    }

    double calcLike(double mean, double stdev, double D[], int lines){
        //this has the actual likelihood density calculation.
        double density=0;
        for (int count = 0; count<lines; count++)		//loop for trying each value in text file for each incremented mean
            density += Math.log((1/Math.sqrt(2*Math.PI*Math.pow(stdev,2)))*Math.exp(-(Math.pow(D[count]-mean,2)/(2*Math.pow(stdev,2)))));
        return density;
    }

}
