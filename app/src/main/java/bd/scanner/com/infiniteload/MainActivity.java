package bd.scanner.com.infiniteload;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {



    //arrayList to hold list view items

    ArrayList<String> myListItems;



    // list view which will show our list items

    ListView listView;



    //it will tell us weather to load more items or not

    boolean loadingMore = false;



    //array adpter to set list items

    ArrayAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        listView = (ListView) findViewById(R.id.listview);

        myListItems = new ArrayList<String>();

        View footerView = ((LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE)).inflate(R.layout.loading_view,null,false);
        listView.addFooterView(footerView);
        //create and adapter
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, myListItems);
        //setting adapter to list view

        listView.setAdapter(adapter);

        listView.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView absListView, int i) {

            }

            @Override
            public void onScroll(AbsListView absListView, int i, int i1, int i2) {

                int lastInScreen = i + i1;

                if ((lastInScreen == i2) && !(loadingMore)){
                    Thread thread = new Thread(null, loadingMoreListItems);
                    thread.start();
                }

            }
        });
    }

    Runnable loadingMoreListItems = new Runnable() {
        @Override
        public void run() {

            loadingMore = true;

            myListItems = new ArrayList<>();

            try{
                Thread.sleep(1000);
            }catch (InterruptedException e){
                e.printStackTrace();
            }
            //Get 10 new list items
            for (int i = 0; i < 10; i++) {
                //Fill the item with some bogus information

                myListItems.add("item" + i);

            }

            runOnUiThread(returnRes);
        }
    };

    Runnable returnRes = new Runnable() {
        @Override
        public void run() {
            //Loop through the new items and add them to the adapter

            if (myListItems != null && myListItems.size() > 0) {



                for (int i = 0; i < myListItems.size(); i++)

                    adapter.add(myListItems.get(i));

            }



            //Update the Application title

            setTitle("Never ending List with " + String.valueOf(adapter.getCount()) + " items");



            //Tell to the adapter that changes have been made, this will cause the list to refresh

            adapter.notifyDataSetChanged();



            //Done loading more.

            loadingMore = false;

        }



    };
}
