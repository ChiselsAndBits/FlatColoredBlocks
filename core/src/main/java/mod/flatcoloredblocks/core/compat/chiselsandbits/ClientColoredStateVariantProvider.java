package mod.flatcoloredblocks.core.compat.chiselsandbits;

import com.communi.suggestu.scena.core.client.models.data.IBlockModelData;
import mod.chiselsandbits.api.client.variant.state.IClientStateVariantProvider;
import mod.chiselsandbits.api.variant.state.IStateVariant;
import mod.flatcoloredblocks.core.util.ModelDataUtils;

public class ClientColoredStateVariantProvider implements IClientStateVariantProvider {

    @Override
    public IBlockModelData getBlockModelData(IStateVariant iStateVariant) {
        if (!(iStateVariant instanceof ColoredStateVariant coloredStateVariant))
            return IBlockModelData.empty();

        return ModelDataUtils.createModelDataForColor(coloredStateVariant.getColor());
    }
}
