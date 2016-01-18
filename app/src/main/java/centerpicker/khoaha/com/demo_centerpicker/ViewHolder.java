package centerpicker.khoaha.com.demo_centerpicker;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

public class ViewHolder extends RecyclerView.ViewHolder {

    TextView text;
    View container;

    public ViewHolder(View itemView) {
        super(itemView);
        text = (TextView) itemView.findViewById(R.id.item_text);
        container = itemView.findViewById(R.id.container);
    }
}
