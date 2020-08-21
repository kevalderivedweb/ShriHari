package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class CheckMPINlRequest extends StringRequest {

    private Map<String, String> parameters;

    public CheckMPINlRequest(String device_token,String mpin, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"check-mpin", listener, null);
        parameters = new HashMap<>();
        parameters.put("device_type", "android");
        parameters.put("m_pin", mpin);
        parameters.put("device_token", device_token);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
