package com.soccer.whosin.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.bumptech.glide.Glide;
import com.soccer.whosin.R;
import com.soccer.whosin.models.Member;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mario A on 27/11/16.
 **/

public class ParticipantsAdapter extends BaseAdapter {

    private Context mContext;
    private List< Member> mPlayersConfirmed;

    public ParticipantsAdapter(Context pContext, List<Member> pPlayersConfirmed) {
        this.mContext          = pContext;
        this.mPlayersConfirmed = pPlayersConfirmed;
    }

    @Override
    public int getCount() {
        return mPlayersConfirmed.size();
    }

    @Override
    public Object getItem(int i) {
        return mPlayersConfirmed.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        ParticipantHolder holder;
        if (view == null) {
            view = LayoutInflater.from(mContext).inflate(R.layout.match_participant, null);
            holder = new ParticipantHolder(view);
            view.setTag(holder);
        } else
            holder = (ParticipantHolder) view.getTag();
        Glide.with(mContext)
                .load(mPlayersConfirmed.get(i).getAvatar())
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .dontAnimate()
                .into(holder.getPicture());
        return view;
    }

    private class ParticipantHolder {
        private CircleImageView vPicture;

        public ParticipantHolder(View pView) {
            this.vPicture = (CircleImageView) pView.findViewById(R.id.match_participant_image);
        }

        public CircleImageView getPicture() {
            return vPicture;
        }
    }
}
