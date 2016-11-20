package com.soccer.whosin.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.soccer.whosin.R;
import com.soccer.whosin.models.Member;
import com.soccer.whosin.models.MemberAction;
import com.soccer.whosin.utils.BusProvider;
import com.soccer.whosin.utils.LocalStorageHelper;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by Mario A on 22/10/2016.
 **/
public class MembersAdapter extends RecyclerView.Adapter<MembersAdapter.MemberViewHolder> {

    private List<Member> mMembers;
    private final Context mContext;
    private int mMemberRowToRemove;
    private boolean mIsApprovalPending;

    public MembersAdapter(List<Member> pMembers, Context pContext, boolean pIsPending) {
        mMembers           = pMembers;
        mContext           = pContext;
        mIsApprovalPending = pIsPending;
        mMemberRowToRemove = -1;
    }

    public void removeMember() {
        if (this.mMemberRowToRemove > -1) {
            this.mMembers.remove(this.mMemberRowToRemove);
            this.notifyItemRemoved(mMemberRowToRemove);
            this.notifyItemRangeChanged(mMemberRowToRemove, mMembers.size());
            this.mMemberRowToRemove = -1;
        }
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
        boolean isGroupAdmin = LocalStorageHelper.getGroupMember(mContext).getMember().isAdmin();
        holder.vName.setText(mMembers.get(position).getName());
        Glide.with(mContext)
                .load(mMembers.get(position).getAvatar())
                .placeholder(R.drawable.com_facebook_profile_picture_blank_square)
                .dontAnimate()
                .into(holder.vAvatar);
        // Enable/Disable Actions
        if (isGroupAdmin && mIsApprovalPending) {
            holder.vRemove.setTag(position);
            holder.vApprove.setTag(position);
            holder.vActions.setVisibility(View.VISIBLE);
        } else
            holder.vActions.setVisibility(View.GONE);
    }

    public class MemberViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public final TextView vName;
        public final CircleImageView vAvatar;
        public final ImageView vApprove;
        public final ImageView vRemove;
        public final LinearLayout vActions;

        public MemberViewHolder(View view) {
            super(view);
            vName    = (TextView) view.findViewById(R.id.member_name);
            vAvatar  = (CircleImageView) view.findViewById(R.id.member_avatar);
            vApprove = (ImageView) view.findViewById(R.id.member_approve);
            vRemove  = (ImageView) view.findViewById(R.id.member_remove);
            vActions = (LinearLayout) view.findViewById(R.id.member_actions);
            this.setListeners();
        }

        protected void setListeners() {
            this.vRemove.setOnClickListener(this);
            this.vApprove.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (view.getTag() != null && view.getTag() instanceof Integer) {
                mMemberRowToRemove  = (int) view.getTag();
                Member member       = mMembers.get(mMemberRowToRemove);
                boolean isApproving = (view.getId() == R.id.member_approve);
                BusProvider.getBus().post(new MemberAction(isApproving, member));
            }
        }
    }
}
