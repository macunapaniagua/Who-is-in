package com.soccer.whosin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soccer.whosin.R;
import com.soccer.whosin.models.Member;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {

    private List<Member> mMembers;
    private final Context mContext;

    public MembersAdapter(List<Member> pMembers, Context pContext) {
        mMembers = pMembers;
        mContext = pContext;
    }

    @Override
    public int getItemCount() {
        return mMembers.size();
    }

    @Override
    public MemberViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_member, parent, false);
        return new MemberViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final MemberViewHolder holder, int position) {
        holder.vName.setText(mMembers.get(position).getName());
        Glide.with(mContext)
                .load(mMembers.get(position).getAvatar())
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .dontAnimate()
                .into(holder.vAvatar);
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder {
        public final TextView vName;
        public final CircleImageView vAvatar;

        public MemberViewHolder(View view) {
            super(view);
            vName   = (TextView) view.findViewById(R.id.member_name);
            vAvatar = (CircleImageView) view.findViewById(R.id.member_avatar);
        }
    }
}
