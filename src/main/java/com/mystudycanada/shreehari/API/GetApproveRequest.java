package com.mystudycanada.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class GetApproveRequest extends StringRequest {

    private Map<String, String> parameters;

    public GetApproveRequest(String coachingbreak_id,String leave_status_id,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"leave-approve-reject", listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("coachingbreak_id", coachingbreak_id);
        parameters.put("leave_status_id", leave_status_id);


    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
