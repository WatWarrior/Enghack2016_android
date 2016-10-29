package com.watwarrior.watchatbot.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;

import com.watwarrior.watchatbot.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }



    public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ChatViewHolder>{


        @Override
        public ChatViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return null;
        }

        @Override
        public void onBindViewHolder(ChatViewHolder holder, int position) {

        }

        @Override
        public int getItemCount() {
            return 0;
        }

        public class ChatViewHolder extends RecyclerView.ViewHolder{



            public ChatViewHolder(View itemView) {
                super(itemView);
            }
        }

    }


}
