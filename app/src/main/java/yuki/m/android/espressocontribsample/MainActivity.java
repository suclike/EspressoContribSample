/*
 * Copyright 2015 yuki312 All Right Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package yuki.m.android.espressocontribsample;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Arrays;
import java.util.List;


public class MainActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.list);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(new RecyclerViewAdapter(recyclerView));
    }

    private static class RecyclerViewAdapter
            extends RecyclerView.Adapter<RecyclerViewAdapter.MainViewHolder> {
        private List<String> datasource
                = Arrays.asList("data1", "data2", "data3", "data4", "data5", "data6", "data7", "data8", "data9");
        private RecyclerView recyclerView;

        static class MainViewHolder extends RecyclerView.ViewHolder {
            private TextView titleTextView;

            public MainViewHolder(View itemView) {
                super(itemView);
                titleTextView = (TextView)itemView.findViewById(android.R.id.text1);
            }
        }

        public RecyclerViewAdapter(RecyclerView recyclerView) {
            this.recyclerView = recyclerView;
        }

        @Override
        public RecyclerViewAdapter.MainViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext())
                    .inflate(android.R.layout.simple_list_item_1, parent, false);
            v.setOnClickListener(itemClickListener);
            return new MainViewHolder(v);
        }

        private final View.OnClickListener itemClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "click pos " + recyclerView.getChildPosition(v), Toast.LENGTH_SHORT).show();
            }
        };

        @Override
        public void onBindViewHolder(RecyclerViewAdapter.MainViewHolder view, int position) {
            view.titleTextView.setText(datasource.get(position));
        }

        @Override
        public int getItemCount() {
            return datasource.size();
        }
    }
}
