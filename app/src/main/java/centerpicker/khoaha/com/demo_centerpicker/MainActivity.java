package centerpicker.khoaha.com.demo_centerpicker;

import android.graphics.Point;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //http://www.plattysoft.com/2015/06/16/snapping-items-on-a-horizontal-list/

    private static final int NUM_ITEMS = 20;

    private float itemWidth;
    private float padding;
    private float firstItemWidth;
    private float allPixels;

    ArrayList<MaterialColor> materialColors = new ArrayList<>();

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

        items.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                allPixels += dx;
                Log.d(MyCons.LOG, "onScrolled allPixels: " + allPixels);
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                synchronized (this) {
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        calculatePositionAndScroll(recyclerView);
                    }
                }
            }
        });

        //get colors
        try{
            //
            Field[] fields = Class.forName("com.wada811.android.material.design.colors" + ".R$color").getDeclaredFields();
            for(Field field : fields) {
                String colorName = field.getName();
                if (colorName.startsWith("md_")) {
                    int colorId = field.getInt(null);
                    int color = ContextCompat.getColor(getBaseContext(), colorId);
                    materialColors.add(new MaterialColor(color, colorName));
                }
            }
        }catch(Exception e){
            Log.e("MY_WATCH", "MainActivity.onCreate" + e.getMessage());
        }

        ExtraItemsAdapter adapter = new ExtraItemsAdapter(materialColors, items);
        items.setAdapter(adapter);
    }

    private void calculatePositionAndScroll(RecyclerView recyclerView) {
        int expectedPosition = Math.round((allPixels + padding - firstItemWidth) / itemWidth);
        // Special cases for the padding items
        if (expectedPosition < 0) {
            expectedPosition = 0;
        } else if (expectedPosition >= recyclerView.getAdapter().getItemCount() - 2) {
            expectedPosition--;
        }
        scrollListToPosition(recyclerView, expectedPosition);
    }

    private void scrollListToPosition(RecyclerView recyclerView, int expectedPosition) {
        float targetScrollPos = expectedPosition * itemWidth + firstItemWidth - padding;
        float missingPx = targetScrollPos - allPixels;

        //always check missingPx then scroll until missingPx = 0
        if (missingPx != 0) {
            recyclerView.smoothScrollBy((int) missingPx, 0);
        }
    }

}
