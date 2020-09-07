package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class AddAttandanceRequest extends StringRequest {

    private Map<String, String> parameters;

    public AddAttandanceRequest(String date,
                                ArrayList<String> stringArrayList,
                                Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, ServerUtils.BASE_URL+"add-attendance", listener, null);
        parameters = new HashMap<>();
        parameters.put("date", date);

        for (int i = 0 ; i < stringArrayList.size() ; i++) {
            parameters.put("students[]", stringArrayList.get(i));
        }



    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
