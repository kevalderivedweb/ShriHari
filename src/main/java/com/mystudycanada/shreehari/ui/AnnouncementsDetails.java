package com.mystudycanada.shreehari.ui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.androidnetworking.AndroidNetworking;
import com.androidnetworking.common.Priority;
import com.androidnetworking.error.ANError;
import com.androidnetworking.interfaces.DownloadListener;
import com.androidnetworking.interfaces.DownloadProgressListener;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.mystudycanada.shreehari.API.GetAnnouncementDetailsRequest;
import com.mystudycanada.shreehari.Adapter.AttachmentAdapter;
import com.mystudycanada.shreehari.HomeActivity;
import com.mystudycanada.shreehari.Login_Activity;
import com.mystudycanada.shreehari.Model.AttachmentModel;
import com.mystudycanada.shreehari.R;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static android.bluetooth.BluetoothGattCharacteristic.PERMISSION_WRITE;


public class AnnouncementsDetails extends Fragment {
    // Store instance variables
    private String title;
    private int page;
    private RequestQueue requestQueue;
    private UserSession session;
    private String Announcement_Id;
    private ArrayList<AttachmentModel> attachmentArray = new ArrayList<>();
    private RecyclerView recyleview;
    private AttachmentAdapter mAdapter;
    private LinearLayout botom;
    private String mImageUrl = "";



