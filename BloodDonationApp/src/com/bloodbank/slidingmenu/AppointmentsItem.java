package com.bloodbank.slidingmenu;

public class AppointmentsItem {

    public String mid;
    public String requid;
    public String donoruid;
    public String donphone;
    public String reqphone;
    public String reqname;
    public String donname;
    public String requname;
    public String bloodgroup;
    public String meetingdate;
    public String meetingtime;
    public String meetingplace;
    public String status;
    
    public AppointmentsItem(){
        super();
    }
   
    public AppointmentsItem(String mid, String requid, String donoruid,String donphone,String reqphone
    		,String reqname, String donname, String requname, String bloodgroup,String meetingdate,String meetingtime ,
    		String meetingplace,String status) 
    {
        super();
        this.mid = mid;
        this.requid = requid;
        this.donoruid = donoruid;
        this.donphone = donphone;
        this.reqphone = reqphone;
        this.reqname = reqname;
        this.donname = donname;
        this.requname = requname;
        this.bloodgroup = bloodgroup;
        this.meetingdate = meetingdate;
        this.meetingtime = meetingtime;
        this.meetingplace = meetingplace;
        this.status = status;
        
    }
    public AppointmentsItem(String mid, String requid, String donoruid)
    {
    	super();
        this.mid = mid;
        this.requid = requid;
        this.donoruid = donoruid;
    }
}

