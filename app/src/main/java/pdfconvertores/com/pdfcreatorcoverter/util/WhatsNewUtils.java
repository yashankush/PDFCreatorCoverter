package pdfconvertores.com.pdfcreatorcoverter.util;

import android.app.Dialog;
import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import pdfconvertores.com.pdfcreatorcoverter.R;
import pdfconvertores.com.pdfcreatorcoverter.adapter.WhatsNewAdapter;
import pdfconvertores.com.pdfcreatorcoverter.model.WhatsNew;

public class WhatsNewUtils {

    public static void displayDialog(Context context) {
        final Dialog dialog = new Dialog(context);
        dialog.setContentView(R.layout.fragment_whats_new);
        RecyclerView rv = dialog.findViewById(R.id.whatsNewListView);
        TextView title = dialog.findViewById(R.id.title);
        Button continueButton = dialog.findViewById(R.id.continueButton);
        continueButton.setText(R.string.whatsnew_continue);
        title.setText(R.string.whatsnew_title);
        ArrayList<WhatsNew> whatsNewList;
        try {
            JSONObject obj = new JSONObject(loadJSONFromAsset(context));
            JSONArray data = obj.getJSONArray("data");
            whatsNewList = new ArrayList<>();

            for (int i = 0; i < data.length(); i++) {
                JSONObject jsonObject = data.getJSONObject(i);
                String newTitle = jsonObject.getString("title");
                String newContent = jsonObject.getString("content");
                String iconLocation = jsonObject.getString("icon");
                WhatsNew whatsNew = new WhatsNew(newTitle, newContent, iconLocation);
                whatsNewList.add(whatsNew);
            }
            WhatsNewAdapter whatsNewAdapter = new WhatsNewAdapter(context, whatsNewList);
            LinearLayoutManager layoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            rv.setLayoutManager(layoutManager);
            rv.setAdapter(whatsNewAdapter);
            dialog.show();
            continueButton.setOnClickListener(view -> dialog.dismiss());
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * Load data from json file
     *
     * @param context - current context
     * @return - json
     */
    private static String loadJSONFromAsset(Context context) {
        String json;
        try {
            InputStream is = context.getAssets().open("whatsnew.json");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }
}
