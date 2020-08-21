package com.example.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class SetMPINRequest extends StringRequest {

    private Map<String, String> parameters;

    public SetMPINRequest(String m_pin, String device_token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"set-mpin", listener, null);
        parameters = new HashMap<>();
        parameters.put("m_pin", m_pin);
        parameters.put("device_type", "android");
        parameters.put("device_token", device_token);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
