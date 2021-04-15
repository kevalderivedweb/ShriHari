package com.mystudycanada.shreehari.API;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by kshitij on 12/17/17.
 */

public class CheckEmailRequest extends StringRequest {

    private Map<String, String> parameters;

    public CheckEmailRequest(String email_mobile_no,String device_token, Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.POST, ServerUtils.BASE_URL+"check-email-or-mobile", listener, errorListener);
        parameters = new HashMap<>();
        parameters.put("email_mobile_no", email_mobile_no);
        parameters.put("device_type", "android");
        parameters.put("device_token", device_token);

    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return parameters;
    }
}
