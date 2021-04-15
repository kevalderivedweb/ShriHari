package com.mystudycanada.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class GetPeriodRequest extends StringRequest {

    private Map<String, String> parameters;

    public GetPeriodRequest(String standard,String batch,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, ServerUtils.BASE_URL+"get-period?standard_id="+batch+"&batch_id="+standard, listener, errorListener);
        parameters = new HashMap<>();

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
