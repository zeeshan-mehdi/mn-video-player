package com.jamshed.videoplayer;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.support.constraint.ConstraintLayout;
import android.support.v7.widget.RecyclerView.Adapter;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

public class Adapter_VideoFolder extends Adapter<Adapter_VideoFolder.ViewHolder> {
    Activity activity;
    ArrayList<Model_Video> al_video;
    ArrayList<String> all_paths;
    Context context;
    int i ;

    public static class ViewHolder extends android.support.v7.widget.RecyclerView.ViewHolder {
        public ImageView iv_image;
        ConstraintLayout rl_select;
        public TextView tvTitle;
        public ImageView button;

        public ViewHolder(View view) {
            super(view);
            this.iv_image = (ImageView) view.findViewById(R.id.iv_image);
            this.tvTitle = (TextView) view.findViewById(R.id.tv_title);
            this.rl_select = (ConstraintLayout) view.findViewById(R.id.ll_item);
            this.button = view.findViewById(R.id.button);
        }
    }

    public Adapter_VideoFolder(Context context, ArrayList<Model_Video> arrayList, Activity activity, ArrayList<String> arrayList2) {
        this.al_video = arrayList;
        this.context = context;
        this.activity = activity;
        this.all_paths = arrayList2;
    }

    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        return new ViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.adapter_videos, viewGroup, false));
    }

    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Glide.with(this.context).load(((Model_Video) this.al_video.get(i)).getStr_thumb()).skipMemoryCache(false).into(viewHolder.iv_image);
        viewHolder.tvTitle.setText(((Model_Video) this.al_video.get(i)).title);
        viewHolder.rl_select.setOnClickListener(new OnClickListener() {
            public void onClick(View view1) {
                Intent view = new Intent(Adapter_VideoFolder.this.context, videoplayer.class);
                view.putExtra("url", "offline");
                view.putStringArrayListExtra("urlarray", Adapter_VideoFolder.this.all_paths);
                view.putExtra("index", i);
                Adapter_VideoFolder.this.activity.startActivity(view);
            }
        });


        viewHolder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popup = new PopupMenu(context, viewHolder.button);
                //inflating menu from xml resource
                popup.inflate(R.menu.video_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {

                        int id = item.getItemId();
                        switch (id) {
                            case R.id.mDelete: {
                                try {

                                    deleteVideo(Adapter_VideoFolder.this.all_paths.get(i));
                                    Adapter_VideoFolder.this.all_paths.remove(i);
                                    Adapter_VideoFolder.this.al_video.remove(i);
                                    notifyDataSetChanged();

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                                break;
                            }
                            case R.id.mShare: {
                                //shareActionProvider = item.getActionProvider();

                                shareImage(Adapter_VideoFolder.this.all_paths.get(i));


                                break;
                            }
                            case R.id.mPlay: {
                                Intent view = new Intent(Adapter_VideoFolder.this.context, videoplayer.class);
                                view.putExtra("url", "offline");
                                view.putStringArrayListExtra("urlarray", Adapter_VideoFolder.this.all_paths);
                                view.putExtra("index", i);
                                Adapter_VideoFolder.this.activity.startActivity(view);
                                break;
                            }
                            default:{
                                Log.e("default","its default ..");
                            }
                        }
                        return  true;
                    }
                });
                //displaying the popup
                popup.show();
            }
        });

    }

    public int getItemCount() {
        return this.al_video.size();
    }

    private void shareImage(String path) {
        Intent share = new Intent(Intent.ACTION_SEND);
        share.setType("video/*");

        File imageFileToShare = new File(path);


        Uri uri = Uri.fromFile(imageFileToShare);
        share.putExtra(Intent.EXTRA_STREAM, uri);

        share.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

        activity.startActivity(Intent.createChooser(share, "Share Video!"));
    }

    public void deleteVideo(String mFilePath){
        File file = new File(mFilePath);
        if (file.exists()) {
            if (file.delete()) {
                Log.e("-->", "file Deleted :" + mFilePath);
                callBroadCast();
            } else {
                Log.e("-->", "file not Deleted :" + mFilePath);
            }
        }


        //callBroadCast();
        callScanItent(context,mFilePath);
    }
    public void callBroadCast() {
        if (Build.VERSION.SDK_INT >= 14) {
            Log.e("-->", " >= 14");
            MediaScannerConnection.scanFile(context, new String[]{Environment.getExternalStorageDirectory().toString()}, null, new MediaScannerConnection.OnScanCompletedListener() {
                /*
                 *   (non-Javadoc)
                 * @see android.media.MediaScannerConnection.OnScanCompletedListener#onScanCompleted(java.lang.String, android.net.Uri)
                 */
                public void onScanCompleted(String path, Uri uri) {
                    Log.e("ExternalStorage", "Scanned " + path + ":");
                    Log.e("ExternalStorage", "-> uri=" + uri);
                }
            });
        }
    }
    public void callScanItent(Context context, String path) {
        MediaScannerConnection.scanFile(context,
                new String[] { path }, null,null);
    }



}
