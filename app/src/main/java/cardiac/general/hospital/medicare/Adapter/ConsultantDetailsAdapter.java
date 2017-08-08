package cardiac.general.hospital.medicare.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import cardiac.general.hospital.medicare.R;

public class ConsultantDetailsAdapter extends ArrayAdapter<String> {
    public Context context;
    public String[] DocNameArray;
    public String[] DocDeptArray;
    public String[] DocSpecialityArray;
    public String[] DocDays1Array;
    public String[] DocTiming1Array;
    public String[] DocDays2Array;
    public String[] DocTiming2Array;

    public ConsultantDetailsAdapter(Context context, String[] DocName, String[] DocDept, String[] DocSpeciality, String[] DocDays1, String[] DocTiming1, String[] DocDays2, String[] DocTiming2) {
        super(context, R.layout.consultant_details_adapter,R.id.docnameText,DocName);
        this.context=context;
        this.DocNameArray=DocName;
        this.DocDeptArray=DocDept;
        this.DocSpecialityArray=DocSpeciality;
        this.DocDays1Array=DocDays1;
        this.DocTiming1Array=DocTiming1;
        this.DocDays2Array=DocDays2;
        this.DocTiming2Array=DocTiming2;
    }
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater=(LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View row=inflater.inflate(R.layout.consultant_details_adapter,parent,false);
        TextView DocName= (TextView) row.findViewById(R.id.docnameText);
        TextView DocDept= (TextView) row.findViewById(R.id.docdptText);
        TextView DocSpeciality= (TextView) row.findViewById(R.id.docspecText);
        TextView DocDays1= (TextView) row.findViewById(R.id.docday1Text);
        TextView DocTime1= (TextView) row.findViewById(R.id.doctime1Text);
        TextView DocDays2= (TextView) row.findViewById(R.id.docday2Text);
        TextView DocTime2= (TextView) row.findViewById(R.id.doctime2Text);
        DocName.setText(DocNameArray[position]);
        DocDept.setText("Department "+DocDeptArray[position]);
        DocSpeciality.setText("Speciality "+DocSpecialityArray[position]);
        DocDays1.setText("Schedule1 "+DocDays1Array[position]);
        DocTime1.setText("timing1  "+DocTiming1Array[position]);
        DocDays2.setText("Schedule2 "+DocDays2Array[position]);
        DocTime2.setText("timing2  "+DocTiming2Array[position]);
        return row;
    }
}