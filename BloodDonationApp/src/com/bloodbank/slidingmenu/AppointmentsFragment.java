package com.bloodbank.slidingmenu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.Request.Method;
import com.android.volley.toolbox.StringRequest;
import com.example.ldialogbox.CustomDialog;


import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AdapterView.OnItemClickListener;

public class AppointmentsFragment extends Fragment{
	public String mid,requid,donoruid,donphone,reqphone,reqname,donname,requname,bloodgroup,meetingdate,meetingtime,meetingplace,status;
	public String changeStatusMID, changeStatusSTATUS;
	private ProgressDialog pDialog;
	   ListView listView1;
	   private SQLiteHandler db=null;
	   Fragment fragment = null;
	   HashMap<String, String> hms;
	   JSONParser jParser = new JSONParser();
	 
	    //ArrayList<HashMap<String, String>> appointmentsList;
	    
	    JSONArray appointments = null;
	    
	    private static final String TAG_SUCCESS = "success";
	    
	 
List<AppointmentsItem> appointmentslist = new ArrayList<AppointmentsItem>();
	
	
	public AppointmentsFragment(){}
	
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        View rootView = inflater.inflate(R.layout.fragment_appointment, container, false);
        
        //getActivity().getActionBar().setTitle("Appointments");
    	//MainActivity.changeDrawerItem(2);
    	
    	new LoadAllappointments().execute();
        
        listView1 = (ListView)rootView.findViewById(R.id.listappointments);
        
        
                listView1.setOnItemClickListener(new OnItemClickListener() {
                	  public void onItemClick(AdapterView<?> parent, View view,
                	    int position, long id) {
                		  showCustomDialog(appointmentslist,position,AppointmentsFragment.this.getActivity().getApplicationContext());
                	  }
                	}); 

