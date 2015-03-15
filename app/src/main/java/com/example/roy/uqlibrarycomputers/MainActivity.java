package com.example.roy.uqlibrarycomputers;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Handler;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

class MyDialog extends DialogFragment{
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState){
        final int[] colours = {R.string.blue, R.string.green};
        String[] options = {"Blue", "Green"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Set Colour");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MainActivity.updateColours(getResources().getString(colours[which]));
            }
        });
        return builder.create();
    }
}

public class MainActivity extends ActionBarActivity{

    // Objects
    static ListView lview;
    static Button but;
    private Handler mHandler;

    static String mainColour;

    String url = "https://www.library.uq.edu.au/home-page?qt-homepage_sidebar=2#qt-homepage_sidebar";

    static public void updateColours(String colour){
        mainColour = colour;
        but.setBackgroundColor(Color.parseColor(colour));
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mainColour = getResources().getString(R.string.blue);

        lview = (ListView) findViewById(R.id.listView);
        but = (Button) findViewById(R.id.button);

        runWebscrape(url);
    }

    /**
     * Update the list when the button is pressed
     * @param v
     */
    public void refreshButton(View v){
        but.setText("REFRESHING");
        but.setBackgroundColor(Color.parseColor(getResources().getString(R.string.orange)));
        but.setEnabled(false);
        runWebscrape(url);
    }

    /**
     * Runs a regex search to extract all of the computer availability information
     * @param html html source code to search through
     * @return An arraylist of strings that contains formatted data
     */
    private void getFreeComputers(String html){
        ArrayList<String> data = new ArrayList<String>();
        String matches = "";
        String pattern = "<td class=\"left\">(.*?)</td>";
        String pattern2 = "<td class=\"right\">(.*?)</td>";
        Pattern r = Pattern.compile(pattern);
        Pattern p = Pattern.compile(pattern2);
        Matcher m = r.matcher(html);
        Matcher n = p.matcher(html);

        while (m.find() && n.find()){
            matches = String.format("%s:  %s\n", m.group(1), n.group(1));
            data.add(matches);
        }

        final ArrayList<String> finalData = data;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.custom_layout, finalData);
                lview.setAdapter(adapter);
                adapter.notifyDataSetChanged();
                //Log.e("INFO", finalData.toString());

                //Set the button back
                but.setBackgroundColor(Color.parseColor(mainColour));
                but.setEnabled(true);
                but.setText("REFRESH");
            }
        });

    }

    /**
     * Runs a webscrape. Automatically threads.
     * @param sUrl The url to webscrape
     */
    private void runWebscrape(final String sUrl){
        Thread t1 = new Thread(new Runnable() {
            Document doc;

            @Override
            public void run() {

                try{
                    while (doc == null) {
                        doc = Jsoup.connect(sUrl).get();
                        Log.e("ERROR", "Doc is null");
                    }

                    getFreeComputers(doc.toString());

                } catch (Exception e){
                    Log.e("ERROR", e.toString());
                }

            }
        });
        t1.start();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            DialogFragment frag = new MyDialog();
            frag.show(getFragmentManager(), "colour_dialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
