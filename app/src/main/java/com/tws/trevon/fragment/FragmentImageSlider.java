package com.tws.trevon.fragment;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.tws.trevon.R;
import com.tws.trevon.co.ImageCO;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.TouchImageView;

import java.util.ArrayList;

import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

public class FragmentImageSlider extends DialogFragment {
    private int selectedPosition = 0;
    ArrayList<ImageCO> images;
    ArrayList<ImageCO> imagesAsk;
    ViewPager viewPager;
    TextView lblCount,lblTitle,lblDate;
    String isPdfShow;
    int type;
    private ImageView ivDownloadAttachment;

    public static FragmentImageSlider newInstance()
    {
        FragmentImageSlider f = new FragmentImageSlider();
        return f;
    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState)
    {
        // Inflate the layout for this fragment






        View rootView= inflater.inflate(R.layout.fragment_image_slider, container, false);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);


        viewPager = (ViewPager) rootView.findViewById(R.id.viewpager);
        lblCount = (TextView)rootView.findViewById(R.id.lbl_count);
        lblTitle = (TextView) rootView.findViewById(R.id.title);
        lblDate = (TextView) rootView.findViewById(R.id.date);
        ivDownloadAttachment = (ImageView) rootView.findViewById(R.id.ic_download_attachment);

        if (getArguments() != null)
        {
            type=getArguments().getInt("type");
            if(type==1)
            {
                imagesAsk= getArguments().getParcelableArrayList("images");
            }
            else
            {
                images  = getArguments().getParcelableArrayList("images");
            }

            selectedPosition = getArguments().getInt("position");
            isPdfShow = getArguments().getString("isPdfShow");
        }

        ImageAdapter myViewPagerAdapter = new ImageAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

        setCurrentItem(selectedPosition);

        ivDownloadAttachment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getActivity(),"Downloading",Toast.LENGTH_SHORT).show();
                AppUtilities.downloadFile(Uri.parse(images.get(selectedPosition).url), images.get(selectedPosition).url, getActivity());
            }
        });
        return rootView;
    }




    private void setCurrentItem(int position) {
        viewPager.setCurrentItem(position, false);
        displayMetaInfo(selectedPosition);
    }

    //	page change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            displayMetaInfo(position);
            selectedPosition = position;
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    private void displayMetaInfo(int position) {

        if(type==1)
        {
            lblCount.setText((position + 1) + " of " + imagesAsk.size());
            ImageCO imageAsk = imagesAsk.get(position);
            lblTitle.setText(imageAsk.url);
            lblDate.setVisibility(View.GONE);
        }
        else
        {
            lblCount.setText((position + 1) + " of " + images.size());
            ImageCO sclPostAttachmentsCO = images.get(position);
            lblTitle.setText(sclPostAttachmentsCO.url);
            //lblDate.setText(sclPostAttachmentsCO.createdDate);
        }


    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, R.style.AppTheme_FullScreenDialog);
    }


    public class ImageAdapter extends PagerAdapter {

        private LayoutInflater layoutInflater;

        public ImageAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, final int position) {

            layoutInflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.image_fullscreen_preview, container, false);

            final TouchImageView imageViewPreview = (TouchImageView) view.findViewById(R.id.image_preview);
            final LinearLayout view_pdf = view.findViewById(R.id.view_pdf);

            if(type==1)
            {
                final ImageCO imageAsk = imagesAsk.get(position);

                if(imageAsk.type.contains("image") )
                {
                    view_pdf.setVisibility(View.GONE);
                    Glide.with(getActivity())
                            .load(imageAsk.url)
                            .placeholder(R.drawable.error_images)
                            .error(R.drawable.error_images)
                            .into(imageViewPreview);
                    container.addView(view);
                }
               /* else if(isPdfShow.equals("yes") && imageAsk.type.toLowerCase().contains(IConstants.FILE_TYPE_PDF))
                {
                    view_pdf.setVisibility(View.VISIBLE);
                    GlideApp.with(getActivity())
                            .load(imageAsk.url)
                            .placeholder(R.drawable.error_images)
                            .error(R.drawable.error_images)
                            .into(imageViewPreview);
                    container.addView(view);
                }*/

               /* view_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(imageAsk.getFileType().toLowerCase().contains("pdf"))
                        {
                            AppUtilities.showPdfFromUrl(getActivity(),imageAsk.getServerUri());
                        }
                    }
                });*/
            }
            else
            {
                final ImageCO sclPostAttachmentsCO = images.get(position);

                if(sclPostAttachmentsCO.type.contains("image") ) {
                    view_pdf.setVisibility(View.GONE);
                    Glide.with(getActivity())
                            .load(sclPostAttachmentsCO.url)
                            .placeholder(R.drawable.error_images)
                            .error(R.drawable.error_images)
                            .into(imageViewPreview);
                    container.addView(view);
                }
               /* else if(isPdfShow.equals("yes") && sclPostAttachmentsCO.type.toLowerCase().contains(IConstants.FILE_TYPE_PDF))
                {
                    view_pdf.setVisibility(View.VISIBLE);
                    GlideApp.with(getActivity())
                            .load(sclPostAttachmentsCO.previewImage)
                            .placeholder(R.drawable.error_images)
                            .error(R.drawable.error_images)
                            .into(imageViewPreview);
                    container.addView(view);
                }

                view_pdf.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        if(sclPostAttachmentsCO.type.toLowerCase().contains("pdf"))
                        {
                            AppUtilities.showPdfFromUrl(getActivity(),images.get(position).url);
                        }
                    }
                });*/
            }










           /* GlideApp.with(getActivity()).load(sclPostAttachmentsCO.url)
                    .thumbnail(1f)
                    .diskCacheStrategy(DiskCacheStrategy.ALL)
                    .into(imageViewPreview);*/



           /* GlideApp.with(getActivity())
                    .asBitmap()
                    .load(sclPostAttachmentsCO.url)
                    .into(new CustomTarget<Bitmap>() {
                        @Override
                        public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                            imageViewPreview.setImageBitmap(resource);
                        }

                        @Override
                        public void onLoadCleared(@Nullable Drawable placeholder) {
                        }
                    });*/



            return view;
        }

        @Override
        public int getCount() {
            if(type==1)
            {
                return imagesAsk.size();
            }
            else
            {
                return images.size();
            }

        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == ((View) obj);
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView((View) object);
        }
    }

    public static void setWindowFlag(Activity activity, final int bits, boolean on) {
        Window win = activity.getWindow();
        WindowManager.LayoutParams winParams = win.getAttributes();
        if (on) {
            winParams.flags |= bits;
        } else {
            winParams.flags &= ~bits;
        }
        win.setAttributes(winParams);
    }
    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
        }
    }




}
