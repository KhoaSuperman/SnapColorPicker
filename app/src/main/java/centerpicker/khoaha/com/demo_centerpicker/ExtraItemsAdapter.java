package centerpicker.khoaha.com.demo_centerpicker;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ViewUtils;
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

    public ExtraItemsAdapter(ArrayList<MaterialColor> materialColors, RecyclerView recyclerView) {
        this.materialColorArrayList = materialColors;
        this.recyclerView = recyclerView;
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
            MaterialColor materialColor = materialColorArrayList.get(position - 1);
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

                }
            });
        }
    }

    static interface ColorPickerListener {
        void colorSelected(int position, int color);
    }
}
