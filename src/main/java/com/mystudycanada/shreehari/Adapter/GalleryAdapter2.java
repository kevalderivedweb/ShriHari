package com.mystudycanada.shreehari.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.BitmapImageViewTarget;
import com.mystudycanada.shreehari.Model.IOnResourceReady;
import com.mystudycanada.shreehari.R;

import java.io.File;
import java.util.List;

public class GalleryAdapter2 extends BaseAdapter {

    private static final String TAG = "GalleryAdapter";
    private List<File> data;
    private LayoutInflater inflater;
    private Activity mContext;
    private int mSizeData = 0;
    ViewHolder holder;


    static class ViewHolder {
        ImageView imgPhoto;
        TextView txt;

        ViewHolder(View v) {
            imgPhoto = v.findViewById(R.id.img);
            txt = v.findViewById(R.id.txt);
        }
    }

    public GalleryAdapter2(Activity context, List<File> arrayData) {
        this.mContext = context;
        this.data = arrayData;
        this.mSizeData = this.data.size();
        this.inflater = LayoutInflater.from(this.mContext);
    }





    @Override
    public int getCount() {
        return this.mSizeData;
    }

    @Override
    public File getItem(int position) {
        return (File) this.data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {

        if (view == null) {
            view = this.inflater.inflate(R.layout.item_sticker_photo, parent, false);
            holder = new ViewHolder(view);
            holder.imgPhoto.setScaleType(ImageView.ScaleType.CENTER_CROP);
            view.setTag(holder);
        }
        holder = (ViewHolder) view.getTag();
        holder.txt.setText(data.get(position).getAbsoluteFile().getName());
        Glide.with(mContext).load(data.get(position).getAbsoluteFile()).into(holder.imgPhoto);


        return view;
    }






    public static DisplayMetrics getDisplayInfo() {
        return Resources.getSystem().getDisplayMetrics();
    }

   /* public static void displayImage(Context context, ImageView image, String url) {
        displayImage(context, image, url, R.drawable.mylibsutil_bg_null, R.drawable.mylibsutil_bg_null);
    }

    public static void displayImage(Context context, ImageView image, String url, IOnResourceReady iOnResourceReady) {
        displayImage(context, image, url, R.drawable.mylibsutil_bg_null, R.drawable.mylibsutil_bg_null, iOnResourceReady);
    }

    public static void displayImage(Context context, ImageView image, int resourceId) {
        displayImage(context, image, Integer.valueOf(resourceId), R.drawable.mylibsutil_bg_null, R.drawable.mylibsutil_bg_null);
    }*/

   /* public static void displayImage(Context context, ImageView image, Object imageSource, int fallBackDrawable, int errorDrawable) {
        Glide.with(context).load(imageSource).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().placeholder(errorDrawable).fallback(fallBackDrawable).error(errorDrawable).animate(R.anim.anim_fade_in).into(image);
    }

    public static void displayImage(Context context, ImageView image, Object imageSource, int fallBackDrawable, int errorDrawable, final IOnResourceReady iOnResourceReady) {
        Glide.with(context).load(imageSource).asBitmap().diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().placeholder(errorDrawable).fallback(fallBackDrawable).error(errorDrawable).animate(R.anim.anim_fade_in).into(new BitmapImageViewTarget(image) {
            public void onLoadStarted(Drawable placeholder) {
                super.onLoadStarted(placeholder);
            }

            public void onLoadFailed(Exception e, Drawable errorDrawable) {
                super.onLoadFailed(e, errorDrawable);
                if (iOnResourceReady != null) {
                    iOnResourceReady.onLoadFailed();
                }
            }

            public void onResourceReady(Bitmap drawable, GlideAnimation anim) {
                super.onResourceReady(drawable, anim);
                if (iOnResourceReady != null) {
                    iOnResourceReady.OnResourceReady(drawable);
                }
            }
        });
    }

*/

}
