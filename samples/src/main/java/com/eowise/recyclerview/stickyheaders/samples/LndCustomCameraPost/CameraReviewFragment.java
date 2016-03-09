package com.eowise.recyclerview.stickyheaders.samples.LndCustomCameraPost;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.Toast;
import android.widget.VideoView;

import com.eowise.recyclerview.stickyheaders.samples.R;
import com.eowise.recyclerview.stickyheaders.samples.Utils.CamUtils;
import com.eowise.recyclerview.stickyheaders.samples.data.CameraData;
import com.eowise.recyclerview.stickyheaders.samples.define.MediaType;

import java.io.File;
import java.io.FileOutputStream;
import java.util.HashMap;


public class CameraReviewFragment extends Fragment
        implements View.OnClickListener {
    /**
     * Data section
     */

    /**
     * The others methods
     */

    /**
     * Others section
     */
    public static ImageButton mBtnUse;

    /**
     * String section
     */
    private static String FILE_PATH;
//    private TextureVideoView mVvVideo;
    /**
     * View section
     */
    private ImageButton mBtnRetake;
    private ImageView mIvPhoto;
    private VideoView mVvVideo;
    public static HashMap<String, CameraData> urls = new HashMap<>();
    String url = "";

    /**
     * Listener section
     */

    public static Fragment newInstance(String filePath) {
        FILE_PATH = filePath;

        CameraReviewFragment fragment = new CameraReviewFragment();
        return fragment;
    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.btn_retake) {
            // Transfer to Camera Preview page by re-using fragment from Back Stack
            CamUtils.clearOldBackStack(getActivity());
        } else if (view.getId() == R.id.btn_use) {
            // Use taken photo for uploading
            // Need finish activity after set again single ton
            // Set File Path into single ton way
            // After selected files in Folder page,
            // begin upload after closed Activity Custom Gallery
            // getActivity().finish();

            // transfer selected File Path to receiver
            // transfer Array List had selected files inside to Enterprise activity
            // Intent mIntent = new Intent(Receiver.ACTION_CHOSE_SINGLE_FILE);

            // Put object : Array list into
            //  mIntent.putExtra(Receiver.EXTRAS_FILE_PATH, CustomCamera.camera.getFilePath());
            //  mIntent.putExtra(Receiver.EXTRAS_CASE_RECEIVER, CustomCamera.case_camera);

            // Send broadcast
            // getActivity().sendBroadcast(mIntent);

            CameraData cd=new CameraData();
            if (urls.get("1") == null) {
                 cd.setFilename("lnd" + System.currentTimeMillis() + ".jpg");
                 cd.setImageurl(url);
                urls.put("1", cd);
            } else if (urls.get("2") == null) {
                cd.setFilename("lnd" + System.currentTimeMillis() + ".jpg");
                cd.setImageurl(url);

                urls.put("2", cd);
            } else if (urls.get("3") == null) {
                cd.setFilename("lnd" + System.currentTimeMillis() + ".jpg");
                cd.setImageurl(url);

                urls.put("3",cd);
            } else if (urls.get("4") == null) {
                cd.setFilename("lnd" + System.currentTimeMillis() + ".jpg");
                cd.setImageurl(url);

                urls.put("4", cd);
            }
            CamUtils.clearOldBackStack(getActivity());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        /**
         * Set Orientation for page
         */
//        getActivity().setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        View v = getLayoutInflater(savedInstanceState).inflate(
                R.layout.fragment_camera_review, container, false);

        // Initial views
        initialViews(v);
        initialData();

        return v;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        // Back to previous mode already chose : Photo mode or Video mode
        if (!CameraPreviewFragment.IS_PHOTO_MODE_OR_VIDEO_MODE)
            CameraPreviewFragment.IS_PHOTO_MODE_OR_VIDEO_MODE = true;
        else
            CameraPreviewFragment.IS_PHOTO_MODE_OR_VIDEO_MODE = false;
    }

    /**
     * Initialize methods
     */
    private void initialData() {
        // Set listener
        mBtnRetake.setOnClickListener(this);
        mBtnUse.setOnClickListener(this);

        // Set Review
        // Need check the taken file is photo or video to set correctly
        if (CamUtils.isPhotoOrVideo(FILE_PATH) == MediaType.PHOTO) {
            // Photo

            // Show Use Photo text


            // Hide Image View, Show Video View player
            mIvPhoto.setVisibility(View.VISIBLE);
            mVvVideo.setVisibility(View.GONE);

            /**
             * Show image on View correctly
            */
          if   (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
            {
                new LoadImage()
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            }
            else
            new LoadImage().execute();
}
 else if (CamUtils.isPhotoOrVideo(FILE_PATH) == MediaType.VIDEO) {
            // Video

            // Show Use VDO text


            // Hide Image View, Show Video View player
            mIvPhoto.setVisibility(View.GONE);
            mVvVideo.setVisibility(View.VISIBLE);

            /**
             * Set up video view
             */
            showVideoOnUI();
        }
    }

    private void initialViews(View v) {
        mBtnRetake = (ImageButton) v.findViewById(R.id.btn_retake);
        mBtnUse = (ImageButton) v.findViewById(R.id.btn_use);

        mIvPhoto = (ImageView) v.findViewById(R.id.iv_review_photo);
        mVvVideo = (VideoView) v.findViewById(R.id.vv_review_video);
    }
private class LoadImage extends AsyncTask<String,Void,Bitmap>
{

    @Override
    protected Bitmap doInBackground(String... strings) {
        try {
            FileOutputStream fos = new FileOutputStream(CameraPreviewFragment.pictureFile);
            fos.write(CameraPreviewFragment.imgdata);
            fos.close();

            return showPhotoOnUI(true);
        }
        catch(Exception ex)
        {
        }

        return null;
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
    if(bitmap!=null)
        mIvPhoto.setImageBitmap(bitmap);

    }
}
    private Bitmap showPhotoOnUI(boolean is_success) {
        try {
            Bitmap mBitmap = null;

            // Check OutOfMemory error happened or not.
            // If success, decode file normally
            // If fail, need put sampleSize when decoding
            if (is_success)
                mBitmap = BitmapFactory.decodeFile(FILE_PATH);
            else
                mBitmap = BitmapFactory.decodeFile(
                        FILE_PATH, CamUtils.getBitmapOptions());

            Bitmap mBitmapRotated = null;
            // Rotate Back photo only once in here
            mBitmapRotated = CamUtils.rotateBackImage(mBitmap);

            // Should overwrite the file inside sd card.
            //create a file to write bitmap data
            File mFile = new File(FILE_PATH);
            mFile.createNewFile();

            // Begin crop photo in here
            Bitmap bitmap = null;

            if (CustomCamera.camera.isCropModeOrFullMode()) {
                /**
                 * Crop mode
                 */
                // Width of bitmap after rotated
                int height = mBitmap.getHeight();
                // Height of bitmap after rotated
                int width = mBitmap.getWidth();

                float scaleImage = (float) height / (float) width;
                float heightBubble = ((float) CamUtils.getSizeOfScreen(getActivity())[1]) * scaleImage / 2 - ((float) CamUtils.getSizeOfScreen(getActivity())[0]) / 2;

                // eight of bitmap / width of screen
                //float scaled_rate = (float) (height) / (float) Utils.getSizeOfScreen(getActivity())[0];
                float scaled_rate = (float) (height) / (((float) CamUtils.getSizeOfScreen(getActivity())[0]) + 2 * heightBubble);

                // create matrix for the manipulation
                Matrix matrix = new Matrix();

                // Should use best resolution from camera
                // recreate the new Bitmap
                Bitmap resizedBitmap = null;

                /**
                 * Need check front or back image also to flip captured image
                 */
                float requiredwidth = 0.0f, requiredheight = 0.0f;
                if (mBitmapRotated.getWidth() < CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate)
                    requiredwidth = mBitmapRotated.getWidth();
                else if(mBitmapRotated.getWidth() > CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate)
                    requiredwidth = mBitmapRotated.getWidth();

                else

                    requiredwidth = CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate;

               /* //for height
                if(mBitmapRotated.getHeight()<CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate)
                    requiredheight=mBitmapRotated.getHeight();
                else
                    requiredheight=CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate;
*/
             //   Log.e("cropedwidth",(int)requiredwidth+"");
              //  Log.e("cropedheight", (int) (CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate)+"");
              //  Log.e("bitmapwidth",(int)mBitmapRotated.getWidth()+"");
              //  Log.e("bitmapheight", (int) mBitmapRotated.getHeight()+"");

                //  Log.e("orientation",CustomCamera.current_orientation+"");
                if (!CameraPreviewFragment.IS_BACK_CAMERA_OR_FRONT_CAMERA) {
                    // Back Camera
                    // create bitmap
                    switch (CustomCamera.current_orientation) {
                        case 0:


                            resizedBitmap = Bitmap.createBitmap(
                                    mBitmapRotated,
                                    // Define X, Y where to begin crop
                                    0, 100,
                                    (int)requiredwidth,
                                    (int) requiredwidth,
                                    matrix, true);

                            break;
                        case 90:
                            resizedBitmap = Bitmap.createBitmap(
                                    mBitmapRotated,
                                    // Define X, Y where to begin crop
                                    100, 0,
                                    (int) requiredwidth,
                                    (int) (CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate),
                                    matrix, true);
                            break;
                        case 180:
                            resizedBitmap = Bitmap.createBitmap(
                                    mBitmapRotated,
                                    // Define X, Y where to begin crop
                                    0, 100,
                                    (int) requiredwidth,
                                    (int) (CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate),
                                    matrix, true);
                            break;
                        case 270:
                            resizedBitmap = Bitmap.createBitmap(
                                    mBitmapRotated,
                                    // Define X, Y where to begin crop
                                    100, 0,
                                    (int) requiredwidth,
                                    (int) (CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate),
                                    matrix, true);
                            break;
                    }

                    // Rotate Back photo need again in here
                    bitmap = resizedBitmap;
                } else {
                    // Front Camera

                    // create bitmap
                    switch (CustomCamera.current_orientation) {
                        case 0:
                            resizedBitmap = Bitmap.createBitmap(
                                    mBitmapRotated,
                                    // Define X, Y where to begin crop
                                    0, 300,
                                    (int) requiredwidth,
                                    (int) requiredwidth,
                                    matrix, true);
                            break;
                        case 90:
                            resizedBitmap = Bitmap.createBitmap(
                                    mBitmapRotated,
                                    // Define X, Y where to begin crop
                                    0, 300,
                                    (int) requiredwidth,
                                    (int) (CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate),
                                    matrix, true);
                            break;
                        case 180:
                            resizedBitmap = Bitmap.createBitmap(
                                    mBitmapRotated,
                                    // Define X, Y where to begin crop
                                    0, 300,
                                    (int) requiredwidth,
                                    (int) (CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate),
                                    matrix, true);
                            break;
                        case 270:
                            resizedBitmap = Bitmap.createBitmap(
                                    mBitmapRotated,
                                    // Define X, Y where to begin crop
                                    0, 300,
                                    (int) requiredwidth,
                                    (int) (CamUtils.getSizeOfScreen(getActivity())[0] * scaled_rate),
                                    matrix, true);
                            break;
                    }

                    // Rotate Front photo need again in here
                    resizedBitmap = CamUtils.flipFrontImage(resizedBitmap);
                    bitmap = resizedBitmap;
                }

                // Set image bitmap
                final File capturedImageFile = new File(getTempDirectoryPath(), System.currentTimeMillis() +".jpg");
                bitmap.compress(Bitmap.CompressFormat.JPEG, 100, new FileOutputStream(capturedImageFile));
                url = capturedImageFile.getAbsolutePath().toString();
                return bitmap;
            } else {
                /**
                 * Full Screen mode
                 */

                // Set image on View
                if (!CameraPreviewFragment.IS_BACK_CAMERA_OR_FRONT_CAMERA) {
                    // Back Camera

                    // Rotate Front photo need again in here
                    bitmap = mBitmapRotated;
                    mIvPhoto.setImageBitmap(bitmap);
                } else {
                    // Front Camera

                    // Rotate Front photo need again in here
                    mBitmapRotated = CamUtils.rotateFrontImage(getActivity(), mBitmapRotated);

                    bitmap = mBitmapRotated;
                    mIvPhoto.setImageBitmap(bitmap);
                }
            }

           /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB)
                new SaveBitmapToFileAsync(getActivity(), bitmap, mFile)
                        .executeOnExecutor(AsyncTask.THREAD_POOL_EXECUTOR);
            else
                new SaveBitmapToFileAsync(getActivity(), bitmap, mFile).execute();*/
        } catch (OutOfMemoryError e) {
            e.printStackTrace();
            //   Toast.makeText(getActivity(),"called"+e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e("error", e.getMessage());

            showPhotoOnUI(false);
        } catch (Exception e) {
            e.printStackTrace();
            // Toast.makeText(getActivity(),"called"+e.getMessage(),Toast.LENGTH_SHORT).show();
            Log.e("error", e.getMessage());
        }
    return null;
    }

    private void showVideoOnUI() {
        File mFile = new File(FILE_PATH);

        // ex. FILE PATH = /mnt/sdcard/Pictures/Enterprise/VID_20150327_143555.mp4
        mVvVideo.setVideoPath(mFile.getAbsolutePath());

        // set play video view dialog details photo
        MediaController mMc = new MediaController(getActivity());
        mMc.setAnchorView(mVvVideo);
        mMc.setMediaPlayer(mVvVideo);

        mVvVideo.requestFocus();
        mVvVideo.setBackgroundColor(Color.WHITE);
        mVvVideo.setMediaController(mMc);
        mVvVideo.setZOrderOnTop(true);
        mVvVideo.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (mVvVideo.isPlaying()) {
                    mVvVideo.pause();

                    // Show full-screen button again
                    mVvVideo.setVisibility(View.VISIBLE);
                } else {
                    mVvVideo.start();
                }

                return false;
            }
        });

        if (!mVvVideo.isPlaying())
            mVvVideo.start();
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

}
