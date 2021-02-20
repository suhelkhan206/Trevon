package com.tws.trevon.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.tws.trevon.R;
import com.tws.trevon.co.FaqCOO;
import com.tws.trevon.co.SubCategoryCO;

import java.util.List;

import androidx.recyclerview.widget.RecyclerView;


public class FaqAdapter extends RecyclerView.Adapter<FaqAdapter.ItemRowHolder> {

    public List<FaqCOO> faqCOOListList;
    private Context mContext;
    ChildCatAdapterView childCatAdapterView;

    public void setEnrolledClickListener(FaqAdapter.EnrolledClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public FaqAdapter.EnrolledClickListner enrolledClick;


    public interface EnrolledClickListner
    {
        void onParentViewClick(View view, int positionParent);

    }



    public FaqAdapter(Context context, List<FaqCOO> faqCOOListList) {
        this.faqCOOListList = faqCOOListList;
        this.mContext = context;

    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.faq_adapter_view, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final FaqCOO faqCOO = faqCOOListList.get(position);

        holder.faq_title.setText(faqCOO.question);
       // holder.faq_description.setText(faqCOO.answer);
        //  holder.decription_parent.setText(subCategoryCO.);

    }

    @Override
    public int getItemCount()
    {
        return (null != faqCOOListList ? faqCOOListList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements  View.OnClickListener
    {

        public LinearLayout faq_root;
        public TextView faq_title, faq_description;



        private ItemRowHolder(View itemView)
        {
            super(itemView);
            faq_root = itemView.findViewById(R.id.faq_root);
            faq_root.setOnClickListener(this);
            faq_title = itemView.findViewById(R.id.faq_title);
            faq_description = itemView.findViewById(R.id.faq_description);
        }


        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.faq_root:
                    enrolledClick.onParentViewClick(v,getAdapterPosition());
                    break;
            }
        }
    }
}



