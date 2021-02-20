package com.tws.trevon.co;

import android.widget.ImageView;

import com.tws.trevon.common.Utilities;
import com.tws.trevon.constants.IConstants;

/**
 * Created by Chandra Prakash on 19-02-2015.
 */
public class RvImageCO
{
    public int position;
    public ImageView imageView;
    public String lowResolutionImageUrl;
    public String url;
    public int defaultImageRes = IConstants.EMPTY_RESOURCE_ID;
    public boolean shouldAnimateLoading = true;

    public RvImageCO(int position, ImageView imageView, String lowResolutionImageUrl, String url)
    {
        this.position = position;
        this.imageView = imageView;
        this.lowResolutionImageUrl = Utilities.getNotNullValue(lowResolutionImageUrl);
        this.url = Utilities.getNotNullValue(url);

        imageView.setTag(Utilities.getNotNullValue(lowResolutionImageUrl));
        imageView.setImageDrawable(null);
    }

    public RvImageCO(int position, ImageView imageView, String url, int defaultImageRes)
    {
        this(position, imageView, url, defaultImageRes, true);
    }

    public RvImageCO(int position, ImageView imageView, String url, int defaultImageRes, boolean shouldAnimateLoading)
    {
        this.position = position;
        this.imageView = imageView;
        this.url = Utilities.getNotNullValue(url);
        this.defaultImageRes = defaultImageRes;
        this.shouldAnimateLoading = shouldAnimateLoading;

        imageView.setTag(Utilities.getNotNullValue(url));
        /*imageView.setImageDrawable(null);*/

        if(defaultImageRes == IConstants.EMPTY_RESOURCE_ID)
        {
            imageView.setImageDrawable(null);
        }
        else
        {
            imageView.setImageResource(defaultImageRes);
        }
    }

    public boolean isCorrectIV()
    {
        if(url.equals(imageView.getTag().toString()))
        {
            return true;
        }
        else
        {
            if(defaultImageRes == IConstants.EMPTY_RESOURCE_ID)
            {
                imageView.setImageDrawable(null);
            }
            else
            {
                imageView.setImageResource(defaultImageRes);
            }

            return false;
        }
    }

    public boolean isCorrectIVForLowRes()
    {
        if(lowResolutionImageUrl.equals(imageView.getTag().toString()))
        {
            return true;
        }
        else
        {
            /*imageView.setImageDrawable(null);*/
            if(defaultImageRes == IConstants.EMPTY_RESOURCE_ID)
            {
                imageView.setImageDrawable(null);
            }
            else
            {
                imageView.setImageResource(defaultImageRes);
            }

            return false;
        }
    }
}
