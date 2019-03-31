package app.smartsociety.ViewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import app.smartsociety.Common.Common;
import app.smartsociety.Interface.ItemClickListener;
import app.smartsociety.R;


public class EventViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener {

    public TextView name,description,date;
    public ItemClickListener itemClickListener;
    public EventViewHolder(@NonNull View itemView) {
        super(itemView);

        name = itemView.findViewById(R.id.eventname);
        description = itemView.findViewById(R.id.eventdescription);
        date = itemView.findViewById(R.id.eventdate);

        itemView.setOnClickListener(this);
        itemView.setOnCreateContextMenuListener(this);
    }

    @Override
    public void onClick(View view) {

        itemClickListener.onClick(view,getAdapterPosition(),false);
    }

    public void setItemClickListener(ItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    @Override
    public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {

        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.Update);
        contextMenu.add(0,0,getAdapterPosition(), Common.Delete);
    }
}
