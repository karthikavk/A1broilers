package in.kassapos.a1broilers.adapter;

import android.app.Activity;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.gc.materialdesign.views.ButtonFloat;
import com.rey.material.widget.Button;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import in.kassapos.chickenshop.api.Advertisment;
import in.kassapos.chickenshop.api.Category;
import in.kassapos.a1broilers.R;
import in.kassapos.a1broilers.image.ImageLoader;
import in.kassapos.a1broilers.service.ServiceCall;

/**
 * Created by KASSAPOS8 on 7/27/2015.
 */
abstract public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.MyViewHolder> implements OrderViewClick{
    List<Category> orderMaster;
    List<Advertisment> advertisments=new ArrayList<>();
    Activity activity;
    public CategoryAdapter(List<Category> orderMaster, Activity activity){
        this.orderMaster=orderMaster;
        this.activity=activity;
    }

    public void setAdvertisments(List<Advertisment> advertisments) {
        this.advertisments = advertisments;
    }

    abstract public  void onClicked(Category orderMaster);

    @Override
    public void onClick(View v) {

    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private final Button ripplebutton;
        TextView textViewName;
        GridView gridView;
        ButtonFloat button;
        ImageView imageView;
        LinearLayout linearLayout;
        View itemView;
        Context context;

        public MyViewHolder(View itemView,Context context) {
            super(itemView);
            this.itemView=itemView;
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.imageView=(ImageView)itemView.findViewById(R.id.imageView);
            this.linearLayout=(LinearLayout)itemView.findViewById(R.id.lin_layout);
            this.context=context;
            this.ripplebutton=(com.rey.material.widget.Button)itemView.findViewById(R.id.back_button1);
       //     this.button = (ButtonFloat) itemView.findViewById(R.id.card_view_button);
        //    this.gridView=(GridView) itemView.findViewById(R.id.gridView);
        }
    }
    @Override
    public CategoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.category_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view,parent.getContext());
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {

        if(position<orderMaster.size()){
            holder.linearLayout.setVisibility(View.VISIBLE);
            holder.textViewName.setVisibility(View.GONE);
            holder.ripplebutton.setVisibility(View.VISIBLE);
            holder.imageView.setVisibility(View.GONE);
            TextView textViewName = holder.textViewName;
            ButtonFloat button = holder.button;
            SimpleDateFormat format=new SimpleDateFormat("dd/MM/yy HH:mm");
            textViewName.setText("" + orderMaster.get(position).name);
            holder.ripplebutton.setText( orderMaster.get(position).name);
            holder.ripplebutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryAdapter.this.onClicked(orderMaster.get(position));
                }
            });
            holder.textViewName.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    CategoryAdapter.this.onClicked(orderMaster.get(position));
                }
            });
        }else{
            holder.itemView.setPadding(0,0,0,0);

            int pos=position-orderMaster.size();
            Advertisment advertisment=advertisments.get(pos);
            holder.linearLayout.setVisibility(View.GONE);
            holder.imageView.setVisibility(View.VISIBLE);
          //  holder.imageView.getLayoutParams().height = 150;
            ImageLoader imgLoader=new ImageLoader(activity);
            int loader = R.drawable.an;
            imgLoader.DisplayImage(ServiceCall._ImagePath + advertisment.imagepath, loader, holder.imageView);

           /* new DownloadImageTask( holder.imageView,holder.context)
                    .execute(ServiceCall._ImagePath + advertisment.imagepath);*/


        }




    }

    @Override
    public int getItemCount() {
        return (orderMaster.size()+advertisments.size());
    }
}
