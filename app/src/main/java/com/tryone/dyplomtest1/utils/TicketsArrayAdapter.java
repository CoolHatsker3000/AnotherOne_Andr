package com.tryone.dyplomtest1.utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;

import com.tryone.dyplomtest1.R;
import com.tryone.dyplomtest1.constants.Constants;
import com.tryone.dyplomtest1.views.Ticket;

import java.util.ArrayList;
import java.util.List;

public class TicketsArrayAdapter extends ArrayAdapter<Ticket> {
    private LayoutInflater layoutInflater;
    private List<Ticket> tickets=new ArrayList<>();
    private Context context;


    public TicketsArrayAdapter(@NonNull Context context, int resource, List<Ticket> tickets, LayoutInflater layoutInflater) {
        super(context, resource,tickets);
        this.context=context;
        this.tickets=tickets;
        this.layoutInflater=layoutInflater;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        Ticket ticket=tickets.get(position);
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.list_ticket_view_1,null,false);
            viewHolder.tvListTicketTitle=convertView.findViewById(R.id.tvListTicketTitle);
            viewHolder.tvListTicketDescr=convertView.findViewById(R.id.tvListTicketDescr);
            viewHolder.tvListTicketStatus=convertView.findViewById(R.id.tvListTicketStatus);
            convertView.setTag(viewHolder);

        } else{
            viewHolder= (ViewHolder) convertView.getTag();
        }
        viewHolder.tvListTicketTitle.setText(ticket.name);
        viewHolder.tvListTicketDescr.setText(ticket.description);
        /*switch (ticket.status){
            case 0:
                viewHolder.tvListTicketStatus.setText("открыт");
                viewHolder.tvListTicketStatus.setTextColor(ContextCompat.getColor(context,R.color.colorTicketOpen));
                break;
            case 1:
                viewHolder.tvListTicketStatus.setText("принят");
                viewHolder.tvListTicketStatus.setTextColor(ContextCompat.getColor(context,R.color.colorOrange));
                break;
        }*/
        viewHolder.tvListTicketStatus.setText(Constants.STATUS_ARRAY[ticket.status]);
        viewHolder.tvListTicketStatus.setTextColor(ContextCompat.getColor(context,Constants.STATUS_COLORS[ticket.status]));

        return convertView;
    }

    private class ViewHolder{
        TextView tvListTicketTitle,tvListTicketDescr,tvListTicketStatus;
        ImageView ivListTicketImg;
    }

}
