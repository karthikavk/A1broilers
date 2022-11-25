package in.kassapos.a1broilers.api;

import java.util.ArrayList;
import java.util.List;

import in.kassapos.chickenshop.api.User;

/**
 * Created by KASSAPOS8 on 9/6/2015.
 */
public class OrderGroup {
    public List<Order> orders=new ArrayList<>();
    public Integer day=0,paymenttype=0;
    public String serviceareaid;
    public String deliveryscheduleid;
    public String companyid;
    public User user;
    public Integer offer_id;
    public Float offer_amount;
    public String imei,additional_info;

    public void addOrder(Order order){
        if(orders==null){
            orders=new ArrayList<>();
        }
        orders.add(order);
    }
    public void addProduct(Product product,int size){
        for(Order order:orders){
            if(order.product.id.equalsIgnoreCase(product.id)&&order.cutsize==size){
                order.quantity+=product.qty.floatValue();
                return;
            }
        }
        Order order=new Order();
        order.product=product;
        order.quantity=product.qty.floatValue();
        order.cutsize=size;
        orders.add(order);
    }
    public  Float getAmount(){
        Float tmp=Float.valueOf(0);
        for(Order order:orders){
            tmp+=order.product.rate*order.quantity;
        }

        return tmp;
    }
    public  Float getAmountOffer(){
        Float tmp=Float.valueOf(0);
        for(Order order:orders){
            tmp+=order.product.rate*order.quantity;
        }
        if(offer_amount==null){
            return tmp;
        }
        return tmp-offer_amount;
    }
}
