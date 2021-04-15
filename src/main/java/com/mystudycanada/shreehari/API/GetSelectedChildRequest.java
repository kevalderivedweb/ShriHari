package com.mystudycanada.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class GetSelectedChildRequest extends StringRequest {

    private Map<String, String> parameters;

    public GetSelectedChildRequest(String mobile_user_master_id,Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"select-default-child", listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("mobile_user_master_id", mobile_user_master_id);
    }


    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }

}
