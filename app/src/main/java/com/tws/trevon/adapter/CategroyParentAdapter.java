package com.tws.trevon.adapter;

 import android.content.Context;
 import android.view.LayoutInflater;
 import android.view.View;
 import android.view.ViewGroup;
 import android.widget.ImageView;
 import android.widget.LinearLayout;
 import android.widget.TextView;

 import com.bumptech.glide.Glide;
 import com.bumptech.glide.load.resource.bitmap.CenterCrop;
 import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
 import com.bumptech.glide.request.RequestOptions;
 import com.tws.trevon.R;
 import com.tws.trevon.co.CategoryCO;
 import com.tws.trevon.co.SubCategoryCO;
 import com.tws.trevon.co.SubminiCategoryCO;

 import java.util.List;

 import androidx.recyclerview.widget.DefaultItemAnimator;
 import androidx.recyclerview.widget.GridLayoutManager;
 import androidx.recyclerview.widget.RecyclerView;


public class CategroyParentAdapter extends RecyclerView.Adapter<CategroyParentAdapter.ItemRowHolder> {

    public List<CategoryCO> categoryCOList;
    private Context mContext;
    ChildCatAdapterView childCatAdapterView;
    
    public void setEnrolledClickListener(CategroyParentAdapter.EnrolledClickListner enrolledClick) {
        this.enrolledClick = enrolledClick;
    }
    public CategroyParentAdapter.EnrolledClickListner enrolledClick;


    public interface EnrolledClickListner
    {
        void onParentViewClick(View view, int positionParent);

    }
    


    public CategroyParentAdapter(Context context, List<CategoryCO> categoryCOList) {
        this.categoryCOList = categoryCOList;
        this.mContext = context;

    }

    @Override
    public ItemRowHolder onCreateViewHolder(ViewGroup parent, int viewType)
    {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.parent_view_adapter_view, parent, false);
        return new ItemRowHolder(v);
    }

    @Override
    public void onBindViewHolder(final ItemRowHolder holder, final int position) {
        final CategoryCO categoryCO = categoryCOList.get(position);

        Glide.with(mContext)
                .load(categoryCO.image)
                .apply(RequestOptions.placeholderOf(R.drawable.error_images).error(R.drawable.error_images))
                //.transform(new CenterCrop(), new RoundedCorners(14))
                .dontAnimate()
                .into(holder.parent_category_image);

        String upperString = categoryCO.title.substring(0, 1).toUpperCase() +
                categoryCO.title.substring(1).toLowerCase();

        holder.parent_title.setText(upperString);
        holder.decription_parent.setText(categoryCO.description);
        if(categoryCO.isVisible)
        {
            holder.child_view_layout.setVisibility(View.VISIBLE);
            holder.parent_arrow.setRotation(90);
        }
        else
        {
            holder.parent_arrow.setRotation(0);
            holder.child_view_layout.setVisibility(View.GONE);
        }

        childCatAdapterView = new ChildCatAdapterView(mContext, categoryCO.subcats);
        GridLayoutManager topLayoutManager = new GridLayoutManager(mContext, 3, GridLayoutManager.VERTICAL, false);
        holder.child_cat_view_recycler_view.setLayoutManager(topLayoutManager);
        holder.child_cat_view_recycler_view.setHasFixedSize(true);
        holder.child_cat_view_recycler_view.setItemAnimator(new DefaultItemAnimator());
        holder.child_cat_view_recycler_view.setAdapter(childCatAdapterView);
        childCatAdapterView.mimi_category = categoryCO.subcats;
        childCatAdapterView.notifyDataSetChanged();




    }

    @Override
    public int getItemCount()
    {
        return (null != categoryCOList ? categoryCOList.size() : 0);
    }

    public class ItemRowHolder extends RecyclerView.ViewHolder implements  View.OnClickListener
    {

        public LinearLayout parent_root_layout;
        public ImageView parent_category_image;
        public TextView parent_title;
        public TextView decription_parent;
        public ImageView parent_arrow;
        public LinearLayout child_view_layout;
        public RecyclerView child_cat_view_recycler_view;


        private ItemRowHolder(View itemView)
        {
            super(itemView);
            parent_root_layout = itemView.findViewById(R.id.parent_root_layout);
            parent_category_image = itemView.findViewById(R.id.parent_category_image);
            parent_title = itemView.findViewById(R.id.parent_title);
            decription_parent = itemView.findViewById(R.id.decription_parent);
            parent_arrow = itemView.findViewById(R.id.parent_arrow);
            child_view_layout= itemView.findViewById(R.id.child_view_layout);
            child_cat_view_recycler_view = itemView.findViewById(R.id.child_cat_view_recycler_view);
            parent_root_layout.setOnClickListener(this);
        }

        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
                case R.id.parent_root_layout:
                    enrolledClick.onParentViewClick(v,getAdapterPosition());
                    break;
            }
        }
    }
}


