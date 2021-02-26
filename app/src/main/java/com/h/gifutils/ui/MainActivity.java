package com.h.gifutils.ui;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.h.gifutils.R;
import com.h.gifutils.utils.FileUtils;
import com.h.gifutils.utils.Scheduler;
import com.h.gifutils.utils.Schedulers;
import com.hjq.permissions.OnPermissionCallback;
import com.hjq.permissions.Permission;
import com.hjq.permissions.XXPermissions;

import java.util.List;

import static android.content.Intent.EXTRA_ALLOW_MULTIPLE;

public class MainActivity extends BaseActivity {
    private static final int REQUEST_IMAGE_GET = 1000;
    private static final int REQUEST_VIDEO_GET = 1001;
    private static final String TAG = "MainActivity";

    @Override
    public int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    public void initView() {
        XXPermissions.with(this)
                .permission(Permission.MANAGE_EXTERNAL_STORAGE)
                .request(new OnPermissionCallback() {
                    @Override
                    public void onGranted(List<String> permissions, boolean all) {
                        if (all)
                            Toast.makeText(MainActivity.this, "获取读写权限成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onDenied(List<String> permissions, boolean never) {
                        if (never) {
                            onBackPressed();
                        } else {
                            Toast.makeText(MainActivity.this, "获取读写权限成失败", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void setListeners() {

    }

    @SuppressLint("NonConstantResourceId")
    public void onClick(View view) {
        int id = view.getId();
        switch (id) {
            case R.id.decomposition_gif:
                selectGifImage(false);
                break;
            case R.id.image2gif:
                selectImage(true);
                break;
            case R.id.video2gif:
                selectVideo();
                break;
            case R.id.marge_gif:
                selectImage(true);
                break;
            default:
                break;
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void selectGifImage(boolean isMultiple) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/gif");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(EXTRA_ALLOW_MULTIPLE,isMultiple);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void selectImage(boolean isMultiple) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.putExtra(EXTRA_ALLOW_MULTIPLE,isMultiple);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_IMAGE_GET);
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    public void selectVideo() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("video/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(intent, REQUEST_VIDEO_GET);
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) return;
        if (requestCode == REQUEST_IMAGE_GET) {
            Uri fullPhotoUri = data.getData();
        } else if (requestCode == REQUEST_VIDEO_GET) {
            Uri fullPhotoUri = data.getData();
            Schedulers.io().execute(new Runnable() {
                @Override
                public void run() {
                    String path = FileUtils.getFilePathByUri(MainActivity.this,fullPhotoUri);
                    TrimVideoActivity.startActivity(MainActivity.this,path);
                    Log.d(TAG, "onActivityResult: "+path);
                }
            });
        }
    }

}

