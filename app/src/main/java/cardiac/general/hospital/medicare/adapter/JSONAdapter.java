package cardiac.general.hospital.medicare.adapter;

import android.app.Activity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cardiac.general.hospital.medicare.R;

/**
 * Created by Syed Minhaj ul Hasan on 8/6/2017.
 */

public class JSONAdapter extends BaseAdapter implements ListAdapter {

    private final Activity activity;
    private final JSONArray jsonArray;
    public JSONAdapter(Activity activity, JSONArray jsonArray) {
        assert activity != null;
        assert jsonArray != null;

        this.jsonArray = jsonArray;
        this.activity = activity;
    }


    @Override public int getCount() {
        return jsonArray.length();
    }

    @Override public JSONObject getItem(int position) {
        return jsonArray.optJSONObject(position);
    }

    @Override public long getItemId(int position) {
        JSONObject jsonObject = getItem(position);

        return jsonObject.optLong("id");
    }


    @Override public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null)
            convertView = activity.getLayoutInflater().inflate(R.layout.specialities_row, null);
            TextView text =(TextView)convertView.findViewById(R.id.Specialities_Name);

        JSONObject json_data = getItem(position);
        if(null!=json_data ){
            String jj= null;
            try {
                jj = json_data.getString("Name");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            text.setText(jj);
        }

        return convertView;
    }
}