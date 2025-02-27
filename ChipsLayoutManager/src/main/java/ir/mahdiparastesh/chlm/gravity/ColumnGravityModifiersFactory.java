package ir.mahdiparastesh.chlm.gravity;

import android.annotation.SuppressLint;
import android.util.SparseArray;
import android.view.Gravity;

import ir.mahdiparastesh.chlm.SpanLayoutChildGravity;

public class ColumnGravityModifiersFactory implements IGravityModifiersFactory {
    private final SparseArray<IGravityModifier> gravityModifierMap;

    @SuppressLint("RtlHardcoded")
    public ColumnGravityModifiersFactory() {
        gravityModifierMap = new SparseArray<>();
        CenterInColumnGravityModifier centerGravityModifier = new CenterInColumnGravityModifier();

        gravityModifierMap.put(Gravity.CENTER, centerGravityModifier);
        gravityModifierMap.put(Gravity.CENTER_HORIZONTAL, centerGravityModifier);
        gravityModifierMap.put(Gravity.LEFT, new LeftGravityModifier());
        gravityModifierMap.put(Gravity.RIGHT, new RightGravityModifier());
    }

    public IGravityModifier getGravityModifier(@SpanLayoutChildGravity int gravity) {
        IGravityModifier gravityModifier = gravityModifierMap.get(gravity);
        if (gravityModifier == null)
            gravityModifier = gravityModifierMap.get(Gravity.CENTER_HORIZONTAL);
        return gravityModifier;
    }
}
