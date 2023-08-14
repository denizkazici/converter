package com.example.convert_app;


import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.SpinnerAdapter;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView tvResult;
    EditText etFrom;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button btnConvert = findViewById(R.id.btnConvert);
        tvResult = findViewById(R.id.tvTo);
        etFrom = findViewById(R.id.etFrom);
        String[] from = new String[1];
        String[] to = new String[1];

        List<String> currency = Arrays.asList("TL", "USD", "EUR", "GBP", "UAH");
        Spinner spinnerFrom = findViewById(R.id.spinnerFrom);
        Spinner spinnerTo = findViewById(R.id.spinnerTo);
        SpinnerAdapter adapter = new spinnerAdapter(getApplicationContext(), currency);

        spinnerFrom.setAdapter(adapter);
        spinnerTo.setAdapter(adapter);
        spinnerFrom.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                from[0] = currency.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });
        spinnerTo.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                to[0] = currency.get(i);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) { }
        });

        btnConvert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String amount  = etFrom.getText().toString();
                if(from[0].equals("TL"))
                    from[0] = "TRY";
                if (to[0].equals("TL"))
                    to[0] = "TRY";
                String url = "https://api.exchangerate.host/convert?from=" + from[0] +"&to=" + to[0] + "&format=xml" + "&amount=" +amount;

                new HTTPAsyncTask().execute(url);
            }
        });







    }
    private class HTTPAsyncTask extends AsyncTask<String, Void, String > {

        protected String doInBackground(String... urls) { //buradaki .. 'nın anlamı arraylist gibi davranıyor
            try{
                return HttpGet(urls[0]);
            }catch (IOException e){
                return "Unable to retrieve web page. URL may be invalid";

            }
        }
        //onpostexecute displays the result of the asyncTask
        @Override
        protected void onPostExecute(String result){

           // tvResult.setText(result);
            try{
                XMLParser(result);
            }catch (XmlPullParserException e){
                e.printStackTrace();
            }
            catch (IOException e){
                e.printStackTrace();
            }

        }

        private void XMLParser(String result) throws XmlPullParserException, IOException{
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(true);
            XmlPullParser xpp = factory.newPullParser();

            xpp.setInput ( new StringReader( result ));
            int eventType = xpp.getEventType();
            String NewResult = "";
            String Tag = "";

            while(eventType != XmlPullParser.END_DOCUMENT){
                if(eventType == XmlPullParser.START_DOCUMENT){

                }else if (eventType == XmlPullParser.END_DOCUMENT){

                }else if (eventType == XmlPullParser.START_TAG){
                    Tag= xpp.getName();

                }else if (eventType == XmlPullParser.END_TAG){

                }else if (eventType == XmlPullParser.TEXT){
                    System.out.println(Tag);
                    if(Tag.equals("result")){

                        NewResult = xpp.getText();
                        Tag="";

                    }
                }
                eventType = xpp.next();
            }
            tvResult.setText(NewResult);

        }

        private String HttpGet (String myUrl) throws IOException{
            InputStream inputStream = null;
            String result = "";
            URL url = new URL(myUrl);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.connect();
            inputStream = conn.getInputStream();

            if (inputStream != null)
                result = convertInputStreamToString(inputStream);
            else
                result = "Did not work";
            return result;
        }
        private String convertInputStreamToString (InputStream inputStream) throws IOException{
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));

            String line ="";
            String result = "";
            while ((line = bufferedReader.readLine()) != null){
                result += line;

            }
            inputStream.close();
            return result;
        }

    }
}

