package ir.mahdiparastesh.chlm.layouter.criteria;

import androidx.annotation.NonNull;

public class PreLayoutCriteriaFactory implements ICriteriaFactory {

    private final int additionalHeight;
    private final int additionalRowsCount;

    public PreLayoutCriteriaFactory(int additionalHeight, int additionalRowsCount) {
        this.additionalHeight = additionalHeight;
        this.additionalRowsCount = additionalRowsCount;
    }

    @NonNull
    @Override
    public IFinishingCriteria getBackwardFinishingCriteria() {
        return new CriteriaUpAdditionalHeight(new CriteriaUpLayouterFinished(), additionalHeight);
    }

    @NonNull
    @Override
    public IFinishingCriteria getForwardFinishingCriteria() {
        return new CriteriaAdditionalRow(
                new CriteriaDownAdditionalHeight(new CriteriaDownLayouterFinished(), additionalHeight),
                additionalRowsCount);
    }
}
