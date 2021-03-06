package com.example.fry.salvavidapp;

import android.support.annotation.RequiresApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class MainActivity extends Activity {

    Boolean flag_delete = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lista);
        final ListView listview = findViewById(R.id.listalarms);
        AlarmsDB db = new AlarmsDB( getApplicationContext() );
        final ArrayList<ArrayList<String>> list_lists = db.getall();
        final ArrayList<String> list_names = new ArrayList<>();
        final ArrayList<String> list_ids = new ArrayList<>();
        for(int i=0;i<list_lists.size();i++){
            list_names.add(String.valueOf(list_lists.get(i).get(1)));
            list_ids.add(String.valueOf(list_lists.get(i).get(0)));
        }
        final StableArrayAdapter adapter = new StableArrayAdapter(this, android.R.layout.simple_list_item_1, list_names);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                if (flag_delete){
                    try{
                        view.animate().setDuration(1000).alpha(0).withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                String real_id = list_ids.get(position);
                                AlarmsDB db = new AlarmsDB( getApplicationContext() );
                                db.eliminar(Integer.valueOf(real_id));
                                list_lists.clear();
                                list_names.clear();
                                list_ids.clear();
                                final ArrayList<ArrayList<String>> list_lists = db.getall();
                                for(int i=0;i<list_lists.size();i++){
                                    list_names.add(String.valueOf(list_lists.get(i).get(1)));
                                    list_ids.add(String.valueOf(list_lists.get(i).get(0)));
                                }
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
                else{
                    changeView(list_ids, position, list_lists);
                }
            }
        });
        // Button CREATE
        Button button_create = findViewById(R.id.button);
        button_create.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent myIntent = new Intent(getApplicationContext(), PantallaPrincipal.class);
                startActivity(myIntent);
                finish();
            }
        });
        // Button DELETE
        final Button button_delete = findViewById(R.id.button2);
        button_delete.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                flag_delete = (!flag_delete);
                if(flag_delete){
                    Toast.makeText(getApplicationContext(), "Press to delete.", Toast.LENGTH_SHORT).show();
                    button_delete.setBackgroundResource(R.mipmap.delete1);
                }
                else{
                    Toast.makeText(getApplicationContext(), "Not deleting anymore.", Toast.LENGTH_SHORT).show();
                    button_delete.setBackgroundResource(R.mipmap.delete0);
                }
            }
        });
    }


    /*
        Change view
     */
    public void changeView(final ArrayList<String> list_ids, int position, ArrayList<ArrayList<String>> list_lists){
        Intent myIntent = new Intent(this, PantallaPrincipal.class);
        Bundle b = new Bundle();
        AlarmsDB db = new AlarmsDB( getApplicationContext() );
        list_lists = db.getall();
        String real_id = list_ids.get(position);
        for (int j=0;j<list_lists.size();j++){
            if (list_lists.get(j).get(0).equals(real_id)){
                b.putString("id", real_id);
                b.putString("name", String.valueOf(list_lists.get(j).get(1)));
                b.putString("message", String.valueOf(list_lists.get(j).get(2)));
                b.putString("sms", String.valueOf(list_lists.get(j).get(3)));
                b.putString("email", String.valueOf(list_lists.get(j).get(4)));
                b.putString("contacto", String.valueOf(list_lists.get(j).get(5)));
                b.putString("timer", String.valueOf(list_lists.get(j).get(6)));
                myIntent.putExtras(b);
                startActivity(myIntent);
                finish();
                break;
            }
        }
    }


    /*
        Adapter for list view
     */
    private class StableArrayAdapter extends ArrayAdapter<String> {
        HashMap<String, Integer> mIdMap = new HashMap<>();
        private StableArrayAdapter(Context context, int textViewResourceId,List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent){
            View view = super.getView(position, convertView, parent);
            TextView tv = view.findViewById(android.R.id.text1);
            tv.setTextSize(TypedValue.COMPLEX_UNIT_DIP,25);
            return view;
        }
    }


}