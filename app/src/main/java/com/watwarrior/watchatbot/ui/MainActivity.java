package com.watwarrior.watchatbot.ui;

import android.os.AsyncTask;
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
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISClient;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISDialog;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISEntity;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISIntent;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponse;
import com.microsoft.cognitiveservices.luis.clientlibrary.LUISResponseHandler;
import com.watwarrior.watchatbot.LUISIntentDictionary;
import com.watwarrior.watchatbot.R;
import com.watwarrior.watchatbot.models.AbstractChatMessage;
import com.watwarrior.watchatbot.models.MapChatMessage;
import com.watwarrior.watchatbot.models.TextChatMessage;
import com.watwarrior.watchatbot.network.BuildingResponse;
import com.watwarrior.watchatbot.network.NetworkHelper;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();

    private RecyclerView mChatList;
    private ChatAdapter mChatAdapter;
    private EditText mChatText;
    private Button mSendButton;

    private LUISClient mLUISClient;
    private LUISResponse previousResponse;

    private NetworkHelper mNetworkHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setupDependencies();

        setUIReference();

        doDemo();
    }

    private void setupDependencies() {
        mLUISClient = new LUISClient(getString(R.string.appId), getString(R.string.appKey), false, false);
        mNetworkHelper = new NetworkHelper(this);
    }

    private void setUIReference() {
        mChatList = (RecyclerView) findViewById(R.id.chat_list);
        mChatAdapter = new ChatAdapter();
        mChatList.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, true));
        mChatList.setAdapter(mChatAdapter);

        mChatText = (EditText) findViewById(R.id.chat_text_box);
        mChatText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mChatList.scrollToPosition(mChatAdapter.mMessages.size() - 1);
            }
        });
        mSendButton = (Button) findViewById(R.id.chat_send);
        mSendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String message = mChatText.getText().toString();
                if (!message.isEmpty()) {
                    mChatText.setText("");
                    mChatAdapter.addMessage(new TextChatMessage("Me", message));
                    // TODO: 16/10/29 Network calls
                    try {
                        mLUISClient.predict(message, new LUISResponseHandler() {
                            @Override
                            public void onSuccess(LUISResponse response) {
                                processResponse(response);
                            }

                            @Override
                            public void onFailure(Exception e) {
                                e.printStackTrace();
                                mChatAdapter.addMessage(new TextChatMessage("Bot", "Sorry, my brain exploded."));
                            }
                        });
                    } catch (IOException e) {
                        e.printStackTrace();
                        mChatAdapter.addMessage(new TextChatMessage("Bot", "Sorry, my brain exploded."));
                    }
                }
            }
        });


    }

    private void doDemo() {
        mChatAdapter.addMessage(new TextChatMessage("Me", "Hello!"));
        mChatAdapter.addMessage(new TextChatMessage("Bot", "Hi!"));
        mChatAdapter.addMessage(new TextChatMessage("Me", "Where is RCH?"));
        mChatAdapter.addMessage(new MapChatMessage("Bot", 43.47031155f, -80.54084139f));
    }

    public void processResponse(LUISResponse response) {
        Log.d(TAG, "-------------------");
        previousResponse = response;
        Log.d(TAG, response.getQuery());
        LUISIntent topIntent = response.getTopIntent();
        Log.d(TAG, "Top Intent: " + topIntent.getName());
        Log.d(TAG, "Entities:");
        List<LUISEntity> entities = response.getEntities();
        for (int i = 0; i < entities.size(); i++) {
            Log.d(TAG, String.valueOf(i + 1) + " - " + entities.get(i).getName());
        }
        LUISDialog dialog = response.getDialog();
        if (dialog != null) {
            Log.d(TAG, "Dialog Status: " + dialog.getStatus());
            if (!dialog.isFinished()) {
                Log.d(TAG, "Dialog prompt: " + dialog.getPrompt());
            }
        }

        if(topIntent.getName().equals(LUISIntentDictionary.INTENT_uWaterlooLocation)){
            for (final LUISEntity entity: entities){
                if (entity.getType().equals(LUISIntentDictionary.TYPE_location)){

                    break;
                }
            }
        }

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
            mMessages.add(0, message);
            notifyDataSetChanged();
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

    public class BuildingTask extends AsyncTask<String, Void, BuildingResponse>{

        @Override
        protected BuildingResponse doInBackground(String... strings) {
            return mNetworkHelper.getBuildingInfo(strings[0]);
        }

        @Override
        protected void onPostExecute(BuildingResponse buildingResponse) {
            super.onPostExecute(buildingResponse);
            mChatAdapter.addMessage(new MapChatMessage("Bot",
                    buildingResponse.latitude, buildingResponse.longitude));
        }
    }

}

