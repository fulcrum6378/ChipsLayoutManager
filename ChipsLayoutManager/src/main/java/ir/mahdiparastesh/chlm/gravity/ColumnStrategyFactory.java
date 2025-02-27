package ir.mahdiparastesh.chlm.gravity;

import ir.mahdiparastesh.chlm.ChipsLayoutManager;
import ir.mahdiparastesh.chlm.RowStrategy;

public class ColumnStrategyFactory implements IRowStrategyFactory {
    @Override
    public IRowStrategy createRowStrategy(@RowStrategy int rowStrategy) {
        switch (rowStrategy) {
            case ChipsLayoutManager.STRATEGY_CENTER:
                return new ColumnFillSpaceCenterStrategy();
            case ChipsLayoutManager.STRATEGY_CENTER_DENSE:
                return new ColumnFillSpaceCenterDenseStrategy();
            case ChipsLayoutManager.STRATEGY_FILL_SPACE:
                return new ColumnFillSpaceStrategy();
            case ChipsLayoutManager.STRATEGY_FILL_VIEW:
                return new ColumnFillStrategy();
            case ChipsLayoutManager.STRATEGY_DEFAULT:
            default:
                return new EmptyRowStrategy();
        }
    }
}
