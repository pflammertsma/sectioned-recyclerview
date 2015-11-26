package com.afollestad.sectionedrecyclerview;

import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.ViewGroup;

import java.util.List;

/**
 * @author Aidan Follestad (afollestad)
 */
public abstract class SectionedRecyclerViewAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private final static int VIEW_TYPE_HEADER = 0;
    private final static int VIEW_TYPE_ITEM = 1;

    private final ArrayMap<Integer, Integer> mHeaderLocationMap;
    private final ArrayMap<Integer, Integer> mSectionSizeMap;
    private GridLayoutManager mLayoutManager;

    public SectionedRecyclerViewAdapter() {
        mHeaderLocationMap = new ArrayMap<>();
        mSectionSizeMap = new ArrayMap<>();
    }

    public abstract int getSectionCount();

    public abstract int getItemCount(int section);

    public abstract VH onCreateViewHolder(ViewGroup parent, boolean header);

    public abstract void onBindHeaderViewHolder(VH holder, int section);

    public abstract void onBindViewHolder(VH holder, int section, int relativePosition, int absolutePosition);

    public final boolean isHeader(int position) {
        return mHeaderLocationMap.get(position) != null;
    }

    public final void setLayoutManager(@Nullable GridLayoutManager lm) {
        mLayoutManager = lm;
        if (lm == null) return;
        lm.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (isHeader(position))
                    return mLayoutManager.getSpanCount();
                return 1;
            }
        });
    }

    private int lookupSection(int itemPosition) {
        synchronized (mHeaderLocationMap) {
            boolean inBounds = false;
            Integer lastKey = null;
            for (Integer key : mHeaderLocationMap.keySet()) {
                if (inBounds && key > itemPosition) {
                    return mHeaderLocationMap.get(lastKey);
                } else if (itemPosition >= key) {
                    inBounds = true;
                }
                lastKey = key;
            }
            return -1;
        }
    }

    // returns section along with offsetted position
    private int[] getOffsettedSectionAndPosition(int itemPosition) {
        synchronized (mHeaderLocationMap) {
            boolean inBounds = false;
            Integer lastKey = null;
            for (Integer key : mHeaderLocationMap.keySet()) {
                if (inBounds && key > itemPosition) {
                    return new int[]{mHeaderLocationMap.get(lastKey), itemPosition - lastKey - 1};
                } else if (itemPosition >= key) {
                    inBounds = true;
                }
                lastKey = key;
            }
            return new int[]{-1, -1};
        }
    }

    @Override
    public final int getItemCount() {
        int count = 0;
        mHeaderLocationMap.clear();
        mSectionSizeMap.clear();
        for (int s = 0; s < getSectionCount(); s++) {
            mHeaderLocationMap.put(count, s);
            final int itemCount = getItemCount(s);
            mSectionSizeMap.put(s, itemCount);
            count += itemCount + 1;
        }
        return count;
    }

    /**
     * @hide
     * @deprecated
     */
    @Override
    @Deprecated
    public final VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateViewHolder(parent, viewType == VIEW_TYPE_HEADER);
    }

    /**
     * @hide
     * @deprecated
     */
    @Deprecated
    @Override
    public final int getItemViewType(int position) {
        if (isHeader(position)) {
            return VIEW_TYPE_HEADER;
        } else {
            return VIEW_TYPE_ITEM;
        }
    }

    /**
     * @hide
     * @deprecated
     */
    @Override
    @Deprecated
    public final void onBindViewHolder(VH holder, int position) {
        StaggeredGridLayoutManager.LayoutParams layoutParams = null;
        if (holder.itemView.getLayoutParams() instanceof GridLayoutManager.LayoutParams)
            layoutParams = new StaggeredGridLayoutManager.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        else if (holder.itemView.getLayoutParams() instanceof StaggeredGridLayoutManager.LayoutParams)
            layoutParams = (StaggeredGridLayoutManager.LayoutParams) holder.itemView.getLayoutParams();
        if (isHeader(position)) {
            if (layoutParams != null) layoutParams.setFullSpan(true);
            onBindHeaderViewHolder(holder, mHeaderLocationMap.get(position));
        } else {
            if (layoutParams != null) layoutParams.setFullSpan(false);
            final int[] sectionAndPos = getOffsettedSectionAndPosition(position);
            onBindViewHolder(holder, sectionAndPos[0],
                    // offset section view positions
                    sectionAndPos[1],
                    position - (sectionAndPos[0] + 1));
        }
        if (layoutParams != null)
            holder.itemView.setLayoutParams(layoutParams);
    }

    /**
     * @hide
     * @deprecated
     */
    @Deprecated
    @Override
    public final void onBindViewHolder(VH holder, int position, List<Object> payloads) {
        super.onBindViewHolder(holder, position, payloads);
    }
}