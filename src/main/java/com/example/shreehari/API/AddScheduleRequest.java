package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class AddScheduleRequest extends StringRequest {

    private Map<String, String> parameters;

    public AddScheduleRequest(String subject_id,
            String standard_id,
            String batch_id,
            String type,
            String date,
            String start_time,
            String end_time,
            String branch_id,
            String class_room,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"add-schedule", listener, null);
        parameters = new HashMap<>();
        parameters.put("subject_id", subject_id);
        parameters.put("standard_id", standard_id);
        parameters.put("batch_id", batch_id);
        parameters.put("type", type);
        parameters.put("date", date);
        parameters.put("start_time", start_time);
        parameters.put("end_time", end_time);
        parameters.put("branch_id", branch_id);
        parameters.put("class_room", class_room);

    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
