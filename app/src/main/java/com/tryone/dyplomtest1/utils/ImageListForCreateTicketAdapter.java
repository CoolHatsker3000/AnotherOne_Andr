package com.tryone.dyplomtest1.utils;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.tryone.dyplomtest1.R;
import java.util.List;

public class ImageListForCreateTicketAdapter extends ArrayAdapter<Uri> {
    private LayoutInflater layoutInflater;
    private List<Uri> imgPaths;
    private Context context;

    private class ViewHolder{
        TextView tvCreateTicketAddedImg;
        ImageView ivCreateTicketAddedImg;
    }

    public ImageListForCreateTicketAdapter(@NonNull Context context, int resource, List<Uri> imgs, LayoutInflater layoutInflater) {
        super(context, resource,imgs);
        this.context=context;
        this.imgPaths=imgs;
        this.layoutInflater=layoutInflater;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder=new ViewHolder();
        Uri uri=imgPaths.get(position);
        if (convertView==null){
            convertView=layoutInflater.inflate(R.layout.list_create_ticket_images_item,null,false);
            viewHolder.tvCreateTicketAddedImg=convertView.findViewById(R.id.tvCreateTicketAddedImg);
            viewHolder.ivCreateTicketAddedImg=convertView.findViewById(R.id.ivCreateTicketAddedImg);
            convertView.setTag(viewHolder);
        } else{
            viewHolder= (ImageListForCreateTicketAdapter.ViewHolder) convertView.getTag();
        }
        viewHolder.tvCreateTicketAddedImg.setText(uri.getPath());
        viewHolder.ivCreateTicketAddedImg.setImageURI(uri);
        return convertView;

    }
}
