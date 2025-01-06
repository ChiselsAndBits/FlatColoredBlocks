package mod.flatcoloredblocks.forge.compat.chiselsandbits;

import com.communi.suggestu.scena.core.dist.Dist;
import com.communi.suggestu.scena.core.dist.DistExecutor;
import mod.chiselsandbits.api.client.variant.state.IClientStateVariantManager;
import mod.chiselsandbits.api.plugin.ChiselsAndBitsPlugin;
import mod.chiselsandbits.api.plugin.IChiselsAndBitsPlugin;
import mod.flatcoloredblocks.core.compat.chiselsandbits.ClientColoredStateVariantProvider;
import mod.flatcoloredblocks.core.compat.chiselsandbits.FCBCandBPlugin;
import mod.flatcoloredblocks.core.compat.chiselsandbits.StateVariantProviders;
import mod.flatcoloredblocks.core.registrars.Blocks;

@ChiselsAndBitsPlugin
public class ForgeFCBCandBPlugin implements IChiselsAndBitsPlugin {

    @Override
    public String getId() {
        return "flat-colored-blocks";
    }

    @Override
    public void onConstruction() {
        (new FCBCandBPlugin()).onConstruction();
    }

    @Override
    public void onClientConstruction() {
        (new FCBCandBPlugin()).onClientConstruction();
    }
}
