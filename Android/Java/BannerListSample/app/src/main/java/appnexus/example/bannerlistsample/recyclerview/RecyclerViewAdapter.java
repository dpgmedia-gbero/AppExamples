package appnexus.example.bannerlistsample.recyclerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.appnexus.opensdk.AdListener;
import com.appnexus.opensdk.AdView;
import com.appnexus.opensdk.BannerAdView;
import com.appnexus.opensdk.NativeAdResponse;
import com.appnexus.opensdk.ResultCode;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import appnexus.example.bannerlistsample.R;
import appnexus.example.bannerlistsample.utils.Constants;

/**
 * The {@link RecyclerViewAdapter} class.
 * <p>The adapter provides access to the items in the {@link MemberIdItemViewHolder}
 * or the {@link AdViewHolder}.</p>
 */
class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    // A menu item view type.
    private static final int MEMBER_ID_ITEM_VIEW_TYPE = 0;

    // The banner ad view type.
    private static final int BANNER_AD_VIEW_TYPE = 1;

    // An Activity's Context.
    private final Context context;

    // The list of banner ads and member id items.
    private final List<Object> recyclerViewItems;

    private final Map<Integer, BannerAdView> cachedAdViews = new HashMap<>();

    // invoke the suitable constructor of the Adapter class
    public RecyclerViewAdapter(Context context, List<Object> recyclerViewItems) {
        this.context = context;
        this.recyclerViewItems = recyclerViewItems;
    }

    /**
     * The {@link MemberIdItemViewHolder} class.
     * Provides a reference to each view in the member id item view.
     */
    public static class MemberIdItemViewHolder extends RecyclerView.ViewHolder {

        private final TextView memberIdItem;

        MemberIdItemViewHolder(View view) {
            super(view);
            memberIdItem = view.findViewById(R.id.member_id);
        }
    }

    /**
     * The {@link AdViewHolder} class.
     */
    public static class AdViewHolder extends RecyclerView.ViewHolder {

        AdViewHolder(View view) {
            super(view);
        }
    }

    /**
     * Returns list items count.
     * @return
     */
    @Override
    public int getItemCount() {

        return recyclerViewItems.size();
    }

    /**
     * Determines the view type for the given position.
     * Perform modulus operation
     * If value is 0 then return type as banner ad else return member id view.
     * @return
     */
    @Override
    public int getItemViewType(int position) {

        return (position % RecyclerViewActivity.AD_DISPLAY_POS == 0) ? BANNER_AD_VIEW_TYPE
                : MEMBER_ID_ITEM_VIEW_TYPE;
    }

    /**
     * Creates a new view for a menu item view or a banner ad view
     * based on the viewType. This method is invoked by the layout manager.
     */
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType) {
        switch (viewType) {
            //If view is member id then insert member id layout.
            case MEMBER_ID_ITEM_VIEW_TYPE:
                View memberIdItemLayoutView = LayoutInflater.from(viewGroup.getContext()).inflate(
                        R.layout.member_id_item_container, viewGroup, false);
                return new MemberIdItemViewHolder(memberIdItemLayoutView);

            //If view is banner ad then insert banner ad layout.
            case BANNER_AD_VIEW_TYPE:
                View bannerLayoutView = LayoutInflater.from(
                        viewGroup.getContext()).inflate(R.layout.banner_ad_container,
                        viewGroup, false);
                return new AdViewHolder(bannerLayoutView);
        }
        return null;
    }

    /**
     * Replaces the content in the views that make up the member id item view and the
     * banner ad view. This method is invoked by the layout manager.
     */
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        int viewType = getItemViewType(position);
        switch (viewType) {

            //If view type is member id
            case MEMBER_ID_ITEM_VIEW_TYPE:
                MemberIdItemViewHolder menuItemHolder = (MemberIdItemViewHolder) holder;
                menuItemHolder.memberIdItem.setText((String) recyclerViewItems.get(position));
                break;

            //If view type is banner ad
            case BANNER_AD_VIEW_TYPE:
                AdViewHolder bannerHolder = (AdViewHolder) holder;
//                (BannerAdView) recyclerViewItems.get(position)
                BannerAdView bannerAdView = null;
                if(cachedAdViews.containsKey(position)) {
                    bannerAdView = cachedAdViews.get(position);
                } else {
                    bannerAdView = createBannerAdView(holder.itemView.getContext());
                    cachedAdViews.put(position, bannerAdView);
                }

                ViewGroup adCardView = (ViewGroup) bannerHolder.itemView;

                // The AdViewHolder recycled by the RecyclerView may be a different
                // instance than the one used previously for this position. Clear the
                // AdViewHolder of any subviews in case it has a different
                // BannerAd associated with it, and make sure the BannerAd for this position doesn't
                // already have a parent of a different recycled AdViewHolder.
                if (bannerAdView.getParent() != null) {
                    ((ViewGroup) bannerAdView.getParent()).removeView(bannerAdView);
                }

                // Add the banner ad to the ad view.
                adCardView.addView(bannerAdView);
                break;
        }
    }

    private BannerAdView createBannerAdView(Context context) {

        // create an instance of banner.
        final BannerAdView bannerAdView = new BannerAdView(context);

        // Set a placement id.
        bannerAdView.setPlacementID(Constants.PLACEMENT_ID);

        // Get a 300x50 ad.
        bannerAdView.setAdSize(300, 50);

        // Set to 0 to disable auto-refresh.
        bannerAdView.setAutoRefreshInterval(0);

        // Turning this on so we always get an ad during testing.
        bannerAdView.setShouldServePSAs(true);

        // Set whether ads will expand to fit the screen width.
        bannerAdView.setExpandsToFitScreenWidth(true);

        //Set ad listener and load ad.
        bannerAdView.loadAd();

        // Set an AdListener on the AdView.
        bannerAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded(AdView adView) {}

            @Override
            public void onAdLoaded(NativeAdResponse nativeAdResponse) {}

            @Override
            public void onAdRequestFailed(AdView adView, ResultCode resultCode) {}

            @Override
            public void onAdExpanded(AdView adView) {}

            @Override
            public void onAdCollapsed(AdView adView) {}

            @Override
            public void onAdClicked(AdView adView) {}

            @Override
            public void onAdClicked(AdView adView, String s) {}

            @Override
            public void onLazyAdLoaded(AdView adView) {}

            @Override
            public void onAdImpression(AdView adView) {}
        });

        return bannerAdView;

    }
}
