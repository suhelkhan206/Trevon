package com.tws.trevon.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.tws.trevon.R;
import com.tws.trevon.adapter.BuyingProductsAdapter;
import com.tws.trevon.co.AddCartCO;
import com.tws.trevon.co.ProductCO;
import com.tws.trevon.co.StockCO;
import com.tws.trevon.co.UserCO;
import com.tws.trevon.common.AppController;
import com.tws.trevon.common.AppUtilities;
import com.tws.trevon.common.AppValidate;
import com.tws.trevon.common.CallApi;
import com.tws.trevon.constants.API;
import com.tws.trevon.constants.IConstants;
import com.tws.trevon.constants.IReqParams;

import org.json.JSONObject;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

public class PopupBottomDailogFragment2 extends AbstractBottomSheetFragment
{
    public static final String TAG = "ActionBottomDialog";
    public RecyclerView product_buy_option_recycler_view;
    private ItemClickListener mListener;
    ProductCO productCO;
    BuyingProductsAdapter buyingProductsAdapter;
    TextView product_title, buying_option;
    TextView product_total_qty,product_total_price,product_price_with_tax;
    LinearLayout product_price_calculation;
    String total_quantity,position;
    LinearLayout buy_now;

    public static PopupBottomDailogFragment2 newInstance(String productCO, String total_quantity)
    {
        Bundle args = new Bundle();
        //args.putParcelableArrayList("productCO", productCO);
        args.putString("productCO", productCO);
        args.putString("total_quantity", total_quantity);

        PopupBottomDailogFragment2 fragment = new PopupBottomDailogFragment2();
        fragment.setArguments(args);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_popup_bottom_dailog, container, false);
    }
    @Override public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        view.findViewById(R.id.add_to_cart).setVisibility(View.GONE);
        buy_now = view.findViewById(R.id.buy_now);
        buy_now.setVisibility(View.VISIBLE);
        buy_now.setOnClickListener(this);
        product_buy_option_recycler_view = view.findViewById(R.id.product_buy_option_recycler_view);
        product_title = view.findViewById(R.id.product_title);
        buying_option = view.findViewById(R.id.buying_option);
        product_price_calculation = view.findViewById(R.id.product_price_calculation);
        product_total_qty = view.findViewById(R.id.product_total_qty);
        product_total_price = view.findViewById(R.id.product_total_price);
        product_price_with_tax = view.findViewById(R.id.product_price_with_tax);
        if (getArguments() != null)
        {
            // stockCOList = getArguments().getParcelableArrayList("stockCOList");
            String productCOString = getArguments().getString("productCO");
            total_quantity = getArguments().getString("total_quantity");
            productCO = AppController.gson.fromJson(productCOString, ProductCO.class);
            // Toast.makeText(getActivity(), ""+productCO.stock.size(), Toast.LENGTH_SHORT).show();
        }

        product_title.setText(productCO.title);
        buying_option.setText(productCO.stock.size()+ " buying option");
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        product_buy_option_recycler_view.setLayoutManager(layoutManager);
        buyingProductsAdapter = new BuyingProductsAdapter(getActivity(), productCO.stock, productCO);
        product_buy_option_recycler_view.setAdapter(buyingProductsAdapter);
        buyingProductsAdapter.stock = productCO.stock;
        buyingProductsAdapter.notifyDataSetChanged();
        priceCalculation();

        buyingProductsAdapter.setClickListener(new BuyingProductsAdapter.ClickListner() {
            @Override
            public void onIncreaseClick(View view, int position)
            {
                StockCO stockCO = productCO.stock.get(position);
                Integer quantity =  Integer.parseInt(stockCO.quantity);

                Integer total_quantityint =  Integer.parseInt(total_quantity);
                total_quantityint = total_quantityint +1;
                total_quantity = ""+total_quantityint;
                quantity= quantity + 1;
                stockCO.quantity = quantity+"";
                buyingProductsAdapter.notifyDataSetChanged();
                priceCalculation();
            }

            @Override
            public void onDecreaseClick(View view, int position)
            {
                StockCO stockCO = productCO.stock.get(position);

                Integer quantity =  Integer.parseInt(stockCO.quantity);
                if(quantity >= 1)
                {
                    Integer total_quantityint =  Integer.parseInt(total_quantity);
                    total_quantityint = total_quantityint -1;
                    total_quantity = ""+total_quantityint;
                    quantity= quantity - 1;
                }
                if(quantity == 0)
                {
                    quantity = 0;
                }
                stockCO.quantity = quantity+"";
                priceCalculation();
                buyingProductsAdapter.notifyDataSetChanged();
            }
        });
    }

    public int priceCalculation()
    {
        product_price_calculation.setVisibility(View.VISIBLE);

        int all_ver_price = 0;
        int totalCount = 0;
        for (int i = 0; i < productCO.stock.size(); i++)
        {
            Integer count = Integer.parseInt(productCO.stock.get(i).quantity);
            totalCount = totalCount + count;
        }

        productCO.quantity = ""+totalCount;

        int totalPrice;
        Integer retailer_max_purchase_qty = Integer.parseInt( productCO.retailer_max_purchase_qty);

        if(totalCount > retailer_max_purchase_qty)
        {
            Integer w_price = Integer.parseInt(productCO.wholesale_price);
            Integer piece_in_set = Integer.parseInt(productCO.piece_in_set);
            Integer overAllPrice = piece_in_set*w_price;
            totalPrice = totalCount*overAllPrice;
            productCO.product_purchase_type = "Wholeseller";
        }
        else
        {
            Integer r_price = Integer.parseInt(productCO.retail_price);
            Integer piece_in_set = Integer.parseInt(productCO.piece_in_set);
            Integer overAllPrice = piece_in_set*r_price;
            totalPrice = totalCount*overAllPrice;
            productCO.product_purchase_type = "Retailer";
        }

        all_ver_price = all_ver_price+totalPrice;

        product_total_qty.setText("Total qty: "+totalCount);
        product_total_price.setText(IConstants.RUPEE_ICON+ all_ver_price);

        float  percent = 0.95f;
        float  gamount = all_ver_price*percent;
        float  GST = all_ver_price - gamount;
        float  excludeGstPrice = all_ver_price  - GST;
        product_price_with_tax.setText(IConstants.RUPEE_ICON+ excludeGstPrice+ " + " +IConstants.RUPEE_ICON+GST+ " Tax");

        return totalCount;
    }
    @Override
    public void onAttach(Context context)
    {
        super.onAttach(context);
        if (context instanceof ItemClickListener)
        {
            mListener = (ItemClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement ItemClickListener");
        }
    }
    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    protected String getTAG()
    {
        return null;
    }

    @Override
    protected void onViewClick(View view)
    {
        switch (view.getId())
        {
            case R.id.add_to_cart:
                AddCartCO addCartCO = new  AddCartCO();
                addCartCO.productId = productCO.id;
                addCartCO.sellerType = productCO.product_purchase_type;

                for(int i=0; i<productCO.stock.size(); i++)
                {
                    StockCO stCO = productCO.stock.get(i);
                    addCartCO.stock.add(stCO);
                }
                String co = AppController.gson.toJson(addCartCO);
                CallApi callApi = new CallApi(API.add_cart);
                callApi.addReqParams("productId",UserCO.getUserCOFromDB().id);
                callApi.addReqParams("sellerType",UserCO.getUserCOFromDB().id);
                callApi.addReqParams("user_id",UserCO.getUserCOFromDB().id);
                callApi.addReqParams("productCO",co);
                processCallApi(callApi);

                break;

            case R.id.buy_now:
                Integer min_purchase_qty = Integer.parseInt(productCO.min_purchase_qty);
                Integer product_total_qty_int= priceCalculation();
                if(min_purchase_qty > product_total_qty_int)
                {
                    Toast.makeText(getActivity(), "Purchase minimum Quantity", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    String productCOstr = AppController.gson.toJson(productCO);
                    mListener.onItemClick(total_quantity, productCOstr);
                    dismiss();
                }

                break;
        }
    }

    @Override
    protected void onApiCallSuccess(Object responseValues, CallApi callApi) {
        if(API.add_cart.method.equals(callApi.networkActivity.method))
        {
            try {
                JSONObject jsonObject = new JSONObject(responseValues.toString());
                JSONObject action = jsonObject.getJSONObject("response");
                String message = action.getString("message");
                String status = action.getString("status");

                if (status.equalsIgnoreCase("Success")) {
                    if(!AppValidate.isEmpty(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT)))
                    {
                        Integer  mCartItemCount = Integer.parseInt(AppUtilities.readFromPref(IReqParams.USER_CART_COUNT))+1;
                        AppUtilities.writeToPref(IReqParams.USER_CART_COUNT,""+mCartItemCount);
                    }
                    else
                    {
                        AppUtilities.writeToPref(IReqParams.USER_CART_COUNT,"1");
                        Integer mCartItemCount = 1;
                    }

                   // mListener.onItemClick("check");
                   // PopupBottomDailogFragment2.this.dismiss();
                    //Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                    //setupBadge();
                }
                else
                {
                    Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /* @Override public void onClick(View view)
     {
         TextView tvSelected = (TextView) view;
         mListener.onItemClick(tvSelected.getText().toString());
         dismiss();
     }*/
    public interface ItemClickListener
    {
        void onItemClick(String item, String pos);
    }
}