package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class GetAnnouncementRequestFilter extends StringRequest {

    private Map<String, String> parameters;

    public GetAnnouncementRequestFilter(String type, String start_date, String end_date,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, ServerUtils.BASE_URL+"get-announcement?start_date="+start_date+"&end_date="+end_date+"&group="+type, listener, null);
        parameters = new HashMap<>();

    }




    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
