package com.watwarrior.watchatbot.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.watwarrior.watchatbot.R;
import com.watwarrior.watchatbot.models.AbstractChatMessage;
import com.watwarrior.watchatbot.models.MapChatMessage;
import com.watwarrior.watchatbot.models.TextChatMessage;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mChatList;
    private ChatAdapter mChatAdapter;
    private EditText mChatText;
    private Button mSendButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setUIReference();

        doDemo();
    }


    private void setUIReference() {
        mChatList = (RecyclerView) findViewById(R.id.chat_list);
        mChatAdapter = new ChatAdapter();
        mChatList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        mChatList.setAdapter(mChatAdapter);

        mChatText = (EditText) findViewById(R.id.chat_text_box);
        mSendButton = (Button) findViewById(R.id.chat_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mChatText.getText().toString();
                if (!message.isEmpty()){
                    mChatText.setText("");
                    mChatAdapter.addMessage(new TextChatMessage("Me", message));
                    // TODO: 16/10/29 Network calls
                }
            }
        });
    }

    private void doDemo(){
        mChatAdapter.addMessage(new TextChatMessage("Me", "Hello!"));
        mChatAdapter.addMessage(new TextChatMessage("Bot", "Hi!"));
        mChatAdapter.addMessage(new TextChatMessage("Me", "Where is RCH?"));
        mChatAdapter.addMessage(new MapChatMessage("Bot", 43.47031155f, -80.54084139f));
    }


    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder> {

        private static final int VIEW_TYPE_TEXT = 0;
        private static final int VIEW_TYPE_MAP = 1;

        List<AbstractChatMessage> mMessages;

        public ChatAdapter() {
            mMessages = new ArrayList<>();
        }

        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            Log.d(TAG, "Create view holder");
            switch (viewType) {
                case VIEW_TYPE_TEXT:
                    return new TextViewHolder(LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.chat_message_normal_text, parent, false));
                case VIEW_TYPE_MAP:
                    return new MapViewHolder(LayoutInflater.from(MainActivity.this)
                            .inflate(R.layout.chat_message_map, parent, false));
            }
            return null;
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {
            Log.d(TAG, "Bind view holder");
            AbstractChatMessage message = mMessages.get(position);
            if (holder instanceof TextViewHolder) {
                TextChatMessage textMessage = (TextChatMessage) message;
                TextViewHolder textViewHolder = (TextViewHolder) holder;
                textViewHolder.user.setText(textMessage.user);
                textViewHolder.message.setText(textMessage.message);
            } else if (holder instanceof MapViewHolder) {
                MapChatMessage mapMessage = (MapChatMessage) message;
                MapViewHolder mapViewHolder = (MapViewHolder) holder;
                mapViewHolder.user.setText(mapMessage.user);
                mapViewHolder.setMapLatLng(mapMessage.latitude, mapMessage.longitude);
            }
        }

        @Override
        public int getItemCount() {
            if (mMessages == null) return 0;
            return mMessages.size();
        }

        public void addMessage(AbstractChatMessage message) {
            mMessages.add(message);
            notifyDataSetChanged();
            Log.d(TAG, "Size: " + mMessages.size());
        }

        @Override
        public int getItemViewType(int position) {
            if (mMessages.get(position) instanceof TextChatMessage) {
                return VIEW_TYPE_TEXT;
            } else if (mMessages.get(position) instanceof MapChatMessage) {
                return VIEW_TYPE_MAP;
            } else {
                return VIEW_TYPE_TEXT;
            }
        }

        public class TextViewHolder extends ChatViewHolder {
            TextView user;
            TextView message;

            public TextViewHolder(View itemView) {
                super(itemView);
                user = (TextView) itemView.findViewById(R.id.chat_user);
                message = (TextView) itemView.findViewById(R.id.chat_message);
            }
        }

        public class MapViewHolder extends ChatViewHolder implements OnMapReadyCallback {
            TextView user;
            GoogleMap mMap;
            private UiSettings mUiSettings;
            private float mLat;
            private float mLng;
            private boolean isCoordinateReceived;

            public MapViewHolder(View itemView) {
                super(itemView);
                user = (TextView) itemView.findViewById(R.id.chat_user);
                SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.chat_map);
                mapFragment.getMapAsync(this);
                isCoordinateReceived = false;
            }

            public void setMapLatLng(float lat, float lng) {
                LatLng coordinates = new LatLng(lat, lng);
                if (mMap != null) {
                    mMap.addMarker(new MarkerOptions()
                            .position(coordinates));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16.0f));
                } else {
                    mLat = lat;
                    mLng = lng;
                    isCoordinateReceived = true;
                }
            }

            @Override
            public void onMapReady(GoogleMap googleMap) {
                mMap = googleMap;

                // Initialize a marker with the position
                if (isCoordinateReceived) {
                    LatLng coordinates = new LatLng(mLat, mLng);
                    mMap.addMarker(new MarkerOptions()
                            .position(coordinates));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(coordinates, 16.0f));
                }

                mUiSettings = mMap.getUiSettings();
                mUiSettings.setZoomGesturesEnabled(true);
                mUiSettings.setZoomControlsEnabled(true);
            }
        }

        public abstract class ChatViewHolder extends RecyclerView.ViewHolder {
            public ChatViewHolder(View itemView) {
                super(itemView);
            }
        }

    }


}
