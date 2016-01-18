package centerpicker.khoaha.com.demo_centerpicker;

import android.content.Context;
import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.nineoldandroids.animation.Animator;
import com.nineoldandroids.animation.AnimatorListenerAdapter;
import com.nineoldandroids.animation.ObjectAnimator;
import com.nineoldandroids.view.ViewHelper;

import java.util.ArrayList;
import java.util.Random;

public class ExtraItemsAdapter extends RecyclerView.Adapter<ViewHolder> {
    private static final int VIEW_TYPE_PADDING = 1;
    private static final int VIEW_TYPE_ITEM = 2;

    ArrayList<MaterialColor> materialColorArrayList = new ArrayList<>();
    RecyclerView recyclerView;
    Context context;

    private float firstItemWidth;
    private float itemWidth;
    private float padding;

    private float allPixels = 0;

    private ColorPickerListener colorPickerListener;

    public ExtraItemsAdapter(ArrayList<MaterialColor> materialColors, RecyclerView recyclerView, float parentWidth) {
        this.materialColorArrayList = materialColors;
        this.recyclerView = recyclerView;

        context = recyclerView.getContext();
        this.firstItemWidth = context.getResources().getDimension(R.dimen.padding_item_width);
        this.itemWidth = context.getResources().getDimension(R.dimen.item_width);
        this.padding = (parentWidth - itemWidth) / 2;

        //measure scroll to center
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
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

        //default click listener
        colorPickerListener = new ColorPickerListener() {
            @Override
            public void colorSelected(int position, int color, int previousColor) {

            }
        };
    }

    @Override
    public int getItemCount() {
        return materialColorArrayList.size() + 2; // We have to add 2 paddings
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0 || position == getItemCount() - 1) {
            return VIEW_TYPE_PADDING;
        }
        return VIEW_TYPE_ITEM;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        if (viewType == VIEW_TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent, false);
            return new ViewHolder(v);
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_padding, parent, false);
            return new ViewHolder(v);
        }
    }

    int selection = -1;
    int previous = -1;

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        if (getItemViewType(position) == VIEW_TYPE_ITEM) {
            // We bind the item to the view
            final MaterialColor materialColor = materialColorArrayList.get(position - 1);
            holder.container.setBackgroundColor(materialColor.color);
            holder.text.setText(materialColor.colorName);

            if (selection == position) {
                ObjectAnimator scale = ObjectAnimator.ofFloat(holder.container, "scaleY", 1.0f, 1.3f);
                scale.setDuration(300);
                scale.setInterpolator(new AccelerateDecelerateInterpolator());
                scale.start();
            } else if (position == previous) {
                ObjectAnimator scale = ObjectAnimator.ofFloat(holder.container, "scaleY", 1.3f, 1.0f);
                scale.setDuration(300);
                scale.setInterpolator(new AccelerateDecelerateInterpolator());
                scale.start();
            } else {
                ObjectAnimator scale = ObjectAnimator.ofFloat(holder.container, "scaleY", 1.3f, 1.0f);
                scale.setDuration(0);
                scale.setInterpolator(new AccelerateDecelerateInterpolator());
                scale.start();
            }

            holder.container.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (selection != -1) {
                        previous = selection;
                    }
                    selection = position;

                    notifyDataSetChanged();

                    //scroll item to center
                    scrollListToPosition(recyclerView, position - 1);

                    //rise click event
                    int selectedColor = materialColorArrayList.get(position - 1).color;
                    int previousColor = previous > 0 ? materialColorArrayList.get(previous - 1).color : 0;
                    colorPickerListener.colorSelected(position, selectedColor, previousColor);
                }
            });
        }
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

    public void setColorPickerListener(ColorPickerListener colorPickerListener) {
        this.colorPickerListener = colorPickerListener;
    }

    static interface ColorPickerListener {
        void colorSelected(int position, int color, int previousColor);
    }
}
