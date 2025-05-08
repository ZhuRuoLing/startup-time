package icu.takeneko.startup_time;

import icu.takeneko.startup_time.datagen.TranslationProvider;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.minecraft.data.DataGenerator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Mod implements ClientModInitializer, DataGeneratorEntrypoint {
    // 日志记录器
    public static final Logger Logger = LoggerFactory.getLogger("Startup Time");

    @Override
    public void onInitializeClient() {
        Logger.info("Mod initialized!");
    }

    @Override
    public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
        FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
        ((DataGenerator.Pack)pack).addProvider(output -> new TranslationProvider((FabricDataOutput) output, "zh_cn"));
        ((DataGenerator.Pack)pack).addProvider(output -> new TranslationProvider((FabricDataOutput) output, "en_us"));
    }
}