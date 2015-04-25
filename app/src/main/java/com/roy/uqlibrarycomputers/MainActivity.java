package com.roy.uqlibrarycomputers;

import android.app.Activity;
import android.app.DialogFragment;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Color;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends Activity{

    // Objects
    static ListView lview;
    static Button but;
    private Handler mHandler;

    static String mainColour;
    private List<LibraryComputer> computers;
    Map<String, String> buildings;
    String fname = "data.txt";

    String url = "https://www.library.uq.edu.au/home-page?qt-homepage_sidebar=2#qt-homepage_sidebar";

    /**
     * Updates the program's colours
     * @param colour
     */
    static public void updateColours(String colour){
        mainColour = colour;
        but.setBackgroundColor(Color.parseColor(colour));
        int count = lview.getChildCount();
        for (int i = 0; i < count; i++) {
            TextView tv = (TextView) lview.getChildAt(i);
            tv.setTextColor(Color.parseColor(colour));
        }
    }

    private static void loadLibraryNames(Map<String, String> dest, String fname, Context context){
        try {
            BufferedReader reader = new BufferedReader(
                    new InputStreamReader(context.getResources().getAssets().open(fname))
            );

            String line = "";
            while ((line = reader.readLine()) != null){
                String[] data = line.split(", ");
                if (data.length == 2){
                    dest.put(data[0], data[1]);
                }
            }

        } catch (Exception e){

        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        mainColour = getResources().getString(R.string.blue);

        lview = (ListView) findViewById(R.id.listView);
        but = (Button) findViewById(R.id.button);

        lview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (computers != null){
                    //Log.w("Test", computers.get(position).toString());
                    Intent myIntent = new Intent(getBaseContext(), MapActivity.class);
                    // Put data here

                    startActivity(myIntent);
                }
            }
        });

        // Load the names for the library
        buildings = new HashMap<String, String>();
        loadLibraryNames(buildings, fname, getApplicationContext());
        //Log.e("DATA", buildings.toString());

        //runWebscrape(url);
        refreshButton(null);
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
        ArrayList<String> data = new ArrayList<String>(); //old data class
        computers = new ArrayList<LibraryComputer>();
        String matches = "";
        String pattern = "<td class=\"left\">(.*?)</td>";
        String pattern2 = "<td class=\"right\">(.*?)</td>";
        Pattern r = Pattern.compile(pattern);
        Pattern p = Pattern.compile(pattern2);
        Matcher m = r.matcher(html);
        Matcher n = p.matcher(html);

        while (m.find() && n.find()){
            String libraryName = buildings.get(m.group(1));
            computers.add(new LibraryComputer(libraryName, n.group(1)));
            //matches = String.format("%s:  %s\n", m.group(1), n.group(1));
            //data.add(matches);
        }

        final List<LibraryComputer> finComputers = computers;
        //final ArrayList<String> finalData = data;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                MyAdapter adapter = new MyAdapter(MainActivity.this, finComputers);
                //ArrayAdapter adapter = new ArrayAdapter(MainActivity.this, R.layout.newlayout, finalData);
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

        // Get the colour working
        /*if (id == R.id.action_settings) {
            DialogFragment frag = new MyDialog();
            frag.show(getFragmentManager(), "colour_dialog");
            return true;
        }*/
        if (id == R.id.action_about){
            DialogFragment frag = new AboutDialog();
            frag.show(getFragmentManager(), "about_dialog");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
