package com.hd.wallpaper.background.Activity;

import android.Manifest;
import android.app.DownloadManager;
import android.app.WallpaperManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.Priority;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;
import com.google.gson.Gson;
import com.hd.wallpaper.background.Api.PhotoApi;
import com.hd.wallpaper.background.BuildConfig;
import com.hd.wallpaper.background.Dialog.WallpaperDialog;
import com.hd.wallpaper.background.Helper.DownloadManagerHelper;
import com.hd.wallpaper.background.Model.Photo;
import com.hd.wallpaper.background.R;
import com.hd.wallpaper.background.Retrofit.Unsplash;
import com.mikhaellopez.circularimageview.CircularImageView;

import java.io.File;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


import static android.Manifest.permission.READ_EXTERNAL_STORAGE;
import static android.Manifest.permission.WRITE_EXTERNAL_STORAGE;
import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;
import static com.hd.wallpaper.background.Helper.DownloadManagerHelper.DownloadType.DOWNLOAD;
import static com.hd.wallpaper.background.Helper.DownloadManagerHelper.DownloadType.WALLPAPER;


public class FeaturedPhotoActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView img_photo;
    private TextView txt_username,txt_date_taken,txt_like,txt_location,txt_downloads;
    private PhotoApi photoApi;
    private String imageId;
    private FeaturedPhotoActivity activity;
    private ImageButton ibtn_back;
    private ImageView img_share;
    private Photo photo;
    private String user_name;
    private LinearLayout ly_user_name,ly_user_info,ly_progressbar;
    private CircularImageView img_user_circle;
    private FloatingActionButton fab_setWallpaper,fab_download,fab_info;
    private FloatingActionMenu floating_action_menu;
    private TextView txt_focal_length,txt_iso,txt_aperture,txt_exposure_time,txt_model,txt_make,txt_dimension;
    private Photo responsePhoto;
    private final int STORAGE_PERMISSION_CODE = 2;
    private String TAG = FeaturedPhotoActivity.class.getName();
    private File sdCardFolder;
    private String fileName;
    private File photoFile;
    private Uri uri;
    private long downloadReference;
    private @DownloadManagerHelper.DownloadType int currentAction;
    private WallpaperDialog wallpaperDialog;

    IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
    BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            long reference = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);
            if (downloadReference == reference) {
                Cursor cursor = DownloadManagerHelper.getInstance(FeaturedPhotoActivity.this).getDownloadCursor(downloadReference);
                if (cursor != null)
                {
                    switch (DownloadManagerHelper.getInstance(FeaturedPhotoActivity.this).getDownloadStatus(cursor)) {
                        case DownloadManagerHelper.DownloadStatus.SUCCESS:
//                            File file = new File(DownloadManagerHelper.getInstance(PhotoActivity.this).getFilePath(downloadReference));
//                            Uri uri = FileProvider.getUriForFile(PhotoActivity.this, BuildConfig.APPLICATION_ID + ".fileprovider", file);
                            uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                            getApplicationContext().sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, uri));
                            if (currentAction == WALLPAPER) {
                                setWallpaper(uri);
                                if (wallpaperDialog != null) wallpaperDialog.setDownloadFinished(true);
                            }
                            break;
                        default:
                            break;
                    }
                    cursor.close();
                }
                if (currentAction == WALLPAPER)
                {
                    if (wallpaperDialog != null) wallpaperDialog.dismiss();
                }
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_featured_photo);
        activity = FeaturedPhotoActivity.this;
        initView();
        initViewAction();
        initViewListener();

    }

    private void initView()
    {
        imageId = getIntent().getStringExtra("imageId");
        img_photo = findViewById(R.id.img_photo);
        txt_username = findViewById(R.id.txt_username);
        txt_like = findViewById(R.id.txt_like);
        txt_date_taken = findViewById(R.id.txt_date_taken);
        ibtn_back = findViewById(R.id.ibtn_back);
        img_share = findViewById(R.id.img_share);
        img_user_circle = findViewById(R.id.img_user_circle);
        txt_location = findViewById(R.id.txt_location);
        txt_downloads = findViewById(R.id.txt_downloads);
//        img_user = findViewById(R.id.img_user);
//        img_user.setLayerType(View.LAYER_TYPE_HARDWARE, null);
        ly_user_name = findViewById(R.id.ly_user_name);
        ly_progressbar = findViewById(R.id.ly_progressbar);
        ly_user_info = findViewById(R.id.ly_user_info);
        floating_action_menu = findViewById(R.id.floating_action_menu);
        fab_info = findViewById(R.id.fab_info);
        fab_download = findViewById(R.id.fab_download);
        fab_setWallpaper = findViewById(R.id.fab_setWallpaper);
    }

    private void initViewAction()
    {
        photo = new Gson().fromJson(getIntent().getStringExtra("photo"),Photo.class);

        if(photo!=null)
        {
            if (WallpaperApplication.getWallpaperApplication().getDrawable() != null) {
                img_photo.setImageDrawable(WallpaperApplication.getWallpaperApplication().getDrawable());
                WallpaperApplication.getWallpaperApplication().setDrawable(null);
            }
            else
            {
                Glide.with(activity)
                        .load(photo.getUrls().getFull())
                        .apply(new RequestOptions()
                                .priority(Priority.HIGH)
                                .placeholder(new ColorDrawable(getResources().getColor(R.color.light_grey)))
                        )
                        .transition(withCrossFade())
//                        .dontAnimate()
                        .into(img_photo);
            }

            txt_username.setText("By "+photo.getUser().getName());

            user_name = photo.getUser().getUsername();

            Log.e("User_image",photo.getUser().getProfileImage().getSmall());

            Glide.with(activity)
                    .asBitmap()
                    .load(photo.getUser().getProfileImage().getLarge())
                    .apply(new RequestOptions()
                                    .priority(Priority.HIGH)
//                            .placeholder(new ColorDrawable(R.attr.colorPrimary))
                                    .placeholder(R.drawable.gradient_drawable)
                    )
//                    .transition(withCrossFade())
                    .into(img_user_circle);

            String update_at_response = photo.getUpdatedAt();
            String update_at_split[] = update_at_response.split("T");
            txt_date_taken.setText(update_at_split[0]);
            txt_like.setText(String.valueOf(photo.getLikes()) + " likes");

            /*ly_user_info.setVisibility(View.VISIBLE);
            ly_progressbar.setVisibility(View.GONE);*/

            photoApi = Unsplash.getRetrofit().create(PhotoApi.class);
            Call<Photo> call = photoApi.getPhotoById(imageId);
            call.enqueue(new Callback<Photo>()
            {
                @Override
                public void onResponse(Call<Photo> call, Response<Photo> response)
                {
                    if (response.isSuccessful())
                    {
                        responsePhoto = response.body();
                        txt_location.setText((responsePhoto.getUser().getLocation()!=null) ? responsePhoto.getUser().getLocation() : getString(R.string.unknown));
                        txt_downloads.setText(Integer.toString(responsePhoto.getDownloads()));
                        ly_user_info.setVisibility(View.VISIBLE);
                        ly_progressbar.setVisibility(View.GONE);
                    }
                    else
                    {
                        Log.e("response", "error in response" + response.toString());
                    }
                }

                @Override
                public void onFailure(Call<Photo> call, Throwable t)
                {
//                    Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            });
        }
        else
        {
            photoApi = Unsplash.getRetrofit().create(PhotoApi.class);
            Call<Photo> call = photoApi.getPhotoById(imageId);
            call.enqueue(new Callback<Photo>() {
                @Override
                public void onResponse(Call<Photo> call, Response<Photo> response) {
                    if (response.isSuccessful()) {
                        responsePhoto = response.body();
                        Glide.with(activity)
//                            .placeholder(R.drawable.error_drawable)
                                .load(responsePhoto.getUrls().getFull())
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                .fitCenter()
                                .apply(new RequestOptions()
                                        .priority(Priority.HIGH)
                                        .placeholder(new ColorDrawable(R.attr.colorPrimary))
                                )
                                .transition(withCrossFade())
                                .into(img_photo);
                        txt_username.setText("By " + responsePhoto.getUser().getName());

                        user_name = responsePhoto.getUser().getUsername();

                        String update_at_response = responsePhoto.getUpdatedAt();
                        String update_at_split[] = update_at_response.split("T");
                        txt_date_taken.setText(update_at_split[0]);
                        txt_like.setText(String.valueOf(responsePhoto.getLikes()) + " likes");
                        txt_location.setText(responsePhoto.getUser().getLocation());
                        txt_downloads.setText(Integer.toString(responsePhoto.getDownloads()));
                        ly_user_info.setVisibility(View.VISIBLE);
                        ly_progressbar.setVisibility(View.GONE);
                    }
                    else
                    {
                        Log.e("response", "error in response" + response.toString());
                    }
                }

                @Override
                public void onFailure(Call<Photo> call, Throwable t) {
//                    Toast.makeText(activity, "Error Occured", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void initViewListener()
    {
        ibtn_back.setOnClickListener(this);
        img_share.setOnClickListener(this);
        ly_user_name.setOnClickListener(this);
        fab_info.setOnClickListener(this);
        fab_download.setOnClickListener(this);
        fab_setWallpaper.setOnClickListener(this);
    }

    @Override
    public void onClick(View v)
    {
        switch (v.getId())
        {
            case R.id.ibtn_back:
                onBackPressed();
                break;
            case R.id.img_share:
                shareImage();
                break;
            case R.id.ly_user_name:
                goToPhotographerActivity();
                break;
            case R.id.fab_info:
                if(floating_action_menu.isOpened())
                {
                    floating_action_menu.close(true);
                }
                showCustomDialog();
                break;
            case R.id.fab_download:
                currentAction = DOWNLOAD;
                if(floating_action_menu.isOpened())
                {
                    floating_action_menu.close(true);
                }
                if (checkAndRequestPermissionStorage(STORAGE_PERMISSION_CODE))
                {
//                    Toast.makeText(activity, "Permission is Already Granted", Toast.LENGTH_SHORT).show();
                    downloadPhoto();
                }
                break;
            case R.id.fab_setWallpaper:
                currentAction = WALLPAPER;
                if(floating_action_menu.isOpened())
                {
                    floating_action_menu.close(true);
                }
                if(checkAndRequestPermissionStorage(STORAGE_PERMISSION_CODE))
                {
//                    setWallpaperPrepare();
                    downloadPhoto();
                }
                break;
        }
    }

    private void setWallpaper()
    {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);
        downloadPhoto();
        Uri uri = null;
        try
        {
            uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
        }
        catch (Exception e)
        {
            Log.e("Exception","Exception is :"+e.getMessage());
        }
        try
        {
            Intent intent = wallpaperManager.getCropAndSetWallpaperIntent(uri);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("mimeType", "image/*");
            startActivityForResult(intent, 13451);
        }
        catch (Exception e)
        {
            Log.e("Exception",e.getMessage());
            Intent wallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
            wallpaperIntent.setDataAndType(uri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
            wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(Intent.createChooser(wallpaperIntent, getString(R.string.set_as_wallpaper)));
//            Toast.makeText(activity, "Error is : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    /*private void downloadPhoto()
    {
        boolean isSdCardPresent = android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        Log.e("isSdCardPresent","isSdCardPresent is : "+String.valueOf(isSdCardPresent));
        Log.e("isSDSupportedDevice","isSDSupportedDevice is :"+String.valueOf(isSDSupportedDevice));

        if(isSdCardPresent)
        {
            File sdCard = Environment.getExternalStorageDirectory();

            sdCardFolder = new File(sdCard.getAbsoluteFile(),"WallpaperApp");
            if(!sdCardFolder.exists())
            {
                sdCardFolder.mkdir();
                Log.e("folder","folder is created");
                Toast.makeText(activity, "Folder is Created", Toast.LENGTH_SHORT).show();
            }

            fileName = responsePhoto.getId()+".jpg";

            photoFile = new File(sdCard+"/WallpaperApp/"+fileName);
            if (!photoFile.exists())
            {
                String photoId = responsePhoto.getId();


                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(responsePhoto.getUrls().getFull()))
                        .setTitle("Downloading Photo")
                        .setDescription("Downloading")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationUri(Uri.fromFile(photoFile));

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                downloadReference = downloadManager.enqueue(request);

//                downloadReference = DownloadManagerHelper.getInstance(activity).addDownloadRequest(DownloadManagerHelper.DownloadType.DOWNLOAD,responsePhoto.getUrls().getFull(),fileName);

                *//*photoApi.downloadPhoto(photoId);
                Call<ResponseBody> call = photoApi.downloadPhoto(photoId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());

                        saveImagetoStorage(response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {

                    }
                });*//*
            }
            else
            {
                Toast.makeText(activity, "File is already present", Toast.LENGTH_SHORT).show();
            }
        }
        else
        {
            Toast.makeText(activity, "SD Card is not present", Toast.LENGTH_SHORT).show();
        }
    }*/

    private void downloadPhoto()
    {
        boolean isSdCardPresent = android.os.Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
        boolean isSDSupportedDevice = Environment.isExternalStorageRemovable();
        Log.e("isSdCardPresent", "isSdCardPresent is : " + String.valueOf(isSdCardPresent));
        Log.e("isSDSupportedDevice", "isSDSupportedDevice is :" + String.valueOf(isSDSupportedDevice));

        if (isSdCardPresent)
        {
            File sdCard = Environment.getExternalStorageDirectory();

            sdCardFolder = new File(sdCard.getAbsoluteFile(), "WallpaperApp");
            if (!sdCardFolder.exists())
            {
                sdCardFolder.mkdir();
                Log.e("folder", "folder is created");
                Toast.makeText(activity, "Folder is Created", Toast.LENGTH_SHORT).show();
            }

            fileName = responsePhoto.getId() + ".jpg";

            photoFile = new File(sdCard + "/WallpaperApp/" + fileName);
            if (!photoFile.exists())
            {
                String photoId = responsePhoto.getId();


                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(responsePhoto.getUrls().getFull()))
                        .setTitle("Downloading Photo")
                        .setDescription("Downloading")
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE)
                        .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                        .setDestinationUri(Uri.fromFile(photoFile));

                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);

                request.allowScanningByMediaScanner();

                downloadReference = downloadManager.enqueue(request);

                if(currentAction==WALLPAPER)
                {
                    wallpaperDialog = new WallpaperDialog();
                    wallpaperDialog.setListener(() -> DownloadManagerHelper.getInstance(activity).removeDownloadRequest(downloadReference));
                    wallpaperDialog.show(getFragmentManager(), null);

                    /*uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                    setWallpaper(uri);*/
                }

//                downloadReference = DownloadManagerHelper.getInstance(activity).addDownloadRequest(DownloadManagerHelper.DownloadType.DOWNLOAD,responsePhoto.getUrls().getFull(),fileName);

                /*photoApi.downloadPhoto(photoId);
                Call<ResponseBody> call = photoApi.downloadPhoto(photoId);
                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response)
                    {
                        Bitmap bitmap = BitmapFactory.decodeStream(response.body().byteStream());

                        saveImagetoStorage(response.body());
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t)
                    {

                    }
                });*/
            }
            else
            {
//                Toast.makeText(activity, "File is already present", Toast.LENGTH_SHORT).show();
                if(currentAction==WALLPAPER)
                {
                    uri = FileProvider.getUriForFile(activity, BuildConfig.APPLICATION_ID + ".fileprovider", photoFile);
                    setWallpaper(uri);
                }
            }
        } else {
            Toast.makeText(activity, "SD Card is not present", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults)
    {

        if (permissions.length == 0) {
            return;
        }

        boolean allPermissionsGranted = true;
        if (grantResults.length > 0)
        {
            for (int grantResult : grantResults) {
                if (grantResult != PackageManager.PERMISSION_GRANTED) {
                    allPermissionsGranted = false;
                    break;
                }
            }
        }
        if (!allPermissionsGranted)
        {
            boolean somePermissionsForeverDenied = false;
            for (String permission : permissions)
            {
                if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission))
                {
                    //denied

                    Log.e(TAG, "onRequestPermissionsResult:===> permition denied " + permission);
                    switch (requestCode)
                    {
                        case STORAGE_PERMISSION_CODE:
                            break;
                        default:
                            break;
                    }

                } else {
                    if (ActivityCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_GRANTED) {
                        //allowed
                        Log.e("allowed", permission);
                        Log.e(TAG, "onRequestPermissionsResult:permition allowed " + permission);
                    } else {
                        //set to never ask again
                        Log.e("set to never ask again", permission);
                        Log.e(TAG, "onRequestPermissionsResult:set to never ask again " + permission);
                        somePermissionsForeverDenied = true;
                    }
                }
            }
            if (somePermissionsForeverDenied)
            {

                Log.e(TAG, "onRequestPermissionsResult: somePermissionsForeverDenied " + somePermissionsForeverDenied);


                String title = "";
                switch (requestCode)
                {
                    case STORAGE_PERMISSION_CODE:
                        if ((ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) || (ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
                            title += "Storage";
                        }
                        break;
                    default:
                        break;
                }

                if (title.toString().trim().endsWith(",") || title.toString().trim().endsWith("&")) {
                    title = title.toString().trim().substring(0, title.toString().trim().length() - 1);
                }

                final android.app.AlertDialog.Builder alertDialogBuilder = new android.app.AlertDialog.Builder(this);
                alertDialogBuilder.setTitle("Permissions Required")
                        .setMessage("Please allow permission for " + title + ".")
                        .setPositiveButton("Cancel", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setNegativeButton("Ok", new DialogInterface.OnClickListener()
                        {
                            @Override
                            public void onClick(DialogInterface dialog, int which)
                            {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                        Uri.fromParts("package", getPackageName(), null));
                                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                                startActivity(intent);
                            }
                        })
                        .setCancelable(false)
                        .create()
                        .show();
            }
        }
        else
        {
            switch (requestCode)
            {
                case STORAGE_PERMISSION_CODE:
//                    Toast.makeText(activity, "Permission is Granted", Toast.LENGTH_SHORT).show();
                    downloadPhoto();
                    break;
            }
        }
    }

    private boolean checkAndRequestPermissionStorage(int code)
    {
        if (ContextCompat.checkSelfPermission(activity, WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(activity, READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(activity, new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE, READ_EXTERNAL_STORAGE},
                    code);
            Log.e("return", "return false from Location Permission");
            return false;
        }
        else
        {
            Log.e("return", "return true from Location Permission");
            return true;
        }
    }

    private void showCustomDialog()
    {
        ViewGroup viewGroup = findViewById(android.R.id.content);

        View dialogView = LayoutInflater.from(activity).inflate(R.layout.photo_info_dialog,viewGroup,false);

        txt_dimension = dialogView.findViewById(R.id.txt_dimension);
        txt_make = dialogView.findViewById(R.id.txt_make);
        txt_model = dialogView.findViewById(R.id.txt_model);
        txt_exposure_time = dialogView.findViewById(R.id.txt_exposure_time);
        txt_aperture = dialogView.findViewById(R.id.txt_aperture);
        txt_iso = dialogView.findViewById(R.id.txt_iso);
        txt_focal_length = dialogView.findViewById(R.id.txt_focal_length);

        txt_dimension.setText(String.valueOf(responsePhoto.getHeight()) + "X" +String.valueOf(responsePhoto.getWidth()));
        txt_make.setText(responsePhoto.getExif().getMake());
        txt_model.setText(responsePhoto.getExif().getModel());
        txt_exposure_time.setText(responsePhoto.getExif().getExposure_time());
        txt_aperture.setText(responsePhoto.getExif().getAperture());
        txt_iso.setText(responsePhoto.getExif().getIso());
        txt_focal_length.setText(responsePhoto.getExif().getFocal_length());

        AlertDialog.Builder builder = new AlertDialog.Builder(activity);

        builder.setView(dialogView);

        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }

    private void goToPhotographerActivity()
    {
        WallpaperApplication.getWallpaperApplication().setUser_drawable(img_user_circle.getDrawable());

        Intent photoGrapherIntent = new Intent(activity,PhotoGrapherActivity.class);
        photoGrapherIntent.putExtra("name",txt_username.getText().toString());
        photoGrapherIntent.putExtra("userName",user_name);
        startActivity(photoGrapherIntent);
    }

    private void shareImage()
    {
        if(photo!=null)
        {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);

            shareIntent.setType("text/plain");
            shareIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);


            shareIntent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.unsplash_image));
            shareIntent.putExtra(Intent.EXTRA_TEXT, photo.getLinks().getHtml() + WallpaperApplication.UNSPLASH_UTM_PARAMETERS);

            startActivity(Intent.createChooser(shareIntent, getString(R.string.share_via)));
        }
    }

    private void setWallpaper(Uri uri)
    {
        WallpaperManager wallpaperManager = WallpaperManager.getInstance(activity);

        try
        {
            Intent intent = wallpaperManager.getCropAndSetWallpaperIntent(uri);
            intent.setDataAndType(uri, "image/*");
            intent.putExtra("mimeType", "image/*");
            startActivityForResult(intent, 13451);
        }
        catch (Exception e)
        {
            Log.e("Exception", e.getMessage());
            Intent wallpaperIntent = new Intent(Intent.ACTION_ATTACH_DATA);
            wallpaperIntent.setDataAndType(uri, "image/*");
            wallpaperIntent.putExtra("mimeType", "image/*");
            wallpaperIntent.addCategory(Intent.CATEGORY_DEFAULT);
            wallpaperIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            wallpaperIntent.addFlags(Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
            startActivity(Intent.createChooser(wallpaperIntent, getString(R.string.set_as_wallpaper)));
//            Toast.makeText(activity, "Error is : "+e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(receiver, filter);
    }

    @Override
    protected void onDestroy()
    {
//        unregisterReceiver(receiver);
        super.onDestroy();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(receiver);
    }
}
