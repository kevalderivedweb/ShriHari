package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class GetAnnouncementStatusRequest extends StringRequest {

    private Map<String, String> parameters;

    public GetAnnouncementStatusRequest(String id, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, ServerUtils.BASE_URL+"announcement-view-status?mobile_announcement_id="+id, listener, null);
        parameters = new HashMap<>();

    }




    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
