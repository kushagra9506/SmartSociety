package app.smartsociety.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import app.smartsociety.Common.Common;
import app.smartsociety.R;

import static app.smartsociety.Common.Common.admin;

public class VisitorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public  TextView name,contact,roomno,intime,outime;
    public  ImageView personimage;


    public VisitorViewHolder(@NonNull View itemView) {
        super(itemView);

        name = (itemView).findViewById(R.id.visitorname);
        contact = (itemView).findViewById(R.id.visitorcontact);
        roomno = (itemView).findViewById(R.id.visitorroomno);
        intime = (itemView).findViewById(R.id.visitorintime);
        personimage = (itemView).findViewById(R.id.visitorimage);
        outime = (itemView).findViewById(R.id.visitorouttime);

        if (Common.admin)
        itemView.setOnCreateContextMenuListener(this);

    }

    @Override
    public void onClick(View view) {

    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.Update);
        contextMenu.add(0,0,getAdapterPosition(), Common.AddOut);
    }
}
