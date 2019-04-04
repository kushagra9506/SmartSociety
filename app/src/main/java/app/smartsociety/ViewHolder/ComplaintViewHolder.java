package app.smartsociety.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import app.smartsociety.R;
import de.hdodenhof.circleimageview.CircleImageView;

public class ComplaintViewHolder extends RecyclerView.ViewHolder {
    public TextView name,date;
    public Button resolved;
    public CircleImageView imageViewl;
    public ComplaintViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.complaint);
        date = itemView.findViewById(R.id.complaintdate);
        imageViewl = itemView.findViewById(R.id.complaintownimage);
        resolved = itemView.findViewById(R.id.bt_resolved);


    }
}
