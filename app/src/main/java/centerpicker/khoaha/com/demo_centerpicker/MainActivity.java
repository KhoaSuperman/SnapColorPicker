package centerpicker.khoaha.com.demo_centerpicker;

import android.content.res.ColorStateList;
import android.graphics.Point;
import android.os.Handler;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Display;
import android.widget.ImageView;

import java.lang.reflect.Field;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //http://www.plattysoft.com/2015/06/16/snapping-items-on-a-horizontal-list/

    ImageView ivImage;
    ImageView ivPrevious;

    ArrayList<MaterialColor> materialColors = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ivImage = (ImageView) findViewById(R.id.ivImage);
        ivPrevious = (ImageView) findViewById(R.id.ivPrevious);

        //calculate item width, padding
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        //set list items
        final RecyclerView items = (RecyclerView) findViewById(R.id.item_list);
        LinearLayoutManager shopItemslayoutManager = new LinearLayoutManager(getApplicationContext());
        shopItemslayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        items.setLayoutManager(shopItemslayoutManager);

        generateColors();

        ExtraItemsAdapter adapter = new ExtraItemsAdapter(materialColors, items, size.x);
        adapter.setColorPickerListener(new ExtraItemsAdapter.ColorPickerListener() {
            @Override
            public void colorSelected(int position, int color, int previousColor) {
                ivImage.setColorFilter(color);
                ivPrevious.setColorFilter(previousColor);
            }
        });
        items.setAdapter(adapter);
    }

    private void generateColors() {
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
    }

}
