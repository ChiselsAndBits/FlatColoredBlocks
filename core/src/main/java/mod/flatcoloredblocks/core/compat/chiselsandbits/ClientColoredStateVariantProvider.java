package mod.flatcoloredblocks.core.compat.chiselsandbits;

import com.communi.suggestu.scena.core.client.models.data.IBlockModelData;
import mod.chiselsandbits.api.client.variant.state.IClientStateVariantProvider;
import mod.chiselsandbits.api.variant.state.IStateVariant;
import mod.flatcoloredblocks.core.util.HoverTextUtils;
import mod.flatcoloredblocks.core.util.ModelDataUtils;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import java.util.List;

public class ClientColoredStateVariantProvider implements IClientStateVariantProvider {

    @Override
    public IBlockModelData getBlockModelData(IStateVariant iStateVariant) {
        if (!(iStateVariant instanceof ColoredStateVariant coloredStateVariant))
            return IBlockModelData.empty();

        return ModelDataUtils.createModelDataForColor(coloredStateVariant.getColor());
    }

    @Override
    public void appendHoverText(IStateVariant iStateVariant, Level level, List<Component> list, TooltipFlag tooltipFlag) {
        if (!(iStateVariant instanceof ColoredStateVariant coloredStateVariant))
            return;

        HoverTextUtils.appendColorHoverText(list, coloredStateVariant.getColor());
    }
}
