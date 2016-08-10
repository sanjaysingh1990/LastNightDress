package com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.hardware.Camera;
import android.media.AudioManager;
import android.media.CamcorderProfile;
import android.media.MediaRecorder;
import android.media.MediaScannerConnection;
import android.media.SoundPool;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentTransaction;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Surface;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.UserProfile.SelectCategoryActivity;
import com.eowise.recyclerview.stickyheaders.samples.Utils.CamUtils;
import com.eowise.recyclerview.stickyheaders.samples.Utils.ImageConvertor;
import com.eowise.recyclerview.stickyheaders.samples.data.CameraData;
import com.eowise.recyclerview.stickyheaders.samples.define.Extension;
import com.eowise.recyclerview.stickyheaders.samples.define.Folder;
import com.eowise.recyclerview.stickyheaders.samples.define.Receiver;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;


public class CameraPreviewFragment extends Fragment
        implements View.OnClickListener {

    /**
     * Data section
     */

    /**
     * String section
     */
    public static int current_camera_id = com.eowise.recyclerview.stickyheaders.samples.define.Camera.CAMERA_BACK;

    public static int x = 0;
    //    public static float bottom_bar = 0;
    public static float top_bar = 0;
    public static boolean IS_BACK_CAMERA_OR_FRONT_CAMERA = true;
    public static boolean IS_PHOTO_MODE_OR_VIDEO_MODE = true;
    public static boolean IS_RECORDING_VIDEO = false;
    public static String filename = "";
    private static int minute = 0;
    private static String RECORDED_FILE_PATH = null;
    private static int second = 0;
    public ImageView gallery, image1, image2, image3, image4;
    private ImageButton cancel1, cancel2, cancel3, cancel4;
    private Bitmap capturedImage;
    private static ImageButton camera_flash;
    public ImageButton add1, add2, add3, add4;
    public static int GALLERY_INTENT_CALLED = 200;
    SoundPool soundPool;
    int shutterSound;
    /**
     * View section
     */
    private static CustomCameraPreview mCameraPreview;
    private static ImageButton mIbtnTakePhotoOrRecordVideo;
    private static TextView mTvElapseTime;
    private ImageButton done;
    FrameLayout frame1, frame2, frame3, frame4;
    public static int flashMode;
    public static File pictureFile = null;
    public static byte[] imgdata;
    /**
     * Others section
     */

    // new Counter that counts 3000 ms with a tick each 1000 ms
    private static CountDownTimer mCdt = new CountDownTimer(24 * 60 * 60 * 1000, 1000) {
        public void onTick(long millisUntilFinished) {
            //update the UI with the new count
            second += 1;

            if (second == 2)
                // Enable again for user clicked to stop recording video
                mIbtnTakePhotoOrRecordVideo.setEnabled(true);

            if (second == 60) {
                minute = minute + 1;

                second = 0;
            }
            if (minute < 10) {
                mTvElapseTime.setText("0" + minute + " : " + second);
            } else
                mTvElapseTime.setText(minute + " : " + second);
        }

        public void onFinish() {
            //start the activity
        }
    };
    private boolean IS_ALREADY_ADDED_CAMERA_REVIEW_INSIDE = false;
    private boolean IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE = false;
    /**
     * The others methods
     */

    private Camera.PictureCallback mPhoto = new Camera.PictureCallback() {

        @Override
        public void onPictureTaken(byte[] data, Camera camera) {
            // Pass True value to indicate choose Photo mode
            imgdata = data;
            soundPool.play(shutterSound, 1f, 1f, 0, 0, 1);
            if (pictureFile == null) {
                Log.i("", "Error creating media file, check storage permissions: ");
                return;
            }

//            try {

            // After take picture successfully,
            //      - Need refresh Gallery to see new image
            //      - Go to preview page to see taken picture

            // Refresh Gallery
            //   CamUtils.addPictureToGallery(getActivity(), pictureFile.getAbsolutePath());

            // Go to Review page
            FragmentTransaction ft = ((FragmentActivity) getActivity())
                    .getSupportFragmentManager().beginTransaction();
            ft.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_from_left);

            ft.addToBackStack(null)
                    .replace(R.id.fl_custom_camera,
                            CameraReviewFragment.newInstance(pictureFile.getAbsolutePath()))
                    .commitAllowingStateLoss();
/*            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }*/
        }
    };
    private FrameLayout mFlCameraPreview;
    private LinearLayout mFlSwitchFrontOrBackCamera;
    private FrameLayout mFlTakePhotoOrRecordVideo;
    private ImageButton mIbtnClose;

    private ImageButton mIbtnSwitchFrontOrBackCamera;
    private ImageButton mIbtnSwitchTakePhotoOrRecordVideo;
    private LinearLayout mLlElapseTime;

    public static Fragment newInstance() {
        CameraPreviewFragment fragment = new CameraPreviewFragment();
        return fragment;
    }

    /**
     * Get current camera ID : Back Camera or Front Camera
     */
    public static int switchCurrentCameraID() {
        int current_camera_id = -1;

        if (IS_BACK_CAMERA_OR_FRONT_CAMERA) {
            // Currently - Front, switch to Back
            IS_BACK_CAMERA_OR_FRONT_CAMERA = false;

            current_camera_id = com.eowise.recyclerview.stickyheaders.samples.define.Camera.CAMERA_BACK;
        } else {
            // Currently - Back, switch to Front
            IS_BACK_CAMERA_OR_FRONT_CAMERA = true;

            current_camera_id = com.eowise.recyclerview.stickyheaders.samples.define.Camera.CAMERA_FRONT;
        }

        return current_camera_id;
    }

    /**
     * Create a File for saving an image or video
     */
    private static File getOutputMediaFile(Context mContext, boolean mode) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.
        File mediaStorageDir = new File(
                Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_PICTURES), Folder.FOLDER_NAME);

        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                // Log.i("", "failed to create directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile = null;
        if (mode) {
            // Photo mode
            mediaFile = new File(
                    mediaStorageDir.getPath() + File.separator + "IMG_" + timeStamp + Extension.JPG);
        } else {
            // Video mode
            mediaFile = new File(
                    mediaStorageDir.getPath() + File.separator + "VID_" + timeStamp + Extension.MP4);
        }

        String FILE_PATH = mediaFile.getAbsolutePath();

        // Set file path into single ton way
        Uri mUri = Uri.parse(FILE_PATH);

        CustomCamera.camera.setFilePath(mUri.toString());

        // Tell the media scanner about the new file so that it is
        // immediately available to the user.
        MediaScannerConnection.scanFile(mContext,
                new String[]{FILE_PATH}, null,
                new MediaScannerConnection.OnScanCompletedListener() {
                    public void onScanCompleted(String path, Uri uri) {
                    }
                });
        return mediaFile;
    }

    public static boolean prepareVideoRecorder(Context mContext, int mode) {
        // Should release before use new Preview for Recording Video mode
        CustomCamera.releaseCamera();

        // Initialize camera
        CustomCamera.mCamera = CustomCamera.getCameraInstance(mode);

        CustomCamera.mCamera.startPreview();
        // Set orientation display
        setCameraDisplayOrientation((Activity) mContext, mode);

        // Should release before use new Preview for Recording Video mode
        CustomCamera.releaseMediaRecorder();

        CustomCamera.mMediaRecorder = new MediaRecorder();

        // Step 1: Unlock and set camera to MediaRecorder
        CustomCamera.mCamera.unlock();
        CustomCamera.mMediaRecorder.setCamera(CustomCamera.mCamera);

        // Step 2: Set sources
        CustomCamera.mMediaRecorder.setAudioSource(MediaRecorder.AudioSource.CAMCORDER);
        CustomCamera.mMediaRecorder.setVideoSource(MediaRecorder.VideoSource.CAMERA);

        // Step 3: Set a CamcorderProfile (requires API Level 8 or higher)
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
                // Step 3: Set output format and encoding (for versions prior to API Level 8)
                if (CamcorderProfile.hasProfile(current_camera_id, CamcorderProfile.QUALITY_HIGH)) {
                    CamcorderProfile camcorderProfile = CamcorderProfile.get(
                            current_camera_id, CamcorderProfile.QUALITY_HIGH);
                    CustomCamera.mMediaRecorder.setProfile(camcorderProfile);
                } else
                    CustomCamera.mMediaRecorder.setProfile(
                            CamcorderProfile.get(current_camera_id, CamcorderProfile.QUALITY_LOW));
            } else
                CustomCamera.mMediaRecorder.setProfile(
                        CamcorderProfile.get(current_camera_id, CamcorderProfile.QUALITY_LOW));
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Step 4: Set output file - pass False value to indicate choose Video mode
        RECORDED_FILE_PATH = getOutputMediaFile(mContext, false).toString();
        CustomCamera.mMediaRecorder.setOutputFile(RECORDED_FILE_PATH);

        // Step 5: Set the preview output
        /**
         * Define Orientation of image in here,
         * if in portrait mode, use value = 90,
         * if in landscape mode, use value = 0
         */
        if (current_camera_id == com.eowise.recyclerview.stickyheaders.samples.define.Camera.CAMERA_BACK)
            // Back Camera
            CustomCamera.mMediaRecorder = CamUtils.rotateBackVideo(CustomCamera.mMediaRecorder);
        else
            // Front Camera
            CustomCamera.mMediaRecorder = CamUtils.rotateFrontVideo(CustomCamera.mMediaRecorder);

        // Set preview display to refresh current screen
        CustomCamera.mMediaRecorder.setPreviewDisplay(mCameraPreview.getHolder().getSurface());

        // Step 6: Prepare configured MediaRecorder
        try {
            CustomCamera.mMediaRecorder.prepare();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            CustomCamera.releaseMediaRecorder();
            return false;
        } catch (IOException e) {
            e.printStackTrace();
            CustomCamera.releaseMediaRecorder();
            return false;
        }
        return true;
    }

    public static void refreshCameraPreview(Activity activity, int current_camera_id) {
        if (CustomCamera.mCamera != null) {
            // Stop preview
            CustomCamera.mCamera.stopPreview();

            //NB: if you don't release the current camera before switching, you app will crash
            CustomCamera.releaseCamera();

            // New setting for Camera
            // Should initialize new Camera after released
            CustomCamera.mCamera = CustomCamera.getCameraInstance(current_camera_id);

            // Set Camera Display Orientation // Use taken photo for uploading
            // Need finish activity after set again single ton
            // Set File Path into single ton way
            // After selected files in Folder page,
            // begin upload after closed Activity Custom Gallery
            // getActivity().finish();

            // transfer selected File Path to receiver
            // transfer Array List had selected files inside to Enterprise activity
            Intent mIntent = new Intent(Receiver.ACTION_CHOSE_SINGLE_FILE);

            // Put object : Array list into

            setCameraDisplayOrientation(activity, current_camera_id);

            try {
                // Set preview display to refresh current face, use when change camera mode
                CustomCamera.mCamera.setPreviewDisplay(mCameraPreview.getHolder());
            } catch (Exception e) {
                e.printStackTrace();
            }

            try {
                CustomCamera.mCamera.startPreview();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    static String mFlashMode;

    // for the Preview - from http://developer.android.com/reference/android/hardware/Camera.html#setDisplayOrientation(int)
    // note, if orientation is locked to landscape this is only called when setting up the activity, and will always have the same orientation
    public static void setCameraDisplayOrientation(Activity activity, int camera_id) {
        CustomCamera.mCameraInfo = new Camera.CameraInfo();
        Camera.getCameraInfo(camera_id, CustomCamera.mCameraInfo);
        int rotation = activity.getWindowManager().getDefaultDisplay().getRotation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }

        int result;
        if (CustomCamera.mCameraInfo.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (CustomCamera.mCameraInfo.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {
            result = (CustomCamera.mCameraInfo.orientation - degrees + 360) % 360;
        }

        // On Sony Device, has issue : Black screen was shown on it
        if (CustomCamera.mCamera != null) {
            CustomCamera.mCamera.setDisplayOrientation(result);
            Log.e("allset", "true" + result);
        }


    }

    public static void stopAndRelaseRecordingVideo() {
        try {
            if (CustomCamera.mMediaRecorder != null) {
                // stop recording and release camera
                // stop the recording
                CustomCamera.mMediaRecorder.stop();

                // release the MediaRecorder object
                CustomCamera.releaseMediaRecorder();
            }

            if (CustomCamera.mCamera != null) {
                // take camera access back from MediaRecorder
                CustomCamera.mCamera.lock();

                // inform the user that recording has stopped
                IS_RECORDING_VIDEO = false;

                // Stop the preview before transfer to Review page
                CustomCamera.mCamera.stopPreview();

                // After stop recording video, need reset time value
                mCdt.cancel();
                minute = 0;
                second = 0;
            }
        } catch (Exception ex) {

        }
    }

    /**
     * Basic methods
     */

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.ibtn_close) {
            // Close current activity
            getActivity().finish();
        } else if (view.getId() == R.id.ibtn_take_photo_or_record_video) {
            /**
             * Need check currently user choose Take Photo mode or Record Video mode.
             * Depend on which mode, use Action
             * - Take photo
             * - Record video
             * relatively.
             */
            if (IS_PHOTO_MODE_OR_VIDEO_MODE) {
                /**
                 * Take photo mode
                 */
                // Begin Take picture
                try {
                    if (getActivity().getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA_AUTOFOCUS)) {
                        CustomCamera.mCamera.autoFocus(new Camera.AutoFocusCallback() {
                            @Override
                            public void onAutoFocus(boolean success, Camera camera) {
                                CustomCamera.mCamera.takePicture(null, null, mPhoto);
                            }
                        });
                    } else {
                        CustomCamera.mCamera.takePicture(null, null, mPhoto);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                /**
                 * Record video mode
                 */
                // Begin Record video

                // Configure MediaRecorder
                if (IS_RECORDING_VIDEO) {
                    try {
                        // stop recording and release camera
                        stopAndRelaseRecordingVideo();

                        // Transfer to Review page to see Recording video at there
                        // Send the file path also
                        ((FragmentActivity) getActivity())
                                .getSupportFragmentManager().beginTransaction()
                                .addToBackStack(null)
                                .replace(R.id.fl_custom_camera,
                                        CameraReviewFragment.newInstance(RECORDED_FILE_PATH))
                                .commitAllowingStateLoss();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    // initialize video camera with Back mode or Front mode
                    if (prepareVideoRecorder(getActivity(), current_camera_id)) {
                        // Camera is available and unlocked, MediaRecorder is prepared,
                        // now you can start recording
                        CustomCamera.mMediaRecorder.start();

                        // inform the user that recording has started
                        IS_RECORDING_VIDEO = true;

                        // Begin set Recording time in here
                        //start the countDown
                        mCdt.start();

                        // hide the other views while recording video
                        mIbtnClose.setVisibility(View.INVISIBLE);
                        mIbtnSwitchFrontOrBackCamera.setVisibility(View.INVISIBLE);

                        mIbtnSwitchTakePhotoOrRecordVideo.setVisibility(View.INVISIBLE);

                        mIbtnTakePhotoOrRecordVideo.setImageResource(R.drawable.ic_launcher);

                        // Disable for a while
                        mIbtnTakePhotoOrRecordVideo.setEnabled(false);
                    } else {
                        // prepare didn't work, release the camera
                        CustomCamera.releaseMediaRecorder();
                    }
                }
            }
        } else if (view.getId() == R.id.ibtn_switch_take_photo_or_record_video) {
            // Show Take photo or Record video correctly
            switchPhotoOrVideoMode();
        } else if (view.getId() == R.id.ibtn_switch_back_or_front_camera) {
            /**
             * Should reset camera for camera get new setting
             */

            // Switch between Front Camera & Back Camera
            current_camera_id = switchCurrentCameraID();

            refreshCameraPreview(getActivity(), current_camera_id);

            // Should remove view parent before add child
            CamUtils.removeViewParent(mCameraPreview);

            /**
             * Add Camera Preview into Layout
             */
            // Create our Preview view and set it as the content of our activity.
            mCameraPreview = new CustomCameraPreview(getActivity(), CustomCamera.mCamera);
            mFlCameraPreview.addView(mCameraPreview);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        pictureFile =
                getOutputMediaFile(getActivity(), true);

        // Define first time go to this page, reset after destroy thi page
        IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE = true;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = getLayoutInflater(savedInstanceState).inflate(
                R.layout.fragment_camera_preview, container, false);

        // Initial views
        initialViews(v);
        initialData();

        // Should remove view parent before add child
        if (IS_ALREADY_ADDED_CAMERA_REVIEW_INSIDE) {
            CamUtils.removeViewParent(mCameraPreview);
        }

        /**
         * Add Camera Preview into Layout
         */
        // Create our Preview view and set it as the content of our activity.
        mCameraPreview = new CustomCameraPreview(getActivity(), CustomCamera.mCamera);
        mFlCameraPreview.addView(mCameraPreview);

        // This boolean detail that in the next time include Camera Preview into layout
        // Should remove current Camera Preview before add new one
        IS_ALREADY_ADDED_CAMERA_REVIEW_INSIDE = true;

        // Reset Camera every time go Back to current page from the other pages
        resetCamera();
        CustomCamera.camera.setCropModeOrFullMode(true);
        // Switch Crop mode | Full mode
        switchCropModeOrFullMode();

        /**
         * Should show views correctly
         * - First time go to this page with default mode is Photo
         * - After back from Review page with previous mode after chose
         */
        switchPhotoOrVideoMode();

        /**
         * Check current device has any camera,
         * if size = 2, need show Switch camera button.
         * otherwise, hide Switch camera button.
         */
        if (CustomCamera.mCamera.getNumberOfCameras() == 2)
            // Show switch camera button
            mIbtnSwitchFrontOrBackCamera.setVisibility(View.VISIBLE);
        else
            // Hide switch camera button
            mIbtnSwitchFrontOrBackCamera.setVisibility(View.INVISIBLE);

            return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Reset
        IS_ALREADY_ADDED_CAMERA_REVIEW_INSIDE = false;

        IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE = false;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        // Set listener
        mIbtnClose.setOnClickListener(this);

        mIbtnSwitchFrontOrBackCamera.setOnClickListener(this);
        mIbtnSwitchTakePhotoOrRecordVideo.setOnClickListener(this);
        mIbtnTakePhotoOrRecordVideo.setOnClickListener(this);
    }

    private void initialViews(View v) {
        mFlCameraPreview = (FrameLayout) v.findViewById(R.id.fl_camera_preview);
        mFlSwitchFrontOrBackCamera = (LinearLayout) v.findViewById(R.id.fl_take_photo_or_record_video);
        mFlTakePhotoOrRecordVideo = (FrameLayout) v.findViewById(R.id.fl_switch_camera_font_or_back);

        mIbtnClose = (ImageButton) v.findViewById(R.id.ibtn_close);

        mIbtnSwitchFrontOrBackCamera = (ImageButton) v.findViewById(
                R.id.ibtn_switch_back_or_front_camera);
        mIbtnSwitchTakePhotoOrRecordVideo = (ImageButton) v.findViewById(
                R.id.ibtn_switch_take_photo_or_record_video);
        mIbtnTakePhotoOrRecordVideo = (ImageButton) v.findViewById(
                R.id.ibtn_take_photo_or_record_video);
        mLlElapseTime = (LinearLayout) v.findViewById(
                R.id.ll_elapse_time);
        mTvElapseTime = (TextView) v.findViewById(
                R.id.tv_elapse_time);

//frame layout reference
        frame1 = (FrameLayout) v.findViewById(R.id.frame1);
        frame2 = (FrameLayout) v.findViewById(R.id.frame2);
        frame3 = (FrameLayout) v.findViewById(R.id.frame3);
        frame4 = (FrameLayout) v.findViewById(R.id.frame4);

        //camera selection images
        image1 = (ImageView) v.findViewById(R.id.image1);
        image2 = (ImageView) v.findViewById(R.id.image2);
        image3 = (ImageView) v.findViewById(R.id.image3);
        image4 = (ImageView) v.findViewById(R.id.image4);

        //add icons
        add1 = (ImageButton) v.findViewById(R.id.add1);
        add2 = (ImageButton) v.findViewById(R.id.add2);
        add3 = (ImageButton) v.findViewById(R.id.add3);
        add4 = (ImageButton) v.findViewById(R.id.add4);


        //getting height
        DisplayMetrics displayMetrics2 = getResources().getDisplayMetrics();

        int height = (displayMetrics2.heightPixels);

        DisplayMetrics displayMetrics = getResources().getDisplayMetrics();

        int heightwidth = (displayMetrics.widthPixels) / 4;
        FrameLayout.LayoutParams layoutParams3 = new FrameLayout.LayoutParams(heightwidth - 20, heightwidth - 20);
        //images layout
        image1.setLayoutParams(layoutParams3);
        image2.setLayoutParams(layoutParams3);
        image3.setLayoutParams(layoutParams3);
        image4.setLayoutParams(layoutParams3);
        //gallery listener

        gallery = (ImageView) v.findViewById(R.id.showimg);

        //calculate gallery imageview height and width
        int galleryimghw = ((heightwidth * 50) / 100);
        //setting galery image width and height
        RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(galleryimghw, galleryimghw);
        layoutParams.addRule(RelativeLayout.ALIGN_PARENT_LEFT, RelativeLayout.TRUE);
        layoutParams.setMargins(galleryimghw / 2, 0, 0, 0);
        gallery.setLayoutParams(layoutParams);

        //gallery imagepic
        gallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK,
                        MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                // Start the Intent
                startActivityForResult(galleryIntent, GALLERY_INTENT_CALLED);
            }
        });

        //cancel button
        cancel1 = (ImageButton) v.findViewById(R.id.cancel1);
        cancel2 = (ImageButton) v.findViewById(R.id.cancel2);
        cancel3 = (ImageButton) v.findViewById(R.id.cancel3);
        cancel4 = (ImageButton) v.findViewById(R.id.cancel4);

        //camera_splash
        camera_flash = (ImageButton) v.findViewById(R.id.camera_flash);
        camera_flash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                flashMode = (flashMode + 1) % 3;

                Camera.Parameters param = CustomCamera.mCamera.getParameters();
                String str;
                switch (flashMode) {
                    case 0:
                        str = Camera.Parameters.FLASH_MODE_AUTO;
                        break;
                    case 1:
                        str = Camera.Parameters.FLASH_MODE_ON;
                        break;
                    default:
                        str = Camera.Parameters.FLASH_MODE_OFF;
                        break;
                }
                try {
                    param.setFlashMode(str);
                    CustomCamera.mCamera.setParameters(param);
                    updateFlashButton(flashMode);
                } catch (Exception ex) {

                }
            }
        });
