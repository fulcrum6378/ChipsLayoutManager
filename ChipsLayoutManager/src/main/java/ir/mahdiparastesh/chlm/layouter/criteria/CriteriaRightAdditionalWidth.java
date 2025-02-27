package ir.mahdiparastesh.chlm.layouter.criteria;

import ir.mahdiparastesh.chlm.layouter.AbstractLayouter;

class CriteriaRightAdditionalWidth extends FinishingCriteriaDecorator {

    private final int additionalWidth;

    CriteriaRightAdditionalWidth(IFinishingCriteria finishingCriteria, int additionalWidth) {
        super(finishingCriteria);
        this.additionalWidth = additionalWidth;
    }

    @Override
    public boolean isFinishedLayouting(AbstractLayouter abstractLayouter) {
        int rightBorder = abstractLayouter.getCanvasRightBorder();
        return super.isFinishedLayouting(abstractLayouter) &&
                //if additional height filled
                abstractLayouter.getViewLeft() > rightBorder + additionalWidth;
    }

}
