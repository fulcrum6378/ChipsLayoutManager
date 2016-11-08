package com.beloo.widget.chipslayoutmanager.layouter;

import android.graphics.Rect;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.beloo.widget.chipslayoutmanager.ChipsLayoutManager;
import com.beloo.widget.chipslayoutmanager.cache.IViewCacheStorage;
import com.beloo.widget.chipslayoutmanager.layouter.criteria.DefaultCriteriaFactory;
import com.beloo.widget.chipslayoutmanager.layouter.criteria.DisappearingCriteriaFactory;
import com.beloo.widget.chipslayoutmanager.layouter.criteria.ICriteriaFactory;
import com.beloo.widget.chipslayoutmanager.layouter.criteria.InfiniteCriteria;
import com.beloo.widget.chipslayoutmanager.layouter.placer.DisappearingViewAtEndPlacer;
import com.beloo.widget.chipslayoutmanager.layouter.placer.DisappearingViewAtStartPlacer;
import com.beloo.widget.chipslayoutmanager.layouter.placer.RealAtEndPlacer;
import com.beloo.widget.chipslayoutmanager.layouter.placer.RealAtStartPlacer;

import java.util.ArrayList;
import java.util.List;

public abstract class AbstractLayouterFactory {
    ChipsLayoutManager layoutManager;
    private IViewCacheStorage cacheStorage;
    @Nullable
    private Integer maxViewsInRow = null;

    @IntRange(from = 0)
    private int additionalRowsCount;

    private List<ILayouterListener> layouterListeners = new ArrayList<>();

    private int additionalHeight;

    AbstractLayouterFactory(IViewCacheStorage cacheStorage, ChipsLayoutManager layoutManager) {
        this.cacheStorage = cacheStorage;
        this.layoutManager = layoutManager;
    }

    public void addLayouterListener(@Nullable ILayouterListener layouterListener) {
        if (layouterListener != null) {
            layouterListeners.add(layouterListener);
        }
    }

    public void setMaxViewsInRow(@Nullable Integer maxViewsInRow) {
        this.maxViewsInRow = maxViewsInRow;
    }

    public void setAdditionalHeight(@IntRange(from = 0) int additionalHeight) {
        if (additionalHeight < 0) throw new IllegalArgumentException("additional height can't be negative");
        this.additionalHeight = additionalHeight;
    }

    public void setAdditionalRowsCount(int additionalRowsCount) {
        this.additionalRowsCount = additionalRowsCount;
    }

    private int getAdditionalRowsCount() {
        return additionalRowsCount;
    }

    @Nullable
    private Integer getMaxViewsInRow() {
        return maxViewsInRow;
    }

    private int getAdditionalHeight() {
        return additionalHeight;
    }

    abstract AbstractLayouter.Builder createUpBuilder();
    abstract AbstractLayouter.Builder createDownBuilder();
    abstract Rect createOffsetRectForUpLayouter(Rect anchorRect);
    abstract Rect createOffsetRectForDownLayouter(Rect anchorRect);

    @NonNull
    private AbstractLayouter.Builder fillBasicBuilder(AbstractLayouter.Builder builder) {
        return builder.layoutManager(layoutManager)
                .canvas(new Square(layoutManager))
                .childGravityResolver(layoutManager.getChildGravityResolver())
                .cacheStorage(cacheStorage)
                .maxCountInRow(getMaxViewsInRow())
                .addLayouterListeners(layouterListeners);
    }

    @NonNull
    public final ILayouter getUpLayouter(@Nullable Rect anchorRect) {
        ICriteriaFactory criteriaFactory = new DefaultCriteriaFactory(getAdditionalHeight());

        return fillBasicBuilder(createUpBuilder())
                .offsetRect(createOffsetRectForUpLayouter(anchorRect))
                .finishingCriteria(criteriaFactory.getUpFinishingCriteria())
                .placer(new RealAtStartPlacer(layoutManager))
                .build();
    }

    @NonNull
    public final ILayouter getDownLayouter(@Nullable Rect anchorRect) {
        ICriteriaFactory criteriaFactory = new DefaultCriteriaFactory(getAdditionalHeight());

        return fillBasicBuilder(createDownBuilder())
                .offsetRect(createOffsetRectForUpLayouter(anchorRect))
                .finishingCriteria(criteriaFactory.getDownFinishingCriteria())
                .placer(new RealAtEndPlacer(layoutManager))
                .build();
    }

    @NonNull
    public final ILayouter getDisappearingDownLayouter(@Nullable Rect anchorRect) {
        ICriteriaFactory criteriaFactory = new DisappearingCriteriaFactory(getAdditionalRowsCount());

        return fillBasicBuilder(createDownBuilder())
                .offsetRect(createOffsetRectForDownLayouter(anchorRect))
                .finishingCriteria(criteriaFactory.getDownFinishingCriteria())
                .placer(new DisappearingViewAtEndPlacer(layoutManager))
                .build();
    }

    @NonNull
    public final ILayouter getDisappearingUpLayouter(@Nullable Rect anchorRect) {
        ICriteriaFactory criteriaFactory = new DisappearingCriteriaFactory(getAdditionalRowsCount());

        return fillBasicBuilder(createUpBuilder())
                .offsetRect(createOffsetRectForUpLayouter(anchorRect))
                .finishingCriteria(criteriaFactory.getUpFinishingCriteria())
                .placer(new DisappearingViewAtStartPlacer(layoutManager))
                .build();
    }

    @NonNull
    public final ILayouter getDisappearingDownLayouter(@NonNull ILayouter layouter) {
        ICriteriaFactory criteriaFactory = new DisappearingCriteriaFactory(getAdditionalRowsCount());

        AbstractLayouter abstractLayouter = (AbstractLayouter) layouter;
        abstractLayouter.setFinishingCriteria(criteriaFactory.getDownFinishingCriteria());
        abstractLayouter.setPlacer(new DisappearingViewAtEndPlacer(layoutManager));

//        return fillBasicBuilder(createDownBuilder())
//                .offsetRect(((AbstractLayouter)layouter).getOffsetRect())
//                .finishingCriteria(criteriaFactory.getDownFinishingCriteria())
//                .placer(new DisappearingViewBottomPlacer(layoutManager))
//                .build();

        return abstractLayouter;
    }

    @NonNull
    public final ILayouter getDisappearingUpLayouter(@NonNull ILayouter layouter) {
        ICriteriaFactory criteriaFactory = new DisappearingCriteriaFactory(getAdditionalRowsCount());

        AbstractLayouter abstractLayouter = (AbstractLayouter) layouter;
        abstractLayouter.setFinishingCriteria(criteriaFactory.getUpFinishingCriteria());
        abstractLayouter.setPlacer(new DisappearingViewAtStartPlacer(layoutManager));

        return abstractLayouter;
    }


    @NonNull
    public ILayouter buildInfiniteLayouter(ILayouter layouter) {
        ((AbstractLayouter)layouter).setFinishingCriteria(new InfiniteCriteria());
        return layouter;
    }
}
