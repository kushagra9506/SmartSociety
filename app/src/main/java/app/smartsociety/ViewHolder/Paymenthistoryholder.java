package app.smartsociety.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import app.smartsociety.R;

public class Paymenthistoryholder extends RecyclerView.ViewHolder {
    public TextView name,transaction,paid,roomno,paymentdate;
    public Paymenthistoryholder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.payername);
        transaction = itemView.findViewById(R.id.transactionid);
        paid = itemView.findViewById(R.id.paidornot);
        roomno = itemView.findViewById(R.id.hisroomno);
        paymentdate = itemView.findViewById(R.id.hisPaymentdate);
    }
}
