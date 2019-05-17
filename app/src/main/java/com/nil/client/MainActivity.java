package com.nil.client;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.nil.client.config.InternetAddress;
import com.nil.client.model.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText userNameEditText;
    private TextView userNameTakenTextView;
    private RadioGroup sexRadioGroup;
    private RadioButton sexRadioButton;
    private Button chatButton;
    private RequestQueue requestQueue;

    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            String userNameString = userNameEditText.getText().toString().trim();
            if (userNameString.isEmpty()) {
                chatButton.setBackgroundResource(R.drawable.rounded_button_grey);
                chatButton.setEnabled(false);
            } else {
                chatButton.setBackgroundResource(R.drawable.rounded_button_blue);
                chatButton.setEnabled(true);
            }
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //find views
        userNameEditText = findViewById(R.id.userNameEditText);
        userNameTakenTextView = findViewById(R.id.userNameTakenTextView);
        sexRadioGroup = findViewById(R.id.sexRadioGroup);
        chatButton = findViewById(R.id.chatButton);
        //find views ends

        //initialize request queue
        requestQueue = Volley.newRequestQueue(this);
        //initialize request queue ends

        userNameEditText.addTextChangedListener(textWatcher);
        chatButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                attempChat();
            }
        });

    }

    /**
     * start tab activity and pass the user object
     */
    private void startTabActivity(String id) {
        int selectedId = sexRadioGroup.getCheckedRadioButtonId();
        sexRadioButton = findViewById(selectedId);
        User user = new User();
        user.setId(id);
        user.setUsername(userNameEditText.getText().toString().trim());
        user.setGender(sexRadioButton.getText().toString());
        user.setStatus("0");
        user.setImageURL("null");

        Intent intent = new Intent(getApplicationContext(), TabActivity.class);
        intent.putExtra("CurrentUserKey", user);
        startActivity(intent);
    }

    /**
     * add user to database without setting its password
     * <p>
     * if user name not taken, start tab activity
     */

    private void attempChat() {
        String url = InternetAddress.volleyUserPostAddress;
        int selectedId = sexRadioGroup.getCheckedRadioButtonId();
        sexRadioButton = findViewById(selectedId);

        JSONObject object = new JSONObject();
        try {
            object.put("userName", userNameEditText.getText().toString().trim());
            object.put("gender", sexRadioButton.getText().toString());
        } catch (JSONException e) {
            e.printStackTrace();
            Toast.makeText(getApplicationContext(),
                    "Error: " + e.getMessage(),
                    Toast.LENGTH_LONG).show();
        }

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url, object, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    if (response.getString("userName").equals("userName Taken")) {
                        userNameTakenTextView.setVisibility(View.VISIBLE);
                    } else {
                        userNameTakenTextView.setVisibility(View.INVISIBLE);
                        startTabActivity(response.getString("id"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error + "", Toast.LENGTH_SHORT).show();
            }
        }) {

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> headers = new HashMap<>();
                headers.put("Content-Type", "application/json; charset = utf-8");
                return headers;
            }
        };
        requestQueue.add(jsonObjectRequest);
    }
}

