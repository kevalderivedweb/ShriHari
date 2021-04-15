package com.mystudycanada.shreehari;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.MimeTypeMap;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;
import com.kaopiz.kprogresshud.KProgressHUD;
import com.mystudycanada.shreehari.API.CheckEmailRequest;
import com.mystudycanada.shreehari.API.ServerUtils;
import com.mystudycanada.shreehari.UserSession.UserSession;
import com.mystudycanada.shreehari.ui.DownloadImages;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class PhotoDetailsActivity extends AppCompatActivity {


    private List mPhotoFiles;
    private static final String TAG = "PhotoDetailActivity";
    private int mInitialPosition = 0;
    private ViewPager mPager;
    private ListImageAdapter mAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_my_photo);


        findViewById(R.id.image_back_gallery).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        }); findViewById(R.id.image_delete_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoFiles != null && mPhotoFiles.size() > 0) {
                    deletePhoto(getCurrentPhoto());
                }
            }
        });findViewById(R.id.image_set_wallpaper).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setWallpaper(getCurrentPhoto());

            }
        });findViewById(R.id.image_share_photo).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mPhotoFiles != null && mPhotoFiles.size() > 0) {
                    shareImage(getCurrentPhoto());
                }
            }
        });
        mPager = findViewById(R.id.pager_photo);
        Bundle bundle = getIntent().getExtras();
        String imageSelectedPath = bundle.getString(ServerUtils.BUNDLE_KEY_IMAGE_SELECTED);
        this.mPhotoFiles = (List) bundle.getSerializable(ServerUtils.BUNDLE_KEY_LIST_PHOTO);
        if (this.mPhotoFiles != null) {
            Log.d(TAG, "INITIAL POSITION: " + this.mInitialPosition);
            int size = this.mPhotoFiles.size();
            for (int i = 0; i < size; i++) {
                if (((File) this.mPhotoFiles.get(i)).getAbsolutePath().equals(imageSelectedPath)) {
                    this.mInitialPosition = i;
                    Log.d(TAG, "INITIAL POSITION:" + this.mInitialPosition);
                    break;
                }
            }
            showListPhoto();
        }




    }


    public static int getCurrentSdkVersion() {
        return Build.VERSION.SDK_INT;
    }

    private File getCurrentPhoto() {
        return ((ImageFragment) ((ListImageAdapter) this.mPager.getAdapter()).getItem(this.mPager.getCurrentItem())).getImageSource();
    }
    public static boolean isLocaleVn() {
        return getCurrentLocale().equalsIgnoreCase("vi");
    }

    public static String getCurrentLocale() {
        return Locale.getDefault().getLanguage();
    }

    public static final String MESSAGE_SHARE_VI = "Ảnh được tạo bởi: ";
    public static final String MESSAGE_SHARE = "Photo create by: ";

    private void shareImage(File file) {
        String msg = "https://play.google.com/store/apps/details?id=" + getPackageName();
        shareImageAndText(this, file.getAbsolutePath(), getString(R.string.app_name), isLocaleVn() ? MESSAGE_SHARE_VI + msg : MESSAGE_SHARE + msg);
    }

    public void showDetailApp(Context mActivity, String package_name) {
        try {
            mActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("market://details?id=" + package_name)));
        } catch (Exception e) {
            mActivity.startActivity(new Intent("android.intent.action.VIEW", Uri.parse("https://play.google.com/store/apps/details?id=" + package_name)));
        }
    }
    public void shareImageAndText(Context mContext, String pathFile, String SUBJECT, String TEXT) {
        try {
            File myFile = new File(pathFile);
            String type = MimeTypeMap.getSingleton().getMimeTypeFromExtension(myFile.getName().substring(myFile.getName().lastIndexOf(".") + 1));
            Intent sharingIntent = new Intent();
            sharingIntent.setAction("android.intent.action.SEND");
            sharingIntent.setType(type);
            sharingIntent.putExtra("android.intent.extra.STREAM", Uri.fromFile(myFile));
            sharingIntent.putExtra("android.intent.extra.SUBJECT", SUBJECT);
            sharingIntent.putExtra("android.intent.extra.TEXT", TEXT);
            if (mContext.getPackageManager().queryIntentActivities(sharingIntent, 65536).size() > 0) {
                mContext.startActivity(Intent.createChooser(sharingIntent, mContext.getResources().getString(R.string.title_share_file)));
            } else {
                showDetailApp(mContext, mContext.getPackageName());
            }
        } catch (Exception e) {
           // L.e("", "shareImageAndText error = " + e.toString());
        }
    }

    public static final String MIME_TYPE_IMAGE = "image/*";

    private void setWallpaper(File photo) {
        if (getCurrentSdkVersion() >= 24) {
            try {
                StrictMode.class.getMethod("disableDeathOnFileUriExposure", new Class[0]).invoke(null, new Object[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Intent intent = new Intent("android.intent.action.ATTACH_DATA");
        intent.setDataAndType(Uri.fromFile(photo), MIME_TYPE_IMAGE);
        intent.putExtra("jpg", MIME_TYPE_IMAGE);
        startActivityForResult(Intent.createChooser(intent, getString(R.string.text_set_wallpaper)), 1);
    }

    public static void scanFile(Context contexts, String pathFile) {
        scanFile(contexts, new String[]{pathFile});
    }

    public static void scanFile(Context contexts, String[] paths) {
        MediaScannerConnection.scanFile(contexts, paths, null, new MediaScannerConnection.OnScanCompletedListener() {
            public void onScanCompleted(String path, Uri uri) {
              //  L.d("SCAN MEDIA FILE", "SCAN MEDIA COMPLETED: " + path);
            }
        });
    }
    private void deletePhoto(final File file) {
        if (file != null && file.exists()) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setCancelable(true);
            builder.setMessage((int) R.string.message_confirm_delete_photo);
            builder.setPositiveButton((int) R.string.text_button_yes, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    if (file.delete()) {
                        scanFile(PhotoDetailsActivity.this, file.getAbsolutePath());
                        Toast.makeText(PhotoDetailsActivity.this,"Photo Delete successfully",Toast.LENGTH_LONG).show();
                        PhotoDetailsActivity.this.mAdapter.removePosition(mPager.getCurrentItem());
                        DownloadImages.FILE_CHANGED = true;
                        mInitialPosition = 0;
                        showListPhoto();
                    }
                    dialog.dismiss();
                }
            });
            builder.setNegativeButton((int) R.string.text_button_no, null);
            builder.create().show();
        }
    }
    private class ListImageAdapter extends FragmentStatePagerAdapter {
        ListImageAdapter(FragmentManager fm) {
            super(fm);
        }

        void removePosition(int position) {
            mPhotoFiles.remove(position);
            notifyDataSetChanged();
        }

        public int getItemPosition(Object object) {
            return -2;
        }

        public Fragment getItem(int position) {
            return ImageFragment.newInstance((File) mPhotoFiles.get(position));
        }

        public int getCount() {
            return mPhotoFiles.size();
        }
    }

    public static class ImageFragment extends Fragment {
        private ImageView mImage;
        private File mImageFile;

        public static ImageFragment newInstance(File image) {
            ImageFragment imageFragment = new ImageFragment();
            imageFragment.init(image);
            return imageFragment;
        }

        public void init(File imageFile) {
            this.mImageFile = imageFile;
        }

        public File getImageSource() {
            return this.mImageFile;
        }

        @Nullable
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            this.mImage = new ImageView(getContext());
            this.mImage.setLayoutParams(new ViewGroup.LayoutParams(-1, -1));
            this.mImage.setScaleType(ImageView.ScaleType.FIT_XY);
            return this.mImage;
        }

        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            if (this.mImageFile != null) {
                Glide.with((Fragment) this).load(this.mImageFile.getAbsolutePath()).placeholder((int) R.drawable.mylibsutil_bg_null).into(this.mImage);
            }
        }
    }



    private void showListPhoto() {

            this.mAdapter = new ListImageAdapter(getSupportFragmentManager());
            this.mPager.setAdapter(this.mAdapter);
            this.mPager.setCurrentItem(this.mInitialPosition);

    }





}