package com.oleg.hubal.pariscopecomments.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.oleg.hubal.pariscopecomments.R;
import com.oleg.hubal.pariscopecomments.model.MessageItem;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 07.12.2016.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final static int MAXIMUM_VISIBLE_ITEM_COUNT = 4;

    private final Context mContext;
    private InsertMessageListener mInsertMessageListener;
    private List<MessageItem> mMessageList;
    private List<MessageViewHolder> mMessageHolderList = new ArrayList<>();

    public MessageAdapter(Context context, InsertMessageListener insertMessageListener) {
        mContext = context;
        mInsertMessageListener = insertMessageListener;
        mMessageList = new ArrayList<>();
    }

    @Override
    public MessageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_message, parent, false);

        MessageViewHolder messageViewHolder = new MessageViewHolder(v);
        mMessageHolderList.add(messageViewHolder);

        return messageViewHolder;
    }

    @Override
    public void onBindViewHolder(MessageViewHolder holder, int position) {
        holder.onBind(position, mMessageList.get(position));
    }

    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    public void addMessage(MessageItem messageItem) {
        mMessageList.add(messageItem);

        int position = mMessageList.size() - 1;
        mInsertMessageListener.onMessageInserted(position);
        notifyItemInserted(position);
        fadeOutLastVisibleElement(position);
    }

    private void fadeOutLastVisibleElement(int lastInsertedPosition) {
        int lastVisiblePosition = lastInsertedPosition - MAXIMUM_VISIBLE_ITEM_COUNT;
        MessageViewHolder messageViewHolder = getViewHolderByPosition(lastVisiblePosition);
        if (messageViewHolder != null) {
            fadeOutIfVisible(messageViewHolder);
        }
    }

    private void fadeOutIfVisible(MessageViewHolder messageViewHolder) {
        if (!messageViewHolder.isAnimating && messageViewHolder.isVisible) {
            messageViewHolder.startFadeOutAnimation();
        }
    }

    private MessageViewHolder getViewHolderByPosition(int position) {
        for (MessageViewHolder viewHolder : mMessageHolderList) {
            if (viewHolder.getBoundPosition() == position) {
                return viewHolder;
            }
        }
        return null;
    }

    class MessageViewHolder extends RecyclerView.ViewHolder {

        private final Handler mDelayHandler = new Handler();

        private static final float ALPHA_INVISIBLE = 0f;
        private static final float ALPHA_VISIBLE = 1f;
        private static final int ANIMATION_DELAY = 3000;

        private View mHolderView;
        private int mPosition;

        private Animation mFadeOutAnimation;
        private boolean isAnimating = false;
        private boolean isVisible = true;

        @BindView(R.id.tv_user_name)
        TextView mUserNameTextView;
        @BindView(R.id.tv_message_card)
        TextView mMessageTextView;
        @BindView(R.id.iv_user_image)
        ImageView mUserImageView;

        private Runnable mRunnableStartAnimation = new Runnable() {
            @Override
            public void run() {
                startFadeOutAnimation();
            }
        };

        private Animation.AnimationListener mFadeOutAnimationListener = new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                isAnimating = true;
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                mHolderView.setAlpha(ALPHA_INVISIBLE);
                isAnimating = false;
                isVisible = false;
                removeText();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        };

        public MessageViewHolder(final View itemView) {
            super(itemView);

            mHolderView = itemView;

            ButterKnife.bind(MessageViewHolder.this, itemView);

            mFadeOutAnimation = AnimationUtils.loadAnimation(mContext, R.anim.fade_out_anim);
            mFadeOutAnimation.setAnimationListener(mFadeOutAnimationListener);
        }

        public void onBind(int position, MessageItem messageItem) {
            stopAnimation();
            mPosition = position;
            mHolderView.setAlpha(ALPHA_VISIBLE);
            isVisible = true;

            String message = messageItem.getMessage();
            String imageUri = messageItem.getImageUri();
            String userName = messageItem.getUserName();

            mMessageTextView.setText(message);
            mUserNameTextView.setText(userName);
            ImageLoader.getInstance().displayImage(imageUri, mUserImageView);

            startAnimationWithDelay();
        }

        private void startAnimationWithDelay() {
            mDelayHandler.postDelayed(mRunnableStartAnimation, ANIMATION_DELAY);
        }

        private void stopAnimation() {
            mDelayHandler.removeCallbacks(mRunnableStartAnimation);
            mHolderView.clearAnimation();
            isAnimating = false;
        }

        public void startFadeOutAnimation() {
            if (!isAnimating) {
                mDelayHandler.removeCallbacks(mRunnableStartAnimation);
                mHolderView.startAnimation(mFadeOutAnimation);
            }
        }

        public int getBoundPosition() {
            return mPosition;
        }

        public boolean isAnimating() {
            return isAnimating;
        }

        public boolean isVisible() {
            return isVisible;
        }

        private void removeText() {
            mMessageTextView.setText("");
            mUserNameTextView.setText("");
        }
    }

    public interface InsertMessageListener {
        void onMessageInserted(int position);
    }
}
