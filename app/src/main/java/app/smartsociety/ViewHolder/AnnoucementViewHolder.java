package app.smartsociety.ViewHolder;

import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.View;
import android.widget.TextView;

import app.smartsociety.Common.Common;
import app.smartsociety.Interface.ItemClickListener;
import app.smartsociety.R;


public class AnnoucementViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnCreateContextMenuListener{


    public TextView Title,message,date;
    public ItemClickListener itemClickListener;
    public AnnoucementViewHolder(View itemView) {
        super(itemView);

        Title = itemView.findViewById(R.id.title);
        message = itemView.findViewById(R.id.message);
        date = itemView.findViewById(R.id.date);

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
    public void onCreateContextMenu(ContextMenu contextMenu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        contextMenu.setHeaderTitle("Select the action");
        contextMenu.add(0,0,getAdapterPosition(), Common.Update);
        contextMenu.add(0,0,getAdapterPosition(), Common.Delete);
    }
}
