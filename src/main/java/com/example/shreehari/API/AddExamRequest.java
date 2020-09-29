package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class AddExamRequest extends StringRequest {

    private Map<String, String> parameters;

    public AddExamRequest(String mock_test_name, String mock_test_date,String mock_test_time,String speaking_topic,String essay,String branch_id,String coaching_id,String batch_id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"add-exam", listener, null);
        parameters = new HashMap<>();
        parameters.put("mock_test_name", mock_test_name);
        parameters.put("mock_test_date", mock_test_date);
        parameters.put("mock_test_time", mock_test_time);
        parameters.put("speaking_topic", speaking_topic);
        parameters.put("essay", essay);
        parameters.put("branch_id", branch_id);
        parameters.put("standard_id", coaching_id);
        parameters.put("batch_id", batch_id);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
