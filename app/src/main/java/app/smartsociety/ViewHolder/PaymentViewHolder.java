package app.smartsociety.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.smartsociety.Interface.ItemClickListener;
import app.smartsociety.R;

public class PaymentViewHolder  extends RecyclerView.ViewHolder implements View.OnClickListener {
    public TextView maintainancetitle,cost,date;
    public ItemClickListener itemClickListener;
    public Button btn;
    public PaymentViewHolder(@NonNull View itemView) {
        super(itemView);

        maintainancetitle = itemView.findViewById(R.id.maintainancetitle);
        cost = itemView.findViewById(R.id.cost);
        date = itemView.findViewById(R.id.monthpaymentdate);
        btn = itemView.findViewById(R.id.paybutton);
        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        itemClickListener.onClick(view,getAdapterPosition(),false);
    }
    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

}