    // Store instance variables based on arguments passed
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    // Inflate the view for the fragment based on layout XML
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);

        try {
            Announcement_Id = getArguments().getString("Announcement_Id");

        } catch (Exception e) {

        }


       File file = new File(getContext().getExternalFilesDir(null) + "/" + "Shree Hari Images");

        if (!file.exists()) {
            file.mkdir();
        }
        requestQueue = Volley.newRequestQueue(getActivity());//Creating the RequestQueue

        session = new UserSession(getActivity());
        GetAnnouncementDetails(Announcement_Id, view);
        //  checkPermission();


        botom = view.findViewById(R.id.botom);
        recyleview = view.findViewById(R.id.recyleview);
        recyleview.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        recyleview.setLayoutManager(gridLayoutManager);
        mAdapter = new AttachmentAdapter(attachmentArray, new AttachmentAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int item) {

                /*if (checkPermission()) {
                   new Downloading().execute(attachmentArray.get(item).getUrl());
                }*/


                Picasso.with(getActivity())
                        .load(attachmentArray.get(item).getUrl())
                        .into(new Target() {
                                  @Override
                                  public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                                      try {
                                          File file = new File(getContext().getExternalFilesDir(null) + "/" + "Shree Hari Images");

                                          if (!file.exists()) {
                                              file.mkdirs();
                                          }

                                          String name = new Date().toString() + ".jpg";
                                          file = new File(file, name);
                                          FileOutputStream out = new FileOutputStream(file);
                                          bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);

                                          out.flush();
                                          out.close();

                                          Toast.makeText(getActivity(), "Image Downloaded Successfully.", Toast.LENGTH_SHORT).show();
                                      } catch (Exception e) {
                                          // some action
                                      }
                                  }

                                  @Override
                                  public void onBitmapFailed(Drawable errorDrawable) {
                                  }

                                  @Override
                                  public void onPrepareLoad(Drawable placeHolderDrawable) {
                                  }
                              }
                        );


            }

        });
        recyleview.setAdapter(mAdapter);


        if (session.getUserType().equals("admin")) {
            botom.setVisibility(View.VISIBLE);
        } else {
            botom.setVisibility(View.GONE);
        }


        view.findViewById(R.id.attechment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //showPopup();
            }
        });

        view.findViewById(R.id.lnparent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AnnouncementsVIewStatus fragobj = new AnnouncementsVIewStatus();
                Bundle bundle = new Bundle();
                bundle.putString("Announcement_Id", Announcement_Id);
                bundle.putString("Announcement_Status", "1");
                fragobj.setArguments(bundle);
                replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
            }
        });

        view.findViewById(R.id.lnstudent).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AnnouncementsVIewStatus fragobj = new AnnouncementsVIewStatus();
                Bundle bundle = new Bundle();
                bundle.putString("Announcement_Id", Announcement_Id);
                bundle.putString("Announcement_Status", "2");
                fragobj.setArguments(bundle);
                replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
            }
        });

        view.findViewById(R.id.lnstaff).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AnnouncementsVIewStatus fragobj = new AnnouncementsVIewStatus();
                Bundle bundle = new Bundle();
                bundle.putString("Announcement_Id", Announcement_Id);
                bundle.putString("Announcement_Status", "3");
                fragobj.setArguments(bundle);
                replaceFragment(R.id.nav_host_fragment, fragobj, "Fragment", null);
            }
        });


        return view;
    }

    protected void replaceFragment(@IdRes int containerViewId,
                                   @NonNull Fragment fragment,
                                   @NonNull String fragmentTag,
                                   @Nullable String backStackStateName) {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(containerViewId, fragment, fragmentTag)
                .addToBackStack(backStackStateName)
                .commit();
    }


    private void GetAnnouncementDetails(String id, View view) {


        final KProgressHUD progressDialog = KProgressHUD.create(getActivity())
                .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                .setLabel("Please wait")
                .setCancellable(false)
                .setAnimationSpeed(2)
                .setDimAmount(0.5f)
                .show();

        GetAnnouncementDetailsRequest loginRequest = new GetAnnouncementDetailsRequest(id, new Response.Listener<String>() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onResponse(String response) {
                Log.e("Response", response + " null");
                progressDialog.dismiss();
                attachmentArray.clear();
                JSONObject jsonObject = null;
                try {
                    jsonObject = new JSONObject(response);

                    JSONObject jsonObject1 = jsonObject.getJSONObject("data");

                    TextView title = view.findViewById(R.id.title);
                    TextView send_by = view.findViewById(R.id.send_by);
                    TextView date = view.findViewById(R.id.date);
                    TextView description = view.findViewById(R.id.description);
                    TextView admin = view.findViewById(R.id.admin);
                    TextView student = view.findViewById(R.id.student);
                    TextView parent = view.findViewById(R.id.parent);
                    TextView letter = view.findViewById(R.id.letter);

                    Character character = jsonObject1.getString("title").charAt(0);
                    title.setText(jsonObject1.getString("title"));
                    letter.setText("" + character.toString().toUpperCase());
                    //description.setText(jsonObject1.getString("description"));

                    /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        description.setText(Html.fromHtml(jsonObject1.getString("description"), Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        description.setText(Html.fromHtml(jsonObject1.getString("description")));
                    }*/

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        description.setText(Html.fromHtml("<p>THIS ONE NORMAL</p>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<p><strong>THIS ONE IS BOLD</strong></p>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<p style=\"text-decoration: UNDERLINE;\"><u>THIS ONE UNDERLINE</u></p>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<p><em>THIS ONE ITALIC</em></p>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<h3>THIS ONE HEADING</h3>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<p style=\"color: #ff0000;\">THIS ONE COLOR</p>", Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        description.setText(Html.fromHtml("<p>THIS ONE NORMAL</p>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<p><strong>THIS ONE IS BOLD</strong></p>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<p style=\"text-decoration: UNDERLINE;\">THIS ONE UNDERLINE</p>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<p><em>THIS ONE ITALIC</em></p>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<h3>THIS ONE HEADING</h3>\n" +
                                "<p>RN</p>\n" +
                                "<p>RN</p>\n" +
                                "<p style=\"color: #ff0000;\">THIS ONE COLOR</p>"));
                    }
                    send_by.setText("Send By : " + jsonObject1.getString("send_by"));
                    date.setText(jsonObject1.getString("date"));
                    admin.setText(jsonObject1.getString("admin_read_count"));
                    student.setText(jsonObject1.getString("student_read_count"));
                    parent.setText(jsonObject1.getString("parent_read_count"));

                    JSONArray jsonArray = jsonObject1.getJSONArray("attachments");

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject object = jsonArray.getJSONObject(i);
                        AttachmentModel attachmentModel = new AttachmentModel();
                        attachmentModel.setId(String.valueOf(object.getInt("mobile_announcement_attachment_id")));
                        attachmentModel.setUrl(object.getString("attachment_url"));
                        attachmentArray.add(attachmentModel);
                    }
                    mAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                progressDialog.dismiss();
                session.logout();
                Intent intent = new Intent(getActivity(), Login_Activity.class);
                startActivity(intent);
                getActivity().finish();
                if (error instanceof ServerError)
                    Toast.makeText(getActivity(), "Server Error", Toast.LENGTH_SHORT).show();
                else if (error instanceof TimeoutError)
                    Toast.makeText(getActivity(), "Connection Timed Out", Toast.LENGTH_SHORT).show();
                else if (error instanceof NetworkError)
                    Toast.makeText(getActivity(), "Bad Network Connection", Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("Accept", "application/json");
                params.put("Authorization", "Bearer " + session.getAPIToken());
                return params;
            }
        };
        loginRequest.setTag("TAG");
        loginRequest.setShouldCache(false);

        requestQueue.add(loginRequest);

    }

    private void showPopup() {
        // custom dialog
        final Dialog dialog = new Dialog(getActivity());
        dialog.setContentView(R.layout.attach_dialog);
        dialog.setCancelable(true);
        dialog.setCanceledOnTouchOutside(true);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation; //style id
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        Window window = dialog.getWindow();
        window.setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);


        dialog.show();

    }


    public class Downloading extends AsyncTask<String, Integer, String> {
        KProgressHUD progressDialog;

        @Override
        public void onPreExecute() {
            super.onPreExecute();
            progressDialog = KProgressHUD.create(getActivity())
                    .setStyle(KProgressHUD.Style.SPIN_INDETERMINATE)
                    .setLabel("Please wait")
                    .setCancellable(false)
                    .setAnimationSpeed(2)
                    .setDimAmount(0.5f)
                    .show();

        }

        @Override
        protected String doInBackground(String... url) {
            File mydir = new File(Environment.getExternalStorageDirectory() + "/ShreeHari");
            if (!mydir.exists()) {
                mydir.mkdirs();
            }

            DownloadManager manager = (DownloadManager) getActivity().getSystemService(Context.DOWNLOAD_SERVICE);
            Uri downloadUri = Uri.parse(url[0]);
            DownloadManager.Request request = new DownloadManager.Request(downloadUri);

            SimpleDateFormat dateFormat = new SimpleDateFormat("mmddyyyyhhmmss");
            String date = dateFormat.format(new Date());

            request.setAllowedNetworkTypes(
                    DownloadManager.Request.NETWORK_WIFI | DownloadManager.Request.NETWORK_MOBILE)
                    .setAllowedOverRoaming(false)
                    .setTitle("Downloading")
                    .setDestinationInExternalPublicDir("/ShreeHari", date + ".jpg");

            manager.enqueue(request);
            return mydir.getAbsolutePath() + File.separator + date + ".jpg";
        }

        @Override
        public void onPostExecute(String s) {
            super.onPostExecute(s);
            progressDialog.dismiss();
            Toast.makeText(getActivity(), "Image Saved... " + s, Toast.LENGTH_SHORT).show();
        }
    }


    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public boolean checkPermission() {
        int currentAPIVersion = Build.VERSION.SDK_INT;
        if (currentAPIVersion >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
                    alertBuilder.setCancelable(true);
                    alertBuilder.setTitle("Permission necessary");
                    alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
                    alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                        }
                    });
                    AlertDialog alert = alertBuilder.create();
                    alert.show();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
                return false;
            } else {
                return true;
            }
        } else {
            return true;
        }
    }

    public static final int MY_PERMISSIONS_REQUEST_WRITE_STORAGE = 123;
    boolean result;
    //Here you can check App Permission

    public void checkAgain() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(), Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getActivity());
            alertBuilder.setCancelable(true);
            alertBuilder.setTitle("Permission necessary");
            alertBuilder.setMessage("Write Storage permission is necessary to Download Images and Videos!!!");
            alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
                public void onClick(DialogInterface dialog, int which) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
                }
            });
            AlertDialog alert = alertBuilder.create();
            alert.show();
        } else {
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, MY_PERMISSIONS_REQUEST_WRITE_STORAGE);
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[]
            permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_WRITE_STORAGE:
                //code for deny
                checkAgain();
                break;
            default:
                break;
        }
    }

    private String saveImage(Bitmap image) {
        String savedImagePath = null;

        String imageFileName = "JPEG_" + System.currentTimeMillis() + ".jpg";

        File storageDir = new File(getContext().getExternalFilesDir(null) + "/" + "Shree Hari Images");

        boolean success = true;
        if (!storageDir.exists()) {
            success = storageDir.mkdirs();
        }
        if (success) {
            File imageFile = new File(storageDir, imageFileName);
            savedImagePath = imageFile.getAbsolutePath();
            try {
                OutputStream fOut = new FileOutputStream(imageFile);
                image.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
                fOut.close();
            } catch (Exception e) {
                e.printStackTrace();
            }

            // Add the image to the system gallery
            galleryAddPic(savedImagePath);
            Toast.makeText(getActivity(), "IMAGE SAVED", Toast.LENGTH_LONG).show();
        }
        return savedImagePath;
    }

    private void galleryAddPic(String imagePath) {
        Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
        File f = new File(imagePath);
        Uri contentUri = Uri.fromFile(f);
        mediaScanIntent.setData(contentUri);
        getActivity().sendBroadcast(mediaScanIntent);
    }


    // Function to check and request permission


}