package com.tws.trevon.adapter;
 
 import android.content.Context;
 import android.graphics.Color;
 import androidx.recyclerview.widget.RecyclerView;

 import android.media.Image;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.RadioButton;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import com.tws.trevon.R;
 import com.tws.trevon.co.MyAddressCO;

 import java.util.List;

/**
 * Created by admin on 7/26/2017.
 */

public class MyAddressAdapter  extends RecyclerView.Adapter<MyAddressAdapter.MyViewHolder> {

    public List<MyAddressCO> myAddressCOList;
    public boolean isShowEdit;
    Context context;

    public void setAddressClickListener(MyAddressAdapter.AddressClickListner addressClickListner) {
        this.addressClickListner = addressClickListner;
    }
    public MyAddressAdapter.AddressClickListner addressClickListner;


    public interface AddressClickListner
    {
        void onAddressCLick(View view, int position);
        void onAddressRootCLick(View view, int position);
        void ondeleteAddressCLick(View view, int position);
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements  View.OnClickListener {

        public LinearLayout edit_address;
        public TextView address_person_name,user_address;
        public LinearLayout address_root_view;
        public RelativeLayout my_address_line;
        public RadioButton address_radio;
        public LinearLayout delete_address;


        public MyViewHolder(View view)
        {
            super(view);
            edit_address = view.findViewById(R.id.edit_address);
            address_person_name = view.findViewById(R.id.address_person_name);
            user_address = view.findViewById(R.id.user_address);
            address_root_view = view.findViewById(R.id.address_root_view);
            my_address_line = view.findViewById(R.id.my_address_line);
            address_radio = view.findViewById(R.id.address_radio);
            delete_address = view.findViewById(R.id.delete_address);
            edit_address.setOnClickListener(this);
            address_root_view.setOnClickListener(this);
            delete_address.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
           switch (v.getId())
            {
                case R.id.edit_address:
                    MyAddressCO myAddressCO =  myAddressCOList.get(getAdapterPosition());
                    if(myAddressCO.disable.equals("0"))
                    {
                        addressClickListner.onAddressCLick(v,getAdapterPosition());
                    }
                    break;

                case R.id.address_root_view:
                    MyAddressCO myAddressCO1 =  myAddressCOList.get(getAdapterPosition());
                    if(myAddressCO1.disable.equals("0"))
                    {

                       // if (!isShowEdit) {
                            addressClickListner.onAddressRootCLick(v, getAdapterPosition());
                       // }
                    }
                    break;

                case R.id.delete_address:
                    addressClickListner.ondeleteAddressCLick(v, getAdapterPosition());
                    break;


            }
        }
    }


    public MyAddressAdapter(Context context,  List<MyAddressCO> myAddressCOList, boolean isShowEdit) {
        this.myAddressCOList = myAddressCOList;
        this.context = context;
        this.isShowEdit = isShowEdit;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.my_address_adapter_view, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position)
    {
        MyAddressCO myAddressCO = myAddressCOList.get(position);

        //holder.product_image.set
        String upperString = myAddressCO.name.substring(0, 1).toUpperCase() +
                myAddressCO.name.substring(1).toLowerCase();
        holder.address_person_name.setText(myAddressCO.name);
        //holder.user_address.setText(myAddressCO.address_1 +"\n"+myAddressCO.city_id+", "+myAddressCO.state_id+", "+"\n"+ myAddressCO.mobile+","+myAddressCO.pin_code);
        holder.user_address.setText( MyAddressCO.getFullAddress(
                myAddressCO.house_no, myAddressCO.area, myAddressCO.city, myAddressCO.state,
                myAddressCO.country, myAddressCO.phone_no, myAddressCO.pincode));


        if(position==myAddressCOList.size()-1)
        {
            holder.my_address_line.setVisibility(View.GONE);
        }
        else
        {
            holder.my_address_line.setVisibility(View.VISIBLE);
        }

        if(myAddressCO.is_default.equals("1"))
        {
            holder.address_radio.setChecked(true);
        }
        else
        {
            holder.address_radio.setChecked(false);
        }

       // holder.edit_address.setVisibility(View.VISIBLE);
        if(myAddressCO.disable.equals("1"))
        {
            holder.address_person_name.setTextColor(Color.parseColor("#d9d9d9"));
            holder.user_address.setTextColor(Color.parseColor("#d9d9d9"));
        }
        else
        {
            holder.address_person_name.setTextColor(Color.parseColor("#313131"));
            holder.user_address.setTextColor(Color.parseColor("#828282"));
        }
        if(isShowEdit)
        {
            if(myAddressCO.disable.equals("1"))
            {
                holder.edit_address.setVisibility(View.GONE);
            }
            else
            {
                holder.edit_address.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            holder.edit_address.setVisibility(View.GONE);
        }


    }

    @Override
    public int getItemCount() {
        return myAddressCOList.size();
    }
}

