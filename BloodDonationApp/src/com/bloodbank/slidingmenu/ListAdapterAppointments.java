package com.bloodbank.slidingmenu;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class ListAdapterAppointments extends ArrayAdapter<AppointmentsItem> {
	private final Context context;
	  private final List<AppointmentsItem> items;
	  private ProgressDialog pDialog;

		String name,email,gender,bgroup,phone,country,state,
    	city,address,candonate,description,weight,age,latitude,
    	longitude,deviceid,createdDate,modifiedDate;
		String p;
	  Integer id;
	  
//    public ListAdapterAppointments(Context context, int textViewResourceId) {
//        super(context, textViewResourceId);
//    }

    public ListAdapterAppointments(Context context, int resource, List<AppointmentsItem> items) {
        super(context, resource, items);
        this.context=context;
        this.items=items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

    	 LayoutInflater inflater = (LayoutInflater) getContext()
                 .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
             View rowView = inflater.inflate(R.layout.appoint_item_row, parent, false);
     
           
             TextView tt1 = (TextView) rowView.findViewById(R.id.textTitleAppoint1);
             TextView tt2 = (TextView) rowView.findViewById(R.id.textTitleAppoint2);
             TextView tt3 = (TextView) rowView.findViewById(R.id.textTitleAppoint3);
             
             
             tt1.setText(("Requested By "+(String) items.get(position).requname+" from "+(String) items.get(position).donname+" for "+(String) items.get(position).reqname));
             tt2.setText("Meeting at "+ (String) items.get(position).meetingplace+" on "+
             (String) items.get(position).meetingdate + " "+ (String) items.get(position).meetingtime);
             tt3.setText((String) items.get(position).status);
             

        return rowView;
    }
    
}