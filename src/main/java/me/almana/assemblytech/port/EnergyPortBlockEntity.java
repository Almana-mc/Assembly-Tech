package me.almana.assemblytech.port;

import me.almana.assemblytech.registry.ModBlockEntities;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.neoforged.neoforge.transfer.energy.SimpleEnergyHandler;
import org.jetbrains.annotations.Nullable;

public class EnergyPortBlockEntity extends PortBlockEntity implements MenuProvider {

    public static final int CAPACITY = 5_000_000;
    private static final String TAG_ENERGY = "Energy";

    private final SimpleEnergyHandler energy = new SimpleEnergyHandler(CAPACITY, Integer.MAX_VALUE, Integer.MAX_VALUE) {
        @Override
        protected void onEnergyChanged(int previousAmount) {
            setChanged();
        }
    };

    public EnergyPortBlockEntity(BlockPos pos, BlockState state) {
        this(ModBlockEntities.ENERGY_PORT.get(), pos, state);
    }

    public EnergyPortBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public SimpleEnergyHandler getEnergy() {
        return energy;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("block.assemblytech.energy_port");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        return new EnergyPortMenu(containerId, inv, this);
    }

    @Override
    protected void saveAdditional(ValueOutput output) {
        super.saveAdditional(output);
        energy.serialize(output.child(TAG_ENERGY));
    }

    @Override
    protected void loadAdditional(ValueInput input) {
        super.loadAdditional(input);
        input.child(TAG_ENERGY).ifPresent(energy::deserialize);
    }
}