                return rootView;
    }


	class LoadAllappointments extends AsyncTask<String, String, String> {
	 
    /**
     * Before starting background thread Show Progress Dialog
     * */
    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        pDialog = new ProgressDialog(getActivity());
        pDialog.setMessage("Loading Appointments. Please wait...");
        pDialog.setIndeterminate(false);
        pDialog.setCancelable(false);
        pDialog.show();
       }

    /**
     * getting All appointments from url
     * */
    protected String doInBackground(String... args) {
        // Building Parameters
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        // getting JSON string from URL
        Log.d("ip: ", AppConfig.url_all_appointments);
        
        db = new SQLiteHandler(getActivity().getApplicationContext());
        db.getReadableDatabase();
        hms=new HashMap<String, String>();
        hms = db.getUserDetails();
        
        String id = hms.get("id");
        
        params.add(new BasicNameValuePair("allappointments", "al"));
        params.add(new BasicNameValuePair("id", id));
        
        
        JSONObject json = jParser.makeHttpRequest(AppConfig.url_all_appointments, "POST", params);

        // Check your log cat for JSON reponse
        Log.d("All appointments: ", json.toString());
        
        try {
            // Checking for SUCCESS TAG
            int success = json.getInt(TAG_SUCCESS);

            if (success == 1) {
                // appointments found
                // Getting Array of appointments
                appointments = json.getJSONArray("meetings");

                // looping through All appointments
                for (int i = 0; i < appointments.length(); i++) 
                {
                    JSONObject c = appointments.getJSONObject(i);

                    // Storing each json item in variable
                    mid = c.getString("mid");
                    requid = c.getString("reqUID");
                    donoruid = c.getString("donorUID");
                    donphone = c.getString("donPhone");
                    reqphone = c.getString("reqPhone");
                    reqname = c.getString("reqNAME");
                    donname = c.getString("donName");
                    requname = c.getString("reqUNAME");
                    bloodgroup = c.getString("bloodGroup");
                    meetingdate = c.getString("meetingDate");
                    meetingtime = c.getString("meetingTime");
                    meetingplace = c.getString("meetingPlace");
                    status = c.getString("status");
                    
                    // creating new HashMap
                    appointmentslist.add(new AppointmentsItem(mid,requid,donoruid,donphone,reqphone
                    		,reqname,donname,requname,bloodgroup,meetingdate,meetingtime,meetingplace,status));
                    /*
                    HashMap<String, String> map = new HashMap<String, String>();

                    // adding each child node to HashMap key => value
                    map.put("name", name);
                    map.put("state", state);
                    map.put("contact", contact);
                    // adding HashList to ArrayList
                    appointmentsList.add(map);
                    */
                    
                }
            } 
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null;
    }

    /**
     * After completing background task Dismiss the progress dialog
     * **/
    protected void onPostExecute(String file_url) {
        
    	pDialog.dismiss();
        // updating UI from Background Thread
    	getActivity().runOnUiThread(new Runnable(){
            public void run() {
                /**
                 * Updating parsed JSON data into ListView
                 * */
            	
            	 ListAdapterAppointments adapter = new ListAdapterAppointments(getActivity(),
                         R.layout.appoint_item_row, appointmentslist);
                 listView1.setAdapter(adapter);
                 
            }
        });

    }

}

	
	protected void showCustomDialog(final List<AppointmentsItem> appointmentslist,final int position,Context cnxt) {
        // TODO Auto-generated method stub
        
		String statusText = null;
		
		if((appointmentslist.get(position).status).equalsIgnoreCase("finalised"))
			statusText = "Cancel Appointment";
		if((appointmentslist.get(position).status).equalsIgnoreCase("Cancel Appointment"))
			statusText = "Confirm";
		
		final String currentStatus = statusText;
		
		CustomDialog.Builder builder = new CustomDialog.Builder(getActivity(),"Details of Appointment", statusText);
	    builder.negativeText("Go back");
	    builder.darkTheme(true);
	    

	    View vm = getActivity().getLayoutInflater().inflate(R.layout.custom_dialog, null);
        
	    TextView tv1 = (TextView)vm.findViewById(R.id.dialog_tv1);
        TextView tv2 = (TextView)vm.findViewById(R.id.dialog_tv2);
        
        
        tv1.setText(appointmentslist.get(position).donname+" donates to "+appointmentslist.get(position).reqname);
        tv2.setText(appointmentslist.get(position).status);
        
        changeStatusMID = appointmentslist.get(position).mid;
        changeStatusSTATUS = currentStatus;
	    
	    builder.negativeColor("#00CCFF");
        builder.positiveColor("#00CCFF");
        builder.titleColor("#00CCFF");
        
	    final CustomDialog customDialog = builder.build();
	   
	    customDialog.setCustomView(vm);
    

        PhoneCallListener phoneListener = new PhoneCallListener(cnxt);
		TelephonyManager telephonyManager = (TelephonyManager) this
			.getActivity().getSystemService(Context.TELEPHONY_SERVICE);
		telephonyManager.listen(phoneListener,PhoneStateListener.LISTEN_CALL_STATE);
 
		customDialog.setClickListener(new CustomDialog.ClickListener() {
            @Override
            public void onConfirmClick() {
            	//status modify
            	new modifyStatus().execute();
            	
            	
            	
            	
     
        
            	customDialog.dismiss();
       
			
            }

            @Override
            public void onCancelClick() {
                customDialog.dismiss();
            }
        });
		
		
        customDialog.show();
    }
	class modifyStatus extends AsyncTask<String, String, String> {
		 
		JSONParser jsonParser = new JSONParser();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
        	
           
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("changestatus", "re"));
            params.add(new BasicNameValuePair("mid", changeStatusMID));
            params.add(new BasicNameValuePair("status", changeStatusSTATUS));
 
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_STATUS,
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                	getActivity().runOnUiThread(new Runnable() {              
                	    @Override
                	    public void run() {                         
                	    	new deleteMeeting().execute();
                	    	Toast.makeText(getActivity().getApplicationContext(),
                                    "Status changed", Toast.LENGTH_LONG)
                                    .show();                	    }
                	});
                	 
                	Log.d("Success","Status changed");
                    
                } else {
                	getActivity().runOnUiThread(new Runnable() {              
                	    @Override
                	    public void run() {                         
                	    	 Toast.makeText(getActivity().getApplicationContext(),
                                     "Status Can't be changed ", Toast.LENGTH_LONG)
                                     .show();
                        	Log.e("Error","Status change error");                	    }
                	});
                	
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            
        }
 
    }


	class deleteMeeting extends AsyncTask<String, String, String> {
		 
		JSONParser jsonParser = new JSONParser();
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
 
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
        	
           
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("delete", "delete"));
            params.add(new BasicNameValuePair("mid", changeStatusMID));
            
 
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(AppConfig.URL_DELETE,
                    "POST", params);
 
            // check log cat fro response
            Log.d("Create Response", json.toString());
 
            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);
 
                if (success == 1) {
                	getActivity().runOnUiThread(new Runnable() {              
                	    @Override
                	    public void run() {           
                	    	fragment = new AppointmentsFragment();
                            
                            if (fragment != null) {
                    			FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                    			fragmentManager.beginTransaction()
                    					.replace(R.id.container_body, fragment).addToBackStack(null).commit();

                    			
                    		} else {
                    			// error in creating fragment
                    			Log.e("MainActivity", "Error in creating fragment");
                    		}
                        
                	    	Toast.makeText(getActivity().getApplicationContext(),
                                    "Deleted", Toast.LENGTH_LONG)
                                    .show();                	    }
                	});
                	 
                	Log.d("Success","Deleted");
                    
                } else {
                	getActivity().runOnUiThread(new Runnable() {              
                	    @Override
                	    public void run() {                         
                	    	 Toast.makeText(getActivity().getApplicationContext(),
                                     "Deletion not possible ", Toast.LENGTH_LONG)
                                     .show();
                        	Log.e("Error","Status change error");                	    }
                	});
                	
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
 
            return null;
        }
 
        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            
        }
 
    }


class PhoneCallListener extends PhoneStateListener {
	 
	private boolean isPhoneCalling = false;

	String LOG_TAG = "LOGGING 123";
	
	private Context context;
	
	public PhoneCallListener(Context context)
	{
		this.context=context;
	}

	@Override
	public void onCallStateChanged(int state, String incomingNumber) {

		if (TelephonyManager.CALL_STATE_RINGING == state) {
			// phone ringing
			Log.i(LOG_TAG, "RINGING, number: " + incomingNumber);
		}

		if (TelephonyManager.CALL_STATE_OFFHOOK == state) {
			// active
			Log.i(LOG_TAG, "OFFHOOK");

			isPhoneCalling = true;
		}

		if (TelephonyManager.CALL_STATE_IDLE == state) {
			
			Log.i(LOG_TAG, "IDLE");

			if (isPhoneCalling) {

				Log.i(LOG_TAG, "restart app");
				
				isPhoneCalling = false;
			}

		}
	}
}

}
	