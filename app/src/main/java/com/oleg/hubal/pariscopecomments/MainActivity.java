package com.oleg.hubal.pariscopecomments;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.oleg.hubal.pariscopecomments.adapter.MessageAdapter;
import com.oleg.hubal.pariscopecomments.model.MessageItem;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private static final String imageUri = "http://pre11.deviantart.net/eeb3/th/pre/i/2009/299/d/2/big_sur_ocean_view_vertical_by_leitmotif.jpg";

    private MessageAdapter mMessageAdapter;
    private LinearLayoutManager mLinearLayoutManager;

    @BindView(R.id.iv_stream_simulator)
    ImageView mStreamSimulatorImageView;
    @BindView(R.id.et_message)
    EditText mMessageEditText;
    @BindView(R.id.btn_send_message)
    ImageButton mSendMessageImageButton;
    @BindView(R.id.rv_messages)
    RecyclerView mMessageRecyclerView;

    private MessageAdapter.InsertMessageListener mInsertMessageListener = new MessageAdapter.InsertMessageListener() {
        @Override
        public void onMessageInserted(int position) {
            mLinearLayoutManager.scrollToPosition(position);
        }

    };

    private View.OnLayoutChangeListener mOnLayoutChangeListener = new View.OnLayoutChangeListener() {
        @Override
        public void onLayoutChange(View view, int left, int top, int right, int bottom, int oldLeft, int oldTop, int oldRight, int oldBottom) {
            if ( bottom < oldBottom) {
                mMessageRecyclerView.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mMessageRecyclerView.scrollToPosition(mMessageRecyclerView.getAdapter().getItemCount() - 1);
                    }
                }, 100);
            }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initImageLoader();
        ButterKnife.bind(MainActivity.this);

        loadBackground();
        initRecyclerView();
    }

    private void loadBackground() {
        ImageLoader.getInstance().displayImage(imageUri, mStreamSimulatorImageView);
    }

    private void initRecyclerView() {
        mMessageAdapter = new MessageAdapter(MainActivity.this, mInsertMessageListener);

        mLinearLayoutManager = new LinearLayoutManager(MainActivity.this) {
            @Override
            public boolean canScrollVertically() {
                return false;
            }
        };

        mLinearLayoutManager.setStackFromEnd(true);

        mMessageRecyclerView.setLayoutManager(mLinearLayoutManager);
        mMessageRecyclerView.setHasFixedSize(true);
        mMessageRecyclerView.setNestedScrollingEnabled(false);
        mMessageRecyclerView.setAdapter(mMessageAdapter);
        mMessageRecyclerView.addOnLayoutChangeListener(mOnLayoutChangeListener);
    }

    @OnClick(R.id.btn_send_message)
    void sendMessage() {
        String message = mMessageEditText.getText().toString();
        String imageUri = "https://pp.vk.me/c631524/v631524029/453fe/YONZuru763Q.jpg";
        String userName = "@user_name2133";

        if (!TextUtils.isEmpty(message)) {
            mMessageEditText.setText("");
            mMessageAdapter.addMessage(new MessageItem(message, imageUri, userName));
        } else {
            mMessageAdapter.addMessage(new MessageItem("AAA???777??/A", "https://encrypted-tbn2.gstatic.com/images?q=tbn:ANd9GcQmNCk7L76tvylZw_VPB3oYMHUDejqnl4TvgpGhpB4qEJdu1oDh", "SlowPoke"));
        }
    }

    private void initImageLoader() {
        DisplayImageOptions mOptions = new DisplayImageOptions.Builder()
                .imageScaleType(ImageScaleType.EXACTLY)
                .showImageOnLoading(android.R.drawable.screen_background_light)
                .cacheInMemory(true)
                .cacheOnDisk(true)
                .considerExifParams(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();

        ImageLoaderConfiguration defaultConfiguration
                = new ImageLoaderConfiguration.Builder(getApplicationContext())
                .defaultDisplayImageOptions(mOptions)
                .threadPoolSize(5)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .diskCacheExtraOptions(480, 480, null)
                .denyCacheImageMultipleSizesInMemory()
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .build();

        ImageLoader.getInstance().init(defaultConfiguration);
    }



}
