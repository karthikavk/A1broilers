package in.kassapos.a1broilers.adapter;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;


import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.List;

import in.kassapos.a1broilers.R;
import in.kassapos.a1broilers.api.Product;
import in.kassapos.a1broilers.service.ServiceCall;

/**
 * Created by KASSAPOS8 on 7/27/2015.
 */
abstract public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.MyViewHolder> {
    List<Product> orderMaster;
    Activity activity;
    public ProductAdapter(List<Product> orderMaster, Activity activity){
        this.orderMaster=orderMaster;
        this.activity=activity;
    }


    abstract public  void onClicked(Product orderMaster,int size);



    public static class MyViewHolder extends RecyclerView.ViewHolder {
        Context context;
        TextView textViewName;
        TextView desc;
        TextView amount,rate,qty;
        ImageView imageView;
        Button button;
        Button plus,minus;
        CardView cardView;
        Spinner cutsize;


        public MyViewHolder(View itemView,Context context) {
            super(itemView);
            this.context=context;
            this.textViewName = (TextView) itemView.findViewById(R.id.textViewName);
            this.button = (Button) itemView.findViewById(R.id.card_view_button);
            this.imageView=(ImageView) itemView.findViewById(R.id.imageView);
           // this.desc= (TextView) itemView.findViewById(R.id.textViewdesc);
           // this.amount=(TextView)itemView.findViewById(R.id.amount);
            this.rate=(TextView)itemView.findViewById(R.id.rate);
            this.qty=(TextView)itemView.findViewById(R.id.quanity);
            this.plus=(Button)itemView.findViewById(R.id.plus);
            this.minus=(Button)itemView.findViewById(R.id.minus);
            this.cardView=(CardView)itemView.findViewById(R.id.card_view);
            this.cutsize=(Spinner)itemView.findViewById(R.id.cutsize1);

        }
    }
    @Override
    public ProductAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.product_cardnew, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(view,parent.getContext());
        return myViewHolder;

    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder,final int position) {
        TextView textViewName = holder.textViewName;
        Button button = holder.button;
        SimpleDateFormat format=new SimpleDateFormat("dd/MM/yy HH:mm");
        textViewName.setText("" + orderMaster.get(position).name);//+" Rs. "+new BigDecimal(orderMaster.get(position).rate).setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"/Kg.");
        final Product product=orderMaster.get(position);
        if(true){
            Glide.with(this.activity).load(ServiceCall._ImagePath + product.imagepath).into( holder.imageView);
            //ImageLoader  imgLoader=new ImageLoader(activity);
           // int loader = R.drawable.img_cart;
            //imgLoader.DisplayImage(ServiceCall._ImagePath + orderMaster.get(position).imagepath, loader, holder.imageView);
        }else{
            holder.imageView.setVisibility(View.GONE);
        }


        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ProductAdapter.this.onClicked(orderMaster.get(position), holder.cutsize.getSelectedItemPosition());
            }
        });
//        holder.desc.setText(orderMaster.get(position).description);
       holder.rate.setText("Rs. "+new BigDecimal(orderMaster.get(position).rate).setScale(2,BigDecimal.ROUND_HALF_UP).toString()+"");
        //holder.rate.setVisibility(View.GONE);
        if (product.qty == null||product.qty<product.minimquantity) {
            product.qty=product.minimquantity.floatValue();
        }
    //    holder.amount.setText(new BigDecimal(product.getAmount()).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
        holder.qty.setText(new BigDecimal(product.qty).setScale(2, BigDecimal.ROUND_HALF_UP).toString());

        holder.plus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                orderMaster.get(position).qty += orderMaster.get(position).increament;
                orderMaster.get(position).ischanged=Boolean.TRUE;
                RefreshData(holder, product);
            }
        });
        holder.minus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(orderMaster.get(position).qty-orderMaster.get(position).increament<product.minimquantity){
                    return;
                }
                orderMaster.get(position).qty-=orderMaster.get(position).increament;
                orderMaster.get(position).ischanged=Boolean.TRUE;
                RefreshData(holder,product);
            }
        });
        Spinner area = holder.cutsize;
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(holder.context, R.layout.row_spn, new String[]{"Cutting Size","Small","Medium","Large"});
        adapter.setDropDownViewResource(R.layout.row_spn);
        area.setAdapter(adapter);
        area.setOnItemSelectedListener(new Spinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
               product.cutsize= position;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                product.cutsize=0;
            }
        });

        CardView cardView=holder.cardView;
        if(position%2==0){
            cardView.setCardBackgroundColor(Color.parseColor("#E5E5E5"));
        }else
        cardView.setCardBackgroundColor(Color.WHITE);
    }
    public  void  RefreshData(MyViewHolder holder,Product product){
        holder.amount.setText(new BigDecimal(product.getAmount()).setScale(2,BigDecimal.ROUND_HALF_UP).toString());
        holder.qty.setText(new BigDecimal(product.qty).setScale(2, BigDecimal.ROUND_HALF_UP).toString());
    }

    @Override
    public int getItemCount() {
        return orderMaster.size();
    }
}
