package mukesh.com.task9;


import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

public class GetQuoteFragment extends Fragment{
    private View view;
    private int cat_id;
    private ListView listView;
    private CustomListViewAdapter adapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.lv_fragment, container,false);

        if (getArguments() != null) {
            cat_id = getArguments().getInt("pos");
            new Quotes().execute("http://rapidans.esy.es/test/getquotes.php?cat_id="+cat_id);
        }

        return view;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    class Quotes extends AsyncTask<String,Void,String>
    {
        private ProgressDialog dialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog = new ProgressDialog(getActivity());
            dialog.setMessage("Loading...");
            dialog.setCancelable(false);
            dialog.show();
        }

        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection connection;
            try {
                URL url = new URL(params[0]);
                try {
                    connection = (HttpURLConnection)url.openConnection();
                    connection.connect();

                    InputStream stream = connection.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(stream));

                    StringBuffer buffer = new StringBuffer();
                    String line = "";

                    while ((line =reader.readLine())!= null){
                        buffer.append(line);
                    }
                    String bufferString = buffer.toString();
                    return  bufferString;

                } catch (IOException e) {
                    e.printStackTrace();
                }
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            if (dialog.isShowing()){
                dialog.dismiss();
            }
            super.onPostExecute(s);

            ArrayList<QuotesPost> arrayList = new ArrayList<>();
            super.onPostExecute(s);
            try {
                JSONObject rootObject = new JSONObject(s);
                JSONArray dataObject = rootObject.getJSONArray("data");
                for (int i = 0; i <dataObject.length() ; i++) {

                    JSONObject obj = dataObject.getJSONObject(i);
                    QuotesPost quotesPost = new QuotesPost();

                    int id = obj.getInt("id");
                    int cat_id = obj.getInt("cat_id");
                    String quotes = obj.getString("quotes");

                    String TAG = "JSON Parsing";
                    Log.d(TAG,"ID: " +id);
                    Log.d(TAG, "cat_id: "+cat_id);
                    Log.d(TAG, "Quotes: "+quotes);

                    quotesPost.setId(id);
                    quotesPost.setCat_id(cat_id);
                    quotesPost.setQuote(quotes);

                    arrayList.add(quotesPost);
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            listView = (ListView)view.findViewById(R.id.listview_id);
            adapter = new CustomListViewAdapter(getActivity(), R.layout.lv_quotes, arrayList);
            listView.setAdapter(adapter);
        }
    }
}
