package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class AddLeaveRequest extends StringRequest {

    private Map<String, String> parameters;

    public AddLeaveRequest(String mock_test_date, String mock_test_time, String speaking_topic, String essay, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"add-leave", listener, null);
        parameters = new HashMap<>();

        parameters.put("break_from", mock_test_date);
        parameters.put("break_to", mock_test_time);
        parameters.put("no_of_days", speaking_topic);
        parameters.put("remarks", essay);


    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
