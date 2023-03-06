package appnexus.example.bannerlistsample.recyclerview;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.appnexus.opensdk.AdListener;
import com.appnexus.opensdk.AdView;
import com.appnexus.opensdk.BannerAdView;
import com.appnexus.opensdk.NativeAdResponse;
import com.appnexus.opensdk.ResultCode;
import java.util.ArrayList;
import java.util.List;
import appnexus.example.bannerlistsample.R;
import appnexus.example.bannerlistsample.utils.Constants;

public class RecyclerViewActivity extends AppCompatActivity {

    // A banner ad is placed in every 11th position in the RecyclerView.
    public static final int AD_DISPLAY_POS = 11;

    // List of banner ads and member ids list items that populate the RecyclerView.
    private final List<Object> recyclerViewItems = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.recyclerview_activity);

        RecyclerView recyclerView = findViewById(R.id.recycler_view);

        //  if changes in content do not change the layout size of the RecyclerView.
        recyclerView.setHasFixedSize(true);

        // Specify the layout as Linear layout.
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // Update the RecyclerView item's list with member ids items and banner ads.
        addMemberId();

        // Specify an adapter.
        RecyclerView.Adapter<RecyclerView.ViewHolder> adapter = new RecyclerViewAdapter(this,
                recyclerViewItems);
        recyclerView.setAdapter(adapter);

    }


    /**
     * Add 20 member ids in list.
     */
    private void addMemberId() {
        for (int i = 1; i <= 200; i++) {
            recyclerViewItems.add("Member Id " + i);
        }
    }
}
