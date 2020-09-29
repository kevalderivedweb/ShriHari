package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class GetStudentAttandanceRequest extends StringRequest {

    private Map<String, String> parameters;

    public GetStudentAttandanceRequest(String month, String year, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, "http://teqcoder.com/shreeharicrm/api/api/get-attendance-calendar?month="+month+"&year="+year, listener, null);
        parameters = new HashMap<>();

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
