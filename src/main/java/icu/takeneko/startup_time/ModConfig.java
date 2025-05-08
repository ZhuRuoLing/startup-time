package icu.takeneko.startup_time;

import org.yaml.snakeyaml.DumperOptions;
import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;
import java.util.HashMap;

/***
 * 模组配置文件类
 * 声明：
 * 我不怎么接触 Java 本类是我根据以往编写C#的经验和翻看 Java 相关的资料完成的，
 * 编写中发现很多 C# 有的特性 Java 并没有，用起来感觉是真的麻烦，也可能是我的技术问题，
 * 这个功能类可能会出现不合理的设计，请见谅。
 * 功能：
 * 1. 读取配置文件，并加载到当前对象中
 * 2. 将配置文件转换为 YAML 字符串
 * 3. 检测读取的配置文件是否有缺少的成员，缺少的成员将使用默认值添加到配置文件
 * ------------------------------------------------------------------------
 * Module configuration file class
 * Function:
 * 1. Read the configuration file and load it into the current object
 * 2. Convert the configuration file to a YAML string
 * 4. Check if there are any missing members in the read configuration file, and add them to the configuration file if there are
 *
 * @author LaoMaoMAG
 * @version 1.0
 * @since 2025-05-09
 */
public class ModConfig {
    // 配置文件路径
    // configuration file path
    private static final Path CONFIG_FILE_Path = Path.of("config/startup_time/config.yml");

    /***
     * 配置文件成员
     * 如果需要添加新的配置字段，请在此类添加公共成员
     * 添加后会自动创建之前配置文件缺少的成员字段
     * ------------------------------------------------------------------------
     * Configuration file members
     * If you need to add new configuration fields, please add them to this class
     */

    // 消息内容（启动时间弹窗）
    // Message content (startup time pop-up window)
    public String messageContent = ""; // Java 居然不能设置属性访问器？感觉写一个 getMessageContent() 很不优雅，就这样吧 =(

    // 构造方法
    // Constructor
    public ModConfig() {
        loadConfig();
    }

    // 加载配置文件
    // Load configuration file
    public void loadConfig() {
        try {
            // 如果配置文件不存在，创建文件并写入默认配置
            // If the configuration file does not exist, create the file and write the default configuration
            if (!Files.exists(CONFIG_FILE_Path)) {
                Files.createDirectories(CONFIG_FILE_Path.getParent());
                // 将类本身符合的条件的成员转换为 YAML 字符串
                // Convert the class itself to a YAML string
                Files.writeString(CONFIG_FILE_Path, toYaml());
            }
            // 读取和解析配置文件
            String yamlString = Files.readString(CONFIG_FILE_Path);
            Map<String, Object> loadedData = new Yaml().load(yamlString);
            fromYaml(yamlString);
            // 检查是否缺少字段并更新配置文件
            updateMissingFieldsInYaml(loadedData);
        } catch (IOException e) {
            // 加载配置文件时错误
            String errorMessage = "Error in loading configuration file";
            Mod.Logger.error(errorMessage); // 错误日志
        }
    }

    // 获取所有公共成员构成的 Map
    // Get a map consisting of all public members
    public Map<String, Object> toPublicMap() {
        Map<String, Object> result = new HashMap<>();
        for (java.lang.reflect.Field field : this.getClass().getDeclaredFields()) {
            if (java.lang.reflect.Modifier.isPublic(field.getModifiers())
                    && !java.lang.reflect.Modifier.isStatic(field.getModifiers())
                    && !field.getName().equals("secret")) {
                try {
                    result.put(field.getName(), field.get(this));
                } catch (IllegalAccessException e) {
                    // 加载配置文件时获取类“ModConfig”的公共成员的 Map 时错误
                    String errorMessage = "Error in obtaining Map for public members of class' ModConfig 'while loading configuration file";
                    Mod.Logger.error(errorMessage, e); // 错误日志
                }
            }
        }
        return result;
    }

    // 转换为 YAML 字符串
    // Convert to YAML string
    public String toYaml() {
        DumperOptions options = new DumperOptions();
        options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
        Yaml yaml = new Yaml(options);
        return yaml.dump(toPublicMap());
    }

    // 从 YAML 字符串加载数据到当前对象
    // Load data from YAML string to current object
    public void fromYaml(String yamlStr) {
        Yaml yaml = new Yaml();
        Map<String, Object> data = yaml.load(yamlStr);
        for (Map.Entry<String, Object> entry : data.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            try {
                Field field = this.getClass().getField(key); // 只获取 public 字段
                if (!field.getName().equals("secret")) {
                    field.set(this, value);
                }
            } catch (NoSuchFieldException | IllegalAccessException e) {
                // 无法设置配置字段
                Mod.Logger.warn("Unable to set configuration field: {}", key, e); // 错误日志
            }
        }
    }

    // 更新配置文件中缺失的字段
    // Update missing fields in the configuration file
    private void updateMissingFieldsInYaml(Map<String, Object> loadedData) throws IOException {
        Map<String, Object> currentPublicMap = toPublicMap();
        // 检查哪些字段缺失
        // Check which fields are missing
        boolean hasMissingFields = false;
        for (String key : currentPublicMap.keySet()) {
            if (!loadedData.containsKey(key)) {
                loadedData.put(key, currentPublicMap.get(key));
                hasMissingFields = true;
            }
        }
        // 如果有缺失字段则更新配置文件
        // Update configuration file if there are missing fields
        if (hasMissingFields) {
            DumperOptions options = new DumperOptions();
            options.setDefaultFlowStyle(DumperOptions.FlowStyle.BLOCK);
            Yaml yaml = new Yaml(options);
            String updatedYaml = yaml.dump(loadedData);
            Files.writeString(CONFIG_FILE_Path, updatedYaml);
        }
    }
}
