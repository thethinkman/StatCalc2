////////////////////////////
//Project Name:	Statistics Calculator (MainActivity.java, CalcLike.java, CalcMax.java)
//Author:		Ameer Reza Jalal
//Date:			12/01/2014
//Description: 	This program for Android devices is a basic statistics calculator that performs
//              functions that have been covered in the course, in addition to mean and standard
//              deviation. It uses the Storage Access Framework, introduced in Android 4.4.4.
//              Users on older versions of Android will still be able to use the application,
//              but will be prompted to use an installed file explorer.
////////////////////////////
package jalal.ameer.statcalc2;

import android.app.Activity;        //all android applications run in activities.
import android.content.Intent;      //Intent allows for running other activities from this one
import android.net.Uri;             //URI is similar to a URL, it's an address to a file.
import android.os.Bundle;           //Bundle is a way to transfer variable data between activities
import android.util.Log;            //This allows for more advanced logcats.
import android.view.View;           //For the android.view files, these are necessary for the menu
import android.widget.TextView;     //This is for modifiable, viewable text areas.

import java.io.BufferedReader;      //these io files are for reading input
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;


public class MainActivity extends Activity {
    //extends activity allows for this class to be used as an android Activity
    //setting up variables for later use
    double[] D;                                         //data points will go in this array
    int lines=0,count=0;                                //counters for processing
    private static final int READ_REQUEST_CODE = 42;    //This code is to label the incoming data
    private static final String TAG = "Incoming file";  //This code is to label the log data
    Uri userFile;                                       //stores the location of the chosen file

    @Override          //overloads the default version of the following function with my own.
    protected void onCreate(Bundle savedInstanceState) {
        //onCreate happens when the program launches.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //these top two are mandatory for proper program functions
    }

    public void dataView(View v) throws IOException {
        //for reading in data

        StringBuilder text = new StringBuilder();
            //StringBuilder holds text for printing and other use.
        BufferedReader br;  //for reading from file
        try {

            InputStream inputStream = getContentResolver().openInputStream(userFile);
            br = new BufferedReader(new InputStreamReader(
                    inputStream));
            String tempS = br.readLine();							//throwaway string

            while(tempS!=null){									//count number of lines in file
                tempS = br.readLine();
                lines++;
            }
            inputStream.close();
            br.close();														//close file

            text.append("You've read a file! "+lines+" lines found. ");    //add to StringBuilder

            D = null;                                   //clear array first (support multiple runs)
            D = new double[lines];		        		//create new array using number of lines
            inputStream = getContentResolver().openInputStream(userFile);   //reopen file
            br = new BufferedReader(new InputStreamReader(
                    inputStream));
            while(count<lines){							//dump values from file into double array
                tempS = br.readLine();
                D[count] = Double.parseDouble(tempS);   //parse strings from file as doubles
                count++;
            }
            inputStream.close();
            br.close();                                 //close file when done

        } catch (IOException e) {
            e.printStackTrace();                        //catch errors
        }
        catch (NumberFormatException e){
            e.printStackTrace();
        }
        //Find the view by its id. This is used to find the element in the XML to overwrite.
        TextView tv = (TextView)findViewById(R.id.textView);
        //Set the text. This overwrites the value in the XML.
        tv.setText(text);
    }

    public void performFileSearch(View v) {
        //This method utilizes the Storage Access Framework provided by Google.
        //This code has been  modified from Google's publicly available recommended code.
        //Source: https://developer.android.com/guide/topics/providers/document-provider.html

        // ACTION_OPEN_DOCUMENT is the intent to choose a file via the system's file
        // browser.
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);

        // Filter to only show results that can be "opened", such as a
        // file (as opposed to a list of contacts or timezones)
        intent.addCategory(Intent.CATEGORY_OPENABLE);

        // MIME data type.
        // search for all documents available via installed storage providers,
        // it would be "*/*".
        intent.setType("*/*");
        //Starts the intent, feeding it the READ_REQUEST_CODE for return later.
        startActivityForResult(intent, READ_REQUEST_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode,
                                 Intent resultData) {
        //After startActivityForResult completes, it automatically launches onActivityResult.
        //This overload allows for interpretation of the data from the user chosen file.
        //This code has been  modified from Google's publicly available recommended code.
        //Source: https://developer.android.com/guide/topics/providers/document-provider.html

        // The ACTION_GET_CONTENT intent was sent with the request code
        // READ_REQUEST_CODE. If the request code seen here doesn't match, it's the
        // response to some other intent, and the code below shouldn't run at all.

        if (requestCode == READ_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            // The document selected by the user won't be returned in the intent.
            // Instead, a URI to that document will be contained in the return intent
            //provided to this method as a parameter.
            // Pull that URI using resultData.getData().
            Uri uri;
            if (resultData != null) {
                uri = resultData.getData();
                Log.i(TAG, "Uri: " + uri.toString());       //log
                userFile = uri;                             //store for later use
                try {
                    dataView(findViewById(R.id.textView));  //launch dataView for use of file
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }


    //These three method calls launch the other activities (classes)
    public void meanDev(View v){
          //find mean and standard deviation
        Bundle b = new Bundle();    //create bundle for data transfer to other class
        b.putDoubleArray("D",D);    //store these. "D" refers to the access key, while D is the value.
        b.putInt("lines",lines);
        b.putInt("count",count);
        Intent i = new Intent(this,MeanDev.class);
            //Intent specifies where you came from, and where you're going.
        i.putExtras(b);            //This stores the bundle b in the intent.
        this.startActivity(i);     //this begins the new program.
    }
    //The following functions are identical but start their respective classes.
    public void calcLike(View v){
            //calculate log likelihood
        Bundle b = new Bundle();
        b.putDoubleArray("D",D);
        b.putInt("lines",lines);
        b.putInt("count",count);
        Intent i = new Intent(this,CalcLike.class);
        i.putExtras(b);
        this.startActivity(i);
    }

    public void calcMax(View v){
            //calculate maximum likelihood
        Bundle b = new Bundle();
        b.putDoubleArray("D",D);
        b.putInt("lines",lines);
        b.putInt("count",count);
        Intent i = new Intent(this,CalcMax.class);
        i.putExtras(b);
        this.startActivity(i);
    }
}
