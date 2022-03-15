package io.github.endertitan.echolib.common.blocks;

import io.github.endertitan.echolib.common.tiles.BoltHandlerTile;
import io.github.endertitan.echolib.resoucenetworks.Network;
import io.github.endertitan.echolib.resoucenetworks.interfaces.INetworkBlock;
import io.github.endertitan.echolib.resoucenetworks.interfaces.INetworkMember;
import net.minecraft.block.BlockState;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.StringTextComponent;
import net.minecraft.world.World;

public class BoltHandlerBlock extends BaseHorizontalBlock implements INetworkBlock<BoltHandlerTile> {

    public BoltHandlerBlock(Properties properties) {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState state, World world, BlockPos pos, BlockState p_220082_4_, boolean p_220082_5_) {
        INetworkBlock.super.update(world, pos, "bolt");
    }

    @Override
    public void onRemove(BlockState state, World world, BlockPos pos, BlockState newState, boolean isMoving) {
        INetworkBlock.super.removeUpdate(world, pos, state, "bolt");
        super.onRemove(state, world, pos, newState, isMoving);
    }


    @Override
    protected void interactWith(World world, BlockPos pos, PlayerEntity player) {

        if (world.isClientSide)
            return;

        if (world.getBlockEntity(pos) instanceof INetworkMember && ((INetworkMember) world.getBlockEntity(pos)).getNetwork() != null) {

            Network<BoltHandlerTile> network = (Network<BoltHandlerTile>) ((INetworkMember) world.getBlockEntity(pos)).getNetwork();

            player.sendMessage(new StringTextComponent("----------"), player.getUUID());

            for (INetworkMember member : network.participants)
                player.sendMessage(new StringTextComponent(String.format("%s:%s", network.id, member.getAmount())), player.getUUID());

        } else {
            player.sendMessage(new StringTextComponent("This block has no network"), player.getUUID());
        }
    }

    @Override
    public boolean hasTileEntity(BlockState state) {
        return true;
    }

}