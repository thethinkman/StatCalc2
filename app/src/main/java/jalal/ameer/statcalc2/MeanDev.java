package jalal.ameer.statcalc2;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;


public class MeanDev extends Activity {

    int lines = 0;
    int count = 0;
    double[] D;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mean_dev);
        Bundle b = getIntent().getExtras();
        if (b!=null){
            lines = b.getInt("lines");
            count = b.getInt("count");
            D = b.getDoubleArray("D");

        }
        StringBuilder mean = new StringBuilder();
        StringBuilder stdev = new StringBuilder();
        mean.append(mean());
        stdev.append(stdev());
        TextView meanAns = (TextView)findViewById(R.id.meanAns);
        TextView stdevAns = (TextView)findViewById(R.id.stdevAns);
        meanAns.setText(mean);
        stdevAns.setText(stdev);
    }

    public double mean(){
        double sum = 0;
        for (count = 0;count < lines; count++)
            sum += D[count];
        return (sum/lines);
    }

    public double stdev(){
        double sum = 0, mean = mean();
        for (count = 0; count < lines; count++){
            sum+=(Math.pow((D[count]-mean),2));
        }
        return (Math.sqrt(sum/lines));
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mean_dev, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement


        return super.onOptionsItemSelected(item);
    }
}
