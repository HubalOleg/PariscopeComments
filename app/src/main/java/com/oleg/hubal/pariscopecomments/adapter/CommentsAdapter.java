package com.oleg.hubal.pariscopecomments.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.oleg.hubal.pariscopecomments.R;
import com.oleg.hubal.pariscopecomments.model.CommentItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 07.12.2016.
 */

public class CommentsAdapter extends RecyclerView.Adapter<CommentsAdapter.MessageViewHolder> {

    private InsertMessageListener mInsertMessageListener;
    private List<CommentItem> mCommentsList;

    public CommentsAdapter(InsertMessageListener insertMessageListener) {
        mInsertMessageListener = insertMessageListener;
        mCommentsList = new ArrayList<>();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_comment, parent, false);

        return new MessageViewHolder(v);
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.onBind(mCommentsList.get(position));
    }

    @Override
    public int getItemCount() {
        return mCommentsList.size();
    }

    public void addComment(CommentItem comment) {
        mInsertMessageListener.onMessageInserted(mCommentsList.size());
        mCommentsList.add(comment);
        notifyItemInserted(mCommentsList.size());
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.tv_user_name)
        TextView mUserNameTextView;
        @BindView(R.id.tv_message_card)
        TextView mMessageTextView;
        @BindView(R.id.iv_user_image)
        ImageView mUserImageView;

        public MessageViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(MessageViewHolder.this, itemView);
        }

        public void onBind(CommentItem commentItem) {
            String message = commentItem.getMessage();
            String imageUri = commentItem.getImageUri();
            String userName = commentItem.getUserName();

            mMessageTextView.setText(message);
            mUserNameTextView.setText(userName);
            ImageLoader.getInstance().displayImage(imageUri, mUserImageView);
        }
    }

    public interface InsertMessageListener {
        void onMessageInserted(int position);
    }
}
