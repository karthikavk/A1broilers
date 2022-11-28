package in.kassapos.a1broilers.adapter;

import android.app.Activity;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;

import in.kassapos.chickenshop.api.OrderMasterBean;
import in.kassapos.a1broilers.R;

/**
 * Created by KASSAPOS8 on 7/27/2015.
 */
 public abstract class OrderHistoryAdapter extends RecyclerView.Adapter<OrderHistoryAdapter.MyViewHolder> {
    OrderMasterBean[] orderMasterBeans;
    Activity activity;
    public OrderHistoryAdapter(OrderMasterBean[] orderMasterBeans, Activity activity){
        this.orderMasterBeans = orderMasterBeans;
        this.activity=activity;
    }





    public static class MyViewHolder extends RecyclerView.ViewHolder {

        TextView textViewName;
        TextView desc;
        TextView amount,txt_date,status,cancel;



        public MyViewHolder(View itemView) {
            super(itemView);
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
             this.amount=(TextView)itemView.findViewById(R.id.amount);
            this.txt_date=(TextView)itemView.findViewById(R.id.txt_date);
            this.status=(TextView)itemView.findViewById(R.id.status);
            this.cancel=(TextView)itemView.findViewById(R.id.cancel);


        }
    }
    @Override
    public OrderHistoryAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.orderhistory_card, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        TextView textViewName = holder.textViewName;
        final OrderMasterBean product= orderMasterBeans[position];
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yyyy");
        textViewName.setText("Order No " + product.ordermasterid);


      //  holder.desc.setText(orderGroup.get(position).description);
        holder.amount.setText(new BigDecimal(product.amount).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

       holder.txt_date.setText(format.format(product.expected_date).toString());
        String status="";
        holder.cancel.setVisibility(View.GONE);
        if(product.orderstatus==0){
            status="Pending";
            holder.cancel.setVisibility(View.VISIBLE);
        }else if(product.orderstatus==1){
            status="Accepted";
            holder.cancel.setVisibility(View.VISIBLE);
        }else if(product.orderstatus==2){
            status="Confirmed";
        }else if(product.orderstatus==-1){
            status="Rejected";
        }else if(product.orderstatus==10){
            status="Delivered";
        }
        holder.status.setText(status);
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelOrder(orderMasterBeans[position]);

            }
        });



    }


    @Override
    public int getItemCount() {
        return orderMasterBeans.length;
    }
    public abstract void cancelOrder(OrderMasterBean orderMasterBean);
}
