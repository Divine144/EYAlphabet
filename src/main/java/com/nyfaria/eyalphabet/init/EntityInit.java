package com.nyfaria.eyalphabet.init;

import com.nyfaria.eyalphabet.entity.*;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.MobCategory;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraftforge.event.entity.EntityAttributeCreationEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

import static com.nyfaria.eyalphabet.EYAlphabet.MODID;

@Mod.EventBusSubscriber(bus = Mod.EventBusSubscriber.Bus.MOD, modid = MODID)
public class EntityInit {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, MODID);
    private static final List<AttributesRegister<?>> attributeSuppliers = new ArrayList<>();

    public static final RegistryObject<EntityType<AlphabetEntity>> ALPHABET_ENTITY = registerEntity("letter", () -> EntityType.Builder.of(AlphabetEntity::new, MobCategory.CREATURE).sized(0.7F, 0.7F), AlphabetEntity::createAttributes);
    public static final RegistryObject<EntityType<F2Entity>> SPECIAL_F = registerEntity("special_f", () -> EntityType.Builder.of(F2Entity::new, MobCategory.CREATURE).sized(0.7F, 0.7F), AlphabetEntity::createAttributes);
    public static final RegistryObject<EntityType<H2Entity>> SPECIAL_H = registerEntity("special_h", () -> EntityType.Builder.of(H2Entity::new, MobCategory.CREATURE).sized(0.7F, 0.7F), AlphabetEntity::createAttributes);
    public static final RegistryObject<EntityType<E2Entity>> SPECIAL_E = registerEntity("special_e", () -> EntityType.Builder.of(E2Entity::new, MobCategory.CREATURE).sized(0.7F, 0.7F), AlphabetEntity::createAttributes);
    public static final RegistryObject<EntityType<WitherStormEntity>> WITHER_STORM_ENTITY = registerEntity("wither_storm", () -> EntityType.Builder.of(WitherStormEntity::new, MobCategory.MONSTER).sized(5.0f, 5.0f), WitherStormEntity::createAttributes);

    private static <T extends Entity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier) {
        return ENTITIES.register(name, () -> supplier.get().build(MODID + ":" + name));
    }

    private static <T extends LivingEntity> RegistryObject<EntityType<T>> registerEntity(String name, Supplier<EntityType.Builder<T>> supplier,
            Supplier<AttributeSupplier.Builder> attributeSupplier) {
        RegistryObject<EntityType<T>> entityTypeSupplier = registerEntity(name, supplier);
        attributeSuppliers.add(new AttributesRegister<>(entityTypeSupplier, attributeSupplier));
        return entityTypeSupplier;
    }

    @SubscribeEvent
    public static void attribs(EntityAttributeCreationEvent e) {
        attributeSuppliers.forEach(p -> e.put(p.entityTypeSupplier.get(), p.factory.get().build()));
    }

    private record AttributesRegister<E extends LivingEntity>(Supplier<EntityType<E>> entityTypeSupplier, Supplier<AttributeSupplier.Builder> factory) {}
}
