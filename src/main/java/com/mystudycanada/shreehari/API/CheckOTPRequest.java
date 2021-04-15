package com.mystudycanada.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class CheckOTPRequest extends StringRequest {

    private Map<String, String> parameters;

    public CheckOTPRequest(String otp, String device_token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"check-otp", listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("otp", otp);
        parameters.put("device_type", "android");
        parameters.put("device_token", device_token);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
