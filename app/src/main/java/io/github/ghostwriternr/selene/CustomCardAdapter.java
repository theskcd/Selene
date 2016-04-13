package io.github.ghostwriternr.selene;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

import java.util.List;
import java.util.Objects;

public class CustomCardAdapter extends BaseAdapter {
    Context context;
    List<CardItem> cardItems;

    public CustomCardAdapter(Context context, List<CardItem> items){
        this.context = context;
        this.cardItems = items;
    }

    public class ViewHolder {
        ImageView cover;
        TextView songtit;
        TextView artname;
        TextView albname;
        ImageButton yout;
        ImageButton spot;
        ImageButton sound;
        CardView card;
    }

    public View getView(int position, View convertView, ViewGroup parent){
        final ViewHolder holder;

        LayoutInflater cInflater = (LayoutInflater) context.getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        if (convertView == null){
            convertView = cInflater.inflate(R.layout.card_item, null);
            holder = new ViewHolder();
            holder.songtit = (TextView) convertView.findViewById(R.id.SongTitle);
            holder.artname = (TextView) convertView.findViewById(R.id.ArtistName);
            holder.albname = (TextView) convertView.findViewById(R.id.AlbumName);
            holder.cover = (ImageView) convertView.findViewById(R.id.CoverArt);
            holder.yout = (ImageButton) convertView.findViewById(R.id.youtubeicon);
            holder.spot = (ImageButton) convertView.findViewById(R.id.spotifyicon);
            holder.sound = (ImageButton) convertView.findViewById(R.id.soundcloudicon);
            holder.card = (CardView) convertView.findViewById(R.id.card_view);
            convertView.setTag(holder);
        }
        else {
            holder = (ViewHolder) convertView.getTag();
        }

        final CardItem card = (CardItem) getItem(position);

        holder.songtit.setText(card.getSongtitle());
        holder.artname.setText(card.getArtistname());
        holder.albname.setText(card.getAlbumname());
//        if (Objects.equals(card.getSoundcloud(), "0")){
//            holder.sound.setVisibility(View.INVISIBLE);
//        }
//        if (Objects.equals(card.getSpotify(), "0")){
//            holder.spot.setVisibility(View.INVISIBLE);
//        }
        Picasso.with(context).load(card.getAlbumart()).into(holder.cover, new com.squareup.picasso.Callback(){
            @Override
            public void onSuccess() {
                Bitmap bitmapimage = ((BitmapDrawable)(holder.cover).getDrawable()).getBitmap();
                Palette.generateAsync(bitmapimage, new Palette.PaletteAsyncListener(){
                    @Override
                    public void onGenerated(Palette palette){
                        Palette.Swatch swatch = palette.getMutedSwatch();
                        if (swatch != null) {
                            holder.card.setCardBackgroundColor(swatch.getRgb());
                            holder.songtit.setTextColor(swatch.getTitleTextColor());
                            holder.albname.setTextColor(swatch.getBodyTextColor());
                            holder.artname.setTextColor(swatch.getBodyTextColor());
                        }
                    }
                });
            }
            @Override
            public void onError() {
                holder.card.setCardBackgroundColor(Color.WHITE);
                holder.songtit.setTextColor(Color.BLACK);
                holder.albname.setTextColor(Color.GRAY);
                holder.artname.setTextColor(Color.GRAY);
            }
        });
        holder.sound.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent soundintent = new Intent(Intent.ACTION_VIEW, Uri.parse("soundcloud://tracks:" + card.getSoundcloud()));
                PackageManager packageman = context.getPackageManager();
                List<ResolveInfo> activities = packageman.queryIntentActivities(soundintent, 0);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    context.startActivity(soundintent);
                }
                else {
                    Toast showtoast = Toast.makeText(context, "Soundcloud not installed", Toast.LENGTH_SHORT);
                    showtoast.show();
                }
//                try {
//                    Intent soundintent = new Intent(Intent.ACTION_VIEW, Uri.parse("soundcloud://tracks:" + card.getSoundcloud()));
//                    context.startActivity(soundintent);
//                } catch (ActivityNotFoundException ex) {
//                    Intent intent = new Intent(Intent.ACTION_VIEW,
//                            Uri.parse("http://www.youtube.com/watch?v=" + card.getYoutube()));
//                    context.startActivity(intent);
//                }
            }
        });
        holder.yout.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                try {
                    Log.d("Youtube link", card.getYoutube());
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + card.getYoutube()));
                    context.startActivity(intent);
                } catch (ActivityNotFoundException ex) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + card.getYoutube()));
                    Log.d("Youtube link", card.getYoutube());
                    context.startActivity(intent);
                }
            }
        });
        holder.spot.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                Intent spotintent = new Intent(Intent.ACTION_VIEW, Uri.parse("spotify:track:" + card.getSpotify()));
                PackageManager packageman = context.getPackageManager();
                List<ResolveInfo> activities = packageman.queryIntentActivities(spotintent, 0);
                boolean isIntentSafe = activities.size() > 0;
                if (isIntentSafe) {
                    context.startActivity(spotintent);
                }
                else {
                    Toast showtoast = Toast.makeText(context, "Spotify not installed", Toast.LENGTH_SHORT);
                    showtoast.show();
                }
            }
        });

        return convertView;
    }

    @Override
    public int getCount(){
        return cardItems.size();
    }

    @Override
    public Object getItem(int position){
        return cardItems.get(position);
    }

    @Override
    public long getItemId(int position){
        return cardItems.indexOf(getItem(position));
    }
}