//cancel1 events
        cancel1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraReviewFragment.urls.remove("1");
                image1.setImageDrawable(null);
                cancel1.setVisibility(View.GONE);
                frame1.setBackgroundResource(R.drawable.rounded_corners_image);
                add1.setVisibility(View.VISIBLE);
            }
        });
//cancel2 events

        cancel2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraReviewFragment.urls.remove("2");
                image2.setImageDrawable(null);
                cancel2.setVisibility(View.GONE);
                frame2.setBackgroundResource(R.drawable.rounded_corners_image);
                add2.setVisibility(View.VISIBLE);
            }

        });
        //cancel3 events

        cancel3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraReviewFragment.urls.remove("3");
                image3.setImageDrawable(null);
                cancel3.setVisibility(View.GONE);
                frame3.setBackgroundResource(R.drawable.rounded_corners_image);
                add3.setVisibility(View.VISIBLE);
            }

        });
        //cancel4 events

        cancel4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CameraReviewFragment.urls.remove("4");
                image4.setImageDrawable(null);
                cancel4.setVisibility(View.GONE);
                frame4.setBackgroundResource(R.drawable.rounded_corners_image);
                add4.setVisibility(View.VISIBLE);
            }

        });
//done button
        done = (ImageButton) v.findViewById(R.id.done);
        done.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // TODO Auto-generated method stub
                if (CameraReviewFragment.urls.size() > 0) {
                    Intent selectcategory = new Intent(getActivity(), SelectCategoryActivity.class);
                    startActivity(selectcategory);
                } else {
                    Toast.makeText(getContext(), "select atleast one image", Toast.LENGTH_SHORT).show();
                }
                //finish();
            }
        });
        //play sound
        soundPool = new SoundPool(1, AudioManager.STREAM_NOTIFICATION, 0);
        shutterSound = soundPool.load(getActivity(), R.raw.camera_shutter, 0);
        new Myloader().execute();
    }

    private void updateFlashButton(int flashMode) {


        switch (flashMode) {
            case 0:

                camera_flash.setImageResource(R.drawable.flash_on);
                break;
            case 1:

                camera_flash.setImageResource(R.drawable.flash_on);

                break;
            default:

                camera_flash.setImageResource(R.drawable.flash_off);

                break;
        }
    }

    private void resetCamera() {
        if (CustomCamera.mCamera == null) {
//            int current_camera_id = switchCurrentCameraID();
            CustomCamera.mCamera = CustomCamera.getCameraInstance(current_camera_id);
        }
    }

    private void switchCropModeOrFullMode() {
        /**
         * In the first time go to Custom Camera, should do these things:
         * - Select Full Mode (boolean = false).
         * - After check Single ton way, use Crop Mode or Full Mode follow boolean value
         * (Crop mode : true, Full mode : false)
         */
        if (CustomCamera.camera.isCropModeOrFullMode()) {
            /**
             * Crop mode
             */


            // Pixels
            // Utils.getSizeOfScreen(getActivity())[1] : 1080
            // Utils.getSizeOfScreen(getActivity())[0] : 720

            // Need convert from pixels to dp to set for Layout Params
            int height_screen =
                    CamUtils.getSizeOfScreen(getActivity())[1];
            int width_screen =
                    CamUtils.getSizeOfScreen(getActivity())[0];

            float height_bar = 100;//((float) height_screen) / 2 - ((float) width_screen) / 2;
            float height_controls = width_screen / 4;
            // Bottom bar
            LinearLayout.LayoutParams mLpFlSwitchFrontOrBackCamera =
                    new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, (int) (height_controls));
            mLpFlSwitchFrontOrBackCamera.gravity = Gravity.BOTTOM;


         /*   // Top bar
            FrameLayout.LayoutParams mLpTakePhotoOrRecordVideo =
                    new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, (int) (height_bar));
            mLpTakePhotoOrRecordVideo.gravity = Gravity.TOP;*/

            // Set params
            mFlSwitchFrontOrBackCamera.setLayoutParams(mLpFlSwitchFrontOrBackCamera);
            //  mFlTakePhotoOrRecordVideo.setLayoutParams(mLpTakePhotoOrRecordVideo);

            // The bar for cropping
            top_bar = height_bar;
        } else {
            /**
             * Full mode
             */

        }
    }

    private void switchPhotoOrVideoMode() {
        /**
         * Need check to show Crop icon in Photo mode
         * and not show Crop icon in Video mode
         */
        if (!IS_PHOTO_MODE_OR_VIDEO_MODE
                | IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE) {
            /**
             * Video mode
             */
            IS_FIRST_TIME_GO_TO_CAMERA_PREVIEW_PAGE = false;

            // Switch from Video mode to Photo mode
            IS_PHOTO_MODE_OR_VIDEO_MODE = true;


            mLlElapseTime.setVisibility(View.INVISIBLE);


            if (CustomCamera.camera.isCropModeOrFullMode()) {
                // Set Crop Bar background is black also : Top Black bar & Bottom black bar

            }
        } else {
            /**
             * Photo mode
             */

            // Switch to Video mode from Photo mode
            IS_PHOTO_MODE_OR_VIDEO_MODE = false;

            mIbtnSwitchTakePhotoOrRecordVideo.setImageResource(
                    R.drawable.ic_launcher);
            mIbtnTakePhotoOrRecordVideo.setImageResource(R.drawable.ic_launcher);

            mLlElapseTime.setVisibility(View.VISIBLE);
            // Show crop icon


        }
    }

    @Override
    public void onResume() {
        super.onResume();
        //to check flash availabe or not
        try {
            Camera.Parameters parameters = CustomCamera.mCamera.getParameters();
            List<String> flashModes = parameters.getSupportedFlashModes();
            if (flashModes != null && flashModes.size() > 0) {

                camera_flash.setVisibility(View.VISIBLE);
            } else {
                camera_flash.setVisibility(View.INVISIBLE);
            }
           /* if (parameters.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            }*/
            // Lock in the changes
            CustomCamera.mCamera.setParameters(parameters);
        }
        catch (Exception ex)
        {

        }
        updateImages();

    }

    class DecondeFile extends AsyncTask<String, Void, Bitmap> {
        int pos;

        public DecondeFile(int pos) {
            this.pos = pos;
        }


        @Override
        protected Bitmap doInBackground(String... urls) {
            return BitmapFactory.decodeFile(urls[0]);

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null)
                setImage(bitmap, pos);

        }
    }

    class CompressGalleryPic extends AsyncTask<String, Void, Bitmap> {
        int pos;

        public CompressGalleryPic(int pos) {
            this.pos = pos;
        }


        @Override
        protected Bitmap doInBackground(String... urls) {
            try {
                final File capturedImageFile = new File(getTempDirectoryPath(), System.currentTimeMillis() + ".jpg");
                Bitmap bm = CompressImage.compressImage(urls[0]);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(capturedImageFile));
                final String url = capturedImageFile.getAbsolutePath().toString();
                if (CameraReviewFragment.urls.get(pos + "") == null) {
                    CameraData cd = new CameraData();
                    cd.setFilename("lnd" + System.currentTimeMillis() + ".jpg");
                    cd.setImageurl(url);
                    CameraReviewFragment.urls.put(pos + "", cd);
                }
                return bm;
            } catch (Exception ex) {

            }
            return null;

        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null)
                setImage(bitmap, pos);

        }
    }

    private String getTempDirectoryPath() {
        File cache = null;

        // SD Card Mounted
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cache = new File(Environment.getExternalStorageDirectory().getAbsolutePath() +
                    "/Android/data/" + getActivity().getPackageName() + "/cache/");
        }
        // Use internal storage
        else {
            cache = getActivity().getCacheDir();
        }

        // Create the cache directory if it doesn't exist
        cache.mkdirs();
        return cache.getAbsolutePath();
    }

    private void updateImages() {
        updateFlashButton(flashMode);
        if (CameraReviewFragment.urls.get("1") != null) {
            new DecondeFile(1).execute(CameraReviewFragment.urls.get("1").getImageurl());

        }
        if (CameraReviewFragment.urls.get("2") != null) {
            new DecondeFile(2).execute(CameraReviewFragment.urls.get("2").getImageurl());

        }
        if (CameraReviewFragment.urls.get("3") != null) {
            new DecondeFile(3).execute(CameraReviewFragment.urls.get("3").getImageurl());
        }
        if (CameraReviewFragment.urls.get("4") != null) {
            new DecondeFile(4).execute(CameraReviewFragment.urls.get("4").getImageurl());
        }

    }

    private void setImage(Bitmap capturedImage, int pos) {
        if (pos == 1) {
            // filename.add(0, "lnd" + System.currentTimeMillis() + ".jpg");
            // imageurl.add(0, url);
            image1.setImageBitmap(capturedImage);
            frame1.setBackgroundResource(R.drawable.rounded_corners_image_selected);
            cancel1.setVisibility(View.VISIBLE);
            add1.setVisibility(View.GONE);

        } else if (pos == 2) {
            //filename.add(1, "lnd" + System.currentTimeMillis() + ".jpg");

            //  imageurl.add(1, url);
            image2.setImageBitmap(capturedImage);
            frame2.setBackgroundResource(R.drawable.rounded_corners_image_selected);
            cancel2.setVisibility(View.VISIBLE);
            add2.setVisibility(View.GONE);

        } else if (pos == 3) {
            //  filename.add(2, "lnd" + System.currentTimeMillis() + ".jpgBitmap myBitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());");

            //  imageurl.add(2, url);
            image3.setImageBitmap(capturedImage);
            frame3.setBackgroundResource(R.drawable.rounded_corners_image_selected);
            cancel3.setVisibility(View.VISIBLE);
            add3.setVisibility(View.GONE);
        } else if (pos == 4) {
            //filename.add(3, "lnd" + System.currentTimeMillis() + ".jpg");

            // imageurl.add(3, url);
            image4.setImageBitmap(capturedImage);
            frame4.setBackgroundResource(R.drawable.rounded_corners_image_selected);
            cancel4.setVisibility(View.VISIBLE);
            add4.setVisibility(View.GONE);
        }
    }

    class Myloader extends AsyncTask<Void, Void, Bitmap> {

        @Override
        protected Bitmap doInBackground(Void... voids) {
            try {
                int count;
                String path;
                final String[] columns = {MediaStore.Images.Media.DATA, MediaStore.Images.Media._ID};
                final String orderBy = MediaStore.Images.Media._ID;
                Cursor imagecursor = getActivity().managedQuery(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, columns, null, null, orderBy);
                int image_column_index = imagecursor.getColumnIndex(MediaStore.Images.Media._ID);
                count = imagecursor.getCount();
                if (count > 0) {
                    imagecursor.moveToPosition(0);
                    int dataColumnIndex = imagecursor.getColumnIndex(MediaStore.Images.Media.DATA);
                    path = imagecursor.getString(dataColumnIndex);
                    return ImageConvertor.getRoundedCornerBitmap(CompressImage.compressImage(path), 25);

                }
            } catch (Exception ex) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            if (bitmap != null)
                gallery.setImageBitmap(bitmap);
        }


    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        //   super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == GALLERY_INTENT_CALLED && resultCode == getActivity().RESULT_OK) {
            try {
                Uri selectedImageuri = data.getData();
                String[] filePathColumn = {MediaStore.Images.Media.DATA};

                // Get the cursor
                Cursor cursor = getActivity().getContentResolver().query(selectedImageuri,
                        filePathColumn, null, null, null);
                // Move to first row
                cursor.moveToFirst();

                int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                String imgDecodableString = cursor.getString(columnIndex);
                cursor.close();

                // Set image bitmap
                final File capturedImageFile = new File(getTempDirectoryPath(), System.currentTimeMillis() + ".jpg");
                Bitmap bm = CompressImage.compressImage(imgDecodableString);
                bm.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(capturedImageFile));
                imgDecodableString = capturedImageFile.getAbsolutePath().toString();
                //  gallery.setImageBitmap(bm);
                if (CameraReviewFragment.urls.get("1") == null) {
                    new CompressGalleryPic(1).execute(imgDecodableString);
                } else if (CameraReviewFragment.urls.get("2") == null) {
                    new CompressGalleryPic(2).execute(imgDecodableString);

                } else if (CameraReviewFragment.urls.get("3") == null) {
                    new CompressGalleryPic(3).execute(imgDecodableString);


                } else if (CameraReviewFragment.urls.get("4") == null) {
                    new CompressGalleryPic(4).execute(imgDecodableString);

                }
                //firs t image
                //ByteArrayOutputStream stream = new ByteArrayOutputStream();
                // Must compress the Image to reduce image size to make upload easy
                //selectedImage.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                // byte[] byte_arr = stream.toByteArray();
                // Encode Image to String
                //  imageurl = Base64.encodeToString(byte_arr, 0);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

    }
}

