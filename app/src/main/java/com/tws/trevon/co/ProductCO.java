package com.tws.trevon.co;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

public class ProductCO implements Parcelable
{
    public String id;
    public String title;
    public String post_type;
    public String category_id;
    public String extra_id;
    public String slug;
    public String srt_description;
    public String description;
    public String regular_price;
    public String sell_price;
    public String mrp;
    public String image;
    public String stock_quantity;
    public String stock_id;
    public String rating_id;
    public String stock_status;
    public String retailer_max_purchase_qty = "0";
    public String on_deal;
    public String is_featured;
    public String extra_field;
    public String extra_field_1;
    public String extra_date;
    public String avg_rate;
    public String rating_count;
    public String total_sale;
    public String offers;
    public String status;
    public String created_by;
    public String created_date;
    public String modified_by;
    public String modified_date;
    public String selectedSize;
    public String SelectedSizeID;
    public String productFinalPrize;
    public String pincode_status;
    public String coupon_id;
    public String coupon_amount = "0";
    public String quantity = "0";
    public String is_wished = "0";
    public List<StockCO> stock;
    public List<RatngCO> rating;
    public String  wholesale_price;
    public String  retail_price;
    public String   piece_in_set;
    public String  min_purchase_qty;
    public String  product_purchase_type="";
    public int  position;
    public List<SliderCO> sliders;
    public List<ProductFilterCO> filter;


    protected ProductCO(Parcel in) {
        id = in.readString();
        title = in.readString();
        post_type = in.readString();
        category_id = in.readString();
        extra_id = in.readString();
        slug = in.readString();
        srt_description = in.readString();
        description = in.readString();
        regular_price = in.readString();
        sell_price = in.readString();
        mrp = in.readString();
        image = in.readString();
        stock_quantity = in.readString();
        stock_id = in.readString();
        rating_id = in.readString();
        stock_status = in.readString();
        retailer_max_purchase_qty = in.readString();
        on_deal = in.readString();
        is_featured = in.readString();
        extra_field = in.readString();
        extra_field_1 = in.readString();
        extra_date = in.readString();
        avg_rate = in.readString();
        rating_count = in.readString();
        total_sale = in.readString();
        offers = in.readString();
        status = in.readString();
        created_by = in.readString();
        created_date = in.readString();
        modified_by = in.readString();
        modified_date = in.readString();
        selectedSize = in.readString();
        SelectedSizeID = in.readString();
        productFinalPrize = in.readString();
        pincode_status = in.readString();
        coupon_id = in.readString();
        coupon_amount = in.readString();
        quantity = in.readString();
        is_wished = in.readString();
        stock = in.createTypedArrayList(StockCO.CREATOR);
        rating = in.createTypedArrayList(RatngCO.CREATOR);
        wholesale_price = in.readString();
        retail_price = in.readString();
        piece_in_set = in.readString();
        min_purchase_qty = in.readString();
        product_purchase_type = in.readString();
        position = in.readInt();
        sliders = in.createTypedArrayList(SliderCO.CREATOR);
        filter = in.createTypedArrayList(ProductFilterCO.CREATOR);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(title);
        dest.writeString(post_type);
        dest.writeString(category_id);
        dest.writeString(extra_id);
        dest.writeString(slug);
        dest.writeString(srt_description);
        dest.writeString(description);
        dest.writeString(regular_price);
        dest.writeString(sell_price);
        dest.writeString(mrp);
        dest.writeString(image);
        dest.writeString(stock_quantity);
        dest.writeString(stock_id);
        dest.writeString(rating_id);
        dest.writeString(stock_status);
        dest.writeString(retailer_max_purchase_qty);
        dest.writeString(on_deal);
        dest.writeString(is_featured);
        dest.writeString(extra_field);
        dest.writeString(extra_field_1);
        dest.writeString(extra_date);
        dest.writeString(avg_rate);
        dest.writeString(rating_count);
        dest.writeString(total_sale);
        dest.writeString(offers);
        dest.writeString(status);
        dest.writeString(created_by);
        dest.writeString(created_date);
        dest.writeString(modified_by);
        dest.writeString(modified_date);
        dest.writeString(selectedSize);
        dest.writeString(SelectedSizeID);
        dest.writeString(productFinalPrize);
        dest.writeString(pincode_status);
        dest.writeString(coupon_id);
        dest.writeString(coupon_amount);
        dest.writeString(quantity);
        dest.writeString(is_wished);
        dest.writeTypedList(stock);
        dest.writeTypedList(rating);
        dest.writeString(wholesale_price);
        dest.writeString(retail_price);
        dest.writeString(piece_in_set);
        dest.writeString(min_purchase_qty);
        dest.writeString(product_purchase_type);
        dest.writeInt(position);
        dest.writeTypedList(sliders);
        dest.writeTypedList(filter);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ProductCO> CREATOR = new Creator<ProductCO>() {
        @Override
        public ProductCO createFromParcel(Parcel in) {
            return new ProductCO(in);
        }

        @Override
        public ProductCO[] newArray(int size) {
            return new ProductCO[size];
        }
    };

    public float getPriceQuantity() {
        Integer qty = Integer.parseInt(quantity);
        float price = Float.parseFloat(sell_price);
        float orderTotal = qty * price;
        return orderTotal;
    }

    public ProductCO()
    {
        filter = new ArrayList<>();
    }


}
