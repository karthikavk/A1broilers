package in.kassapos.a1broilers.adapter;

import android.view.View;

import in.kassapos.chickenshop.api.Category;

/**
 * Created by KASSAPOS8 on 7/27/2015.
 */
public interface OrderViewClick extends View.OnClickListener{

    void onClicked(Category orderMaster);

}
