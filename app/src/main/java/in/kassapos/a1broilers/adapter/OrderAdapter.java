package in.kassapos.a1broilers.adapter;

import android.app.Activity;

import androidx.appcompat.widget.AppCompatImageButton;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.facebook.drawee.view.SimpleDraweeView;
import com.gc.materialdesign.views.ButtonFloat;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import in.kassapos.a1broilers.R;
import in.kassapos.a1broilers.api.Order;
import in.kassapos.a1broilers.api.OrderGroup;
import in.kassapos.a1broilers.service.ServiceCall;

/**
 * Created by KASSAPOS8 on 7/27/2015.
 */
abstract public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.MyViewHolder> {
    OrderGroup orderGroup;
    Activity activity;
    public OrderAdapter(OrderGroup orderGroup, Activity activity){
        this.orderGroup = orderGroup;
        this.activity=activity;
    }


    abstract public  void onClicked(Order orderMaster);



    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView desc;
        TextView amount,rate,qty;
        AppCompatImageButton button;
        SimpleDraweeView imageView;
        Button plus,minus;


        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.button = (AppCompatImageButton) itemView.findViewById(R.id.card_view_button);
            this.imageView=(SimpleDraweeView) itemView.findViewById(R.id.imageView);
            this.desc= (TextView) itemView.findViewById(R.id.textViewdesc);
            this.amount=(TextView)itemView.findViewById(R.id.amount);
            this.rate=(TextView)itemView.findViewById(R.id.rate);
            this.qty=(TextView)itemView.findViewById(R.id.quanity);
            this.plus=(Button)itemView.findViewById(R.id.plus);
            this.minus=(Button)itemView.findViewById(R.id.minus);

        }
    }
    @Override
    public OrderAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.order_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        TextView textViewName = holder.textViewName;
        AppCompatImageButton button = holder.button;
        final Order product= orderGroup.orders.get(position);
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yy HH:mm");
        textViewName.setText("" + product.product.name);

        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                orderGroup.orders.remove(product);
                notifyDataSetChanged();
                OrderAdapter.this.onClicked(product);
            }
        });
      //  holder.desc.setText(orderGroup.get(position).description);
        holder.rate.setText(new BigDecimal(product.product.rate).setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/Kg.");
        if (product.product.qty == null||product.product.qty<product.product.minimquantity) {
            product.product.qty=product.product.minimquantity.floatValue();
        }
        holder.amount.setText(new BigDecimal(product.product.rate * product.quantity).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        holder.qty.setText(new BigDecimal(product.quantity).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        Glide.with(activity).load(ServiceCall._ImagePath + product.product.imagepath).into( holder.imageView);

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                product.quantity += product.product.increament;
                RefreshData(holder,product);
                OrderAdapter.this.onClicked(product);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(product.quantity-product.product.increament<product.product.minimquantity){
                    return;
                }
                product.quantity-=product.product.increament;
                RefreshData(holder,product);
                OrderAdapter.this.onClicked(product);
            }
        });


    }
    public  void  RefreshData(MyViewHolder holder,Order product){
        holder.amount.setText(new BigDecimal(product.quantity*product.product.rate).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        holder.qty.setText(new BigDecimal(product.quantity).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

    @Override
    public int getItemCount() {
        return orderGroup.orders.size();
    }
}
