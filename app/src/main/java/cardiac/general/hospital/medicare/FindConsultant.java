package cardiac.general.hospital.medicare;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FindConsultant extends AppCompatActivity {
    public String localjsonString = "{\"specilities\":[{\"Id\":\"30\",\"Name\":\"Accident And Emergency Care\"},{\"Id\":\"2\",\"Name\":\"Anesthesiology And Pain Management \"},{\"Id\":\"3\",\"Name\":\"Cardiology And Cardiothoracic Surgery\"},{\"Id\":\"32\",\"Name\":\"Clinical Nutrition And Dietetics\"},{\"Id\":\"33\",\"Name\":\"Critical Care\"},{\"Id\":\"34\",\"Name\":\"Day Care\"},{\"Id\":\"9\",\"Name\":\"Dental Surgery\"},{\"Id\":\"19\",\"Name\":\"Dermatology And Cosmetology\"},{\"Id\":\"23\",\"Name\":\"Dialysis\"},{\"Id\":\"6\",\"Name\":\"E.N.T\"},{\"Id\":\"10\",\"Name\":\"Endocrinology\"},{\"Id\":\"20\",\"Name\":\"Eye\"},{\"Id\":\"18\",\"Name\":\"Gastroenterology\"},{\"Id\":\"11\",\"Name\":\"General Surgery\"},{\"Id\":\"39\",\"Name\":\"Maxillo Facial\"},{\"Id\":\"12\",\"Name\":\"Medicine\"},{\"Id\":\"13\",\"Name\":\"Nephrology\"},{\"Id\":\"26\",\"Name\":\"Neurology\"},{\"Id\":\"14\",\"Name\":\"Neurosurgery\"},{\"Id\":\"15\",\"Name\":\"Obstetrics and Gynaecology\"},{\"Id\":\"29\",\"Name\":\"Oncology\"},{\"Id\":\"7\",\"Name\":\"Orthopedics\"},{\"Id\":\"36\",\"Name\":\"Pathology And Blood Bank\"},{\"Id\":\"16\",\"Name\":\"Pediatrics And Neonatology\"},{\"Id\":\"37\",\"Name\":\"Plastic And Reconstructive Surgery\"},{\"Id\":\"17\",\"Name\":\"Psychiatry\"},{\"Id\":\"25\",\"Name\":\"Pulmonology\"},{\"Id\":\"38\",\"Name\":\"Radiology\"},{\"Id\":\"24\",\"Name\":\"Rheumatology\"},{\"Id\":\"5\",\"Name\":\"Urology\"}]}";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_find_consultant);
        initList();
        ListView listView = (ListView) findViewById(R.id.listView1);
        SimpleAdapter simpleAdapter = new SimpleAdapter(this, specilitiesList, android.R.layout.simple_list_item_1, new String[] {"specilities"}, new int[] {android.R.id.text1});
        listView.setAdapter(simpleAdapter);
    }

    List<Map<String,String>> specilitiesList = new ArrayList<Map<String,String>>();
    private void initList(){

        try{
            JSONObject jsonResponse = new JSONObject(localjsonString);
            JSONArray jsonMainNode = jsonResponse.optJSONArray("specilities");

            for(int i = 0; i<jsonMainNode.length();i++){
                JSONObject jsonChildNode = jsonMainNode.getJSONObject(i);
                String name = jsonChildNode.optString("Id");
                String number = jsonChildNode.optString("Name");
                String outPut = name + ": " +number;
                specilitiesList.add(createEmployee("specilities", outPut));
            }
        }
        catch(JSONException e){
            Toast.makeText(getApplicationContext(), "Error"+e.toString(), Toast.LENGTH_SHORT).show();
        }
    }

    private HashMap<String, String>createEmployee(String name,String number){
        HashMap<String, String> employeeNameNo = new HashMap<String, String>();
        employeeNameNo.put(name, number);
        return employeeNameNo;
    }

}