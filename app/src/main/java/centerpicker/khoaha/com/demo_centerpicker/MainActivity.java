package centerpicker.khoaha.com.demo_centerpicker;

import android.graphics.Point;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Display;

public class MainActivity extends AppCompatActivity {

    private static final int NUM_ITEMS = 5;

    private float itemWidth;
    private float padding;
    private float firstItemWidth;
    private float allPixels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //calculate item width, padding
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);
        firstItemWidth = getResources().getDimension(R.dimen.padding_item_width);
        itemWidth = getResources().getDimension(R.dimen.item_width);
        padding = (size.x - itemWidth) / 2;

        //amount of scroll
        allPixels = 0;

        //set list items
        final RecyclerView items = (RecyclerView) findViewById(R.id.item_list);
        LinearLayoutManager shopItemslayoutManager = new LinearLayoutManager(getApplicationContext());
        shopItemslayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        items.setLayoutManager(shopItemslayoutManager);

        ExtraItemsAdapter adapter = new ExtraItemsAdapter(NUM_ITEMS);
        items.setAdapter(adapter);
    }
}
