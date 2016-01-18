package centerpicker.khoaha.com.demo_centerpicker;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_ITEMS = 5;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        final RecyclerView items = (RecyclerView) findViewById(R.id.item_list);
        LinearLayoutManager shopItemslayoutManager = new LinearLayoutManager(getApplicationContext());
        shopItemslayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        items.setLayoutManager(shopItemslayoutManager);

        ExtraItemsAdapter adapter = new ExtraItemsAdapter(NUM_ITEMS);
        items.setAdapter(adapter);
    }
}
