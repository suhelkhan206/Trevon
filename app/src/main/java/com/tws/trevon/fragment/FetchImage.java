package com.tws.trevon.fragment;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import androidx.annotation.NonNull;
import androidx.exifinterface.media.ExifInterface;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;
import com.tws.trevon.BuildConfig;
import com.tws.trevon.R;
import com.tws.trevon.co.BottomMenuItemCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.FileUtils;
import com.tws.trevon.common.Utilities;
import com.tws.trevon.constants.IConstants;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * A simple {@link android.app.Fragment} subclass.
 */
public final class FetchImage extends Fragment
{
    private ImageSelectionListener mListener;

    private Uri mImageCaptureUri;
    private int colorTheme;
    private boolean showRemoveImageOption = true;

    private static boolean isProcessed = false;

    private static final int PICK_FROM_CAMERA = 1;
    private static final int PICK_FROM_FILE = 3;


    public FetchImage() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);

        isProcessed = false;
        colorTheme = getArguments().getInt(IConstants.COLOR_THEME);

        if(mListener == null)
        {
            isProcessed = true;
            AppUtilities.showUserMessageInDevMode("Skipping onCreate");
        }
        else
        {
            boolean isProceed = true;
            if(android.os.Build.VERSION.SDK_INT > 22)
            {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    //do nothing
                }
                else
                {
                    isProceed = false;
                    requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, IConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_CODE);
                }
            }

            if(isProceed)
            {
                captureImageInitialization();
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults)
    {
        /*super.onRequestPermissionsResult(requestCode, permissions, grantResults);*/

        if(IConstants.WRITE_EXTERNAL_STORAGE_PERMISSION_CODE == requestCode)
        {
            for(int i = 0; i < permissions.length; i++)
            {
                String permission = permissions[i];
                if(android.Manifest.permission.WRITE_EXTERNAL_STORAGE.equals(permission))
                {
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED)
                    {
                        break;
                    }
                }
            }
        }
    }

    private void captureImageInitialization()
    {
        isProcessed = true;
        ArrayList<BottomMenuItemCO> selectOptionList = new ArrayList<>();
        selectOptionList.add(new BottomMenuItemCO("0", IConstants.EMPTY_RESOURCE_ID, R.string.select_camera));
        selectOptionList.add(new BottomMenuItemCO("1", IConstants.EMPTY_RESOURCE_ID, R.string.select_from_gallery));

        if(showRemoveImageOption)
        {
            selectOptionList.add(new BottomMenuItemCO("2", IConstants.EMPTY_RESOURCE_ID, R.string.remove_photo));
        }

        final BottomSheetOptionsDialogFragment bottomOptionDialog = BottomSheetOptionsDialogFragment.newInstance(selectOptionList, IConstants.EMPTY_STRING, colorTheme);
        bottomOptionDialog.setOnOptionClickListener(new BottomSheetOptionsDialogFragment.OnOptionSelected() {
            @Override
            public void onOptionSelected(BottomMenuItemCO bottomMenuItemCO, int position)
            {
                switch(bottomMenuItemCO.code)
                {
                    case "0":

/*                    if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission_group.CAMERA) != PackageManager.PERMISSION_GRANTED)
                    {
                        AppUtilities.showUserMessage(R.string.runtime_permission_not_given, new Object[]{});
                    }*/
                        /**
                         * To take a photo from camera, pass intent action
                         * ‘MediaStore.ACTION_IMAGE_CAPTURE‘ to open the camera app.
                         */
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

                        /**
                         * Also specify the Uri to save the image on specified path
                         * and file name. Note that this Uri variable also used by
                         * gallery app to hold the selected image path.
                         */
                        /*mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_avatar_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));*/


                        if(android.os.Build.VERSION.SDK_INT > 20)
                        {
                            mImageCaptureUri = FileProvider.getUriForFile(getActivity(),
                                    BuildConfig.APPLICATION_ID + ".provider",
                                    new File(Environment.getExternalStorageDirectory(), "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                        }
                        else
                        {
                            mImageCaptureUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "tmp_" + String.valueOf(System.currentTimeMillis()) + ".jpg"));
                        }

                        intent.putExtra(MediaStore.EXTRA_OUTPUT,  mImageCaptureUri);

                        try
                        {
                            intent.putExtra("return-data", true);
                            startActivityForResult(intent, PICK_FROM_CAMERA);
                        }
                        catch (Exception e)
                        {
                            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                            CustomAlertDialog customAlertDialog = new CustomAlertDialog();
                            Bundle bundle = new Bundle();
                            bundle.putString(IConstants.DIALOG_TITLE, e.getMessage() + " " + e.getLocalizedMessage());
                            bundle.putString(IConstants.DESCRIPTION, Utilities.getStackTrace(e));
                            bundle.putBoolean(IConstants.BOOLEAN, true);
                            customAlertDialog.setArguments(bundle);
                            customAlertDialog.setOnDialogClickListener(new CustomAlertDialog.OnDialogFragmentListener() {
                                @Override
                                public void onPositiveAction() {

                                }

                                @Override
                                public void onNegativeAction() {

                                }
                            });
                            customAlertDialog.show(fragmentManager,"");

                            AppUtilities.showUserMessageInDevMode("Unexpected CAMERA error occurred.");
                        }
                        break;

                    case "1":
                        // pick from file
                        /**
                         * To select an image from existing files, use
                         * Intent.createChooser to open image chooser. Android will
                         * automatically display a list of supported applications,
                         * such as image gallery or file manager.
                         */

                        intent = new Intent();

                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        AppUtilities.showUserMessageInDevMode("ACTION_GET_CONTENT " + mListener);

                        startActivityForResult(Intent.createChooser(intent, "Complete action using"), PICK_FROM_FILE);
                        break;

                    case "2":
                        if(mListener != null)
                        {
                            mListener.onImageRemove();
                        }
                        else
                        {
                            AppUtilities.showUserMessageInDevMode("onImageRemove Listener is null");
                        }
                        break;
                }
            }
        });

        bottomOptionDialog.show(getActivity().getSupportFragmentManager(), bottomOptionDialog.getTag());
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        if (resultCode != getActivity().RESULT_OK)
        {
            AppUtilities.showUserMessageInDevMode("requestCode : " + requestCode + ", resultCode :" + resultCode);
            /*AppUtilities.showUserMessage("Camera app did not work as expected. Please try again.");*/
            return;
        }

        switch (requestCode)
        {
            case PICK_FROM_CAMERA:
                /**
                 * After taking a picture, do the crop
                 */
                processSelectedImage();
                Utilities.deleteFile(FileUtils.getFile(getActivity(), mImageCaptureUri));
                break;

            case PICK_FROM_FILE:
                /**
                 * After selecting image from files, save the selected path
                 */
                mImageCaptureUri = data.getData();
//                AppUtilities.showUserMessageInDevMode(Utilities.getNotNullValue(mImageCaptureUri));
                processSelectedImage();

                break;
        }
    }

    private void processSelectedImage()
    {
        Uri uri; // the URI you've received from the other app
        InputStream inputStream = null;
        int rotation = 0;

        try
        {
            inputStream = getActivity().getContentResolver().openInputStream(mImageCaptureUri);
            ExifInterface exifInterface = new ExifInterface(inputStream);
            // Now you can extract any Exif tag you want
            // Assuming the image is a JPEG or supported raw format

            int orientation = exifInterface.getAttributeInt(
                    ExifInterface.TAG_ORIENTATION,
                    ExifInterface.ORIENTATION_NORMAL);

            switch (orientation)
            {
                case ExifInterface.ORIENTATION_ROTATE_90:
                    rotation = 90;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_180:
                    rotation = 180;
                    break;
                case ExifInterface.ORIENTATION_ROTATE_270:
                    rotation = 270;
                    break;
            }
        }
        catch (IOException e)
        {
            // Handle any errors
        }
        finally
        {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException ignored) {}
            }
        }

        proceedWithSelectedImageUri(rotation);
        return;
    }

    private void proceedWithSelectedImageUri(final int rotation)
    {
        if(mImageCaptureUri == null)
        {
            return;
        }

        Bitmap photo = null;
        try
        {
            photo = MediaStore.Images.Media.getBitmap(AppController.getInstance().getContentResolver(), mImageCaptureUri);
            photo = AppUtilities.rotateImage(photo, rotation);
/*            populateFinalWidthHeight(photo);
            photo = Bitmap.createScaledBitmap(photo, imageWidth, imageHeight, false);*/
        }
        catch (IOException e)
        {
            AppUtilities.showUserMessageInDevMode(e.getMessage());
        }

        if(mListener == null || photo == null)
        {
            AppUtilities.showUserMessage("Unexpected error occurred, please try again.");
        }
        else
        {
            mListener.onImageSelected(photo);
        }
    }

    public interface ImageSelectionListener
    {
        void onImageSelected(final Bitmap photo);
        void onImageRemove();
    }

    public void setListener(ImageSelectionListener mListener) {
        this.mListener = mListener;
    }

    @Override
    public void onResume()
    {
        super.onResume();

        if(!isProcessed)
        {
            boolean isProceed = true;
            if(android.os.Build.VERSION.SDK_INT > 22)
            {
                if (ContextCompat.checkSelfPermission(getActivity(), android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
                {
                    //do nothing
                }
                else
                {
                    isProceed = false;
                }
            }

            if(isProceed)
            {
                captureImageInitialization();
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    public void setShowRemoveImageOption(boolean showRemoveImageOption) {
        this.showRemoveImageOption = showRemoveImageOption;
    }
}
